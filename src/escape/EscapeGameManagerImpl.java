package escape;

import escape.required.*;
import escape.required.Coordinate.CoordinateType;
import escape.required.EscapePiece.*;
import escape.builder.PieceTypeDescriptor;
import escape.builder.RuleDescriptor;
import escape.required.Rule;
import java.util.*;

public class EscapeGameManagerImpl<C extends Coordinate> implements EscapeGameManager<C> {
    private final int xMax;
    private final int yMax;
    private final CoordinateType coordinateType;
    private final List<String> players;
    private int currentPlayerIndex = 0;

    private final Board board;
    private final Map<PieceName, PieceTypeDescriptor> pieceTypes;
    private final RuleDescriptor[] ruleDescriptors;  // SCORE, TURN_LIMIT, POINT_CONFLICT rules
    private final Map<String, Integer> scores = new HashMap<>();
    private int moveCount = 0;  // For TURN_LIMIT

    // Observer list for notifications.
    private final List<GameObserver> observers = new ArrayList<>();


    public EscapeGameManagerImpl(
            int xMax,
            int yMax,
            CoordinateType coordinateType,
            List<String> players,
            Board board,
            Map<PieceName, PieceTypeDescriptor> pieceTypes,
            RuleDescriptor[] ruleDescriptors) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.coordinateType = coordinateType;
        this.players = new ArrayList<>(players);
        this.board = board;
        this.pieceTypes = pieceTypes;
        this.ruleDescriptors = ruleDescriptors;
        for (String player : players) {
            scores.put(player, 0);
        }
    }

    @Override
    public C makeCoordinate(int x, int y) {
        if (!board.isInBounds(x, y)) return null;
        return (C) new CoordinateImpl(x, y);
    }

    @Override
    public GameStatus move(C from, C to) {
        moveCount++;  // Increase move count for TURN_LIMIT

        // 1) Validate coordinates.
        if (!isValidCoordinate(from) || !isValidCoordinate(to)) {
            notifyObservers("Move out of bounds.");
            return invalidMove(from);
        }

        // 2) Get piece at source and ensure it belongs to current player.
        EscapePiece piece = board.getPieceAt(from);
        String currentPlayer = players.get(currentPlayerIndex);
        if (piece == null || !piece.getPlayer().equals(currentPlayer)) {
            notifyObservers("No piece for current player at " + from);
            return invalidMove(from);
        }

        // 3) If destination occupied, resolve conflict if moving piece can JUMP.
        boolean conflictResolved = false;

        if (board.getPieceAt(to) != null) {
            if (((EscapePieceImpl) piece).canJump() || ((EscapePieceImpl) piece).hasUnblock() || containsPointConflict(ruleDescriptors)) {
                EscapePiece occupant = board.getPieceAt(to);
                int moverValue = ((EscapePieceImpl) piece).getValue();
                int occupantValue = ((EscapePieceImpl) occupant).getValue();
                if (moverValue > occupantValue) {
                    board.removePieceAt(to);
                    ((EscapePieceImpl) piece).reduceValue(occupantValue);
                    conflictResolved = true;
                    notifyObservers(currentPlayer + " wins conflict at " + to);
                }
                else if (moverValue == occupantValue) {
                    // Conflict draw: both pieces exit, no score change.
                    board.removePieceAt(to);
                    board.removePieceAt(from);
                    notifyObservers("Conflict draw at " + to + ". Both pieces exit.");
                    return new GameStatusImpl(true, GameStatus.MoveResult.DRAW, null, true);
                } else {
                    board.removePieceAt(from);
                    ((EscapePieceImpl) occupant).reduceValue(moverValue);
                    notifyObservers("Conflict lost by " + currentPlayer + "; attacker's piece removed, occupant's value reduced.");
                    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                    return new GameStatusImpl(true, GameStatus.MoveResult.NONE, null, true);
                }
            } else {
                notifyObservers("Destination occupied at " + to);
                return invalidMove(from);
            }
        }

        // 4) Validate movement.
        if (!isLegalMove((EscapePieceImpl) piece, from, to)) {
            notifyObservers("Illegal move from " + from + " to " + to);
            return invalidMove(from);
        }

        // 5) Check for EXIT encountered along path.
        boolean exitEncountered = false;
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getColumn() - from.getColumn();
        int steps = Math.max(Math.abs(rowDiff), Math.abs(colDiff));
        if (steps > 1) {
            int stepRow = rowDiff / steps;
            int stepCol = colDiff / steps;
            int currRow = from.getRow(), currCol = from.getColumn();
            for (int i = 1; i < steps; i++) {
                currRow += stepRow;
                currCol += stepCol;
                Coordinate intermediate = new CoordinateImpl(currRow, currCol);
                if (board.getLocationType(intermediate) == LocationType.EXIT) {
                    exitEncountered = true;
                    break;
                }
            }
        }

        // 6) Check destination location type.
        LocationType destType = board.getLocationType(to);
        boolean pieceExits = false;
        if (destType == LocationType.BLOCK) {
            notifyObservers("Destination BLOCK at " + to);
            return invalidMove(from);
        }
        if (destType == LocationType.EXIT || exitEncountered) {
            pieceExits = true;
        }

        // 7) Process the move.
        board.removePieceAt(from);
        if (pieceExits) {
            int curScore = scores.getOrDefault(currentPlayer, 0);
            scores.put(currentPlayer, curScore + ((EscapePieceImpl) piece).getValue());
            to = null;  // Piece exits the game.
            notifyObservers(currentPlayer + "'s piece exits.");
        } else {
            board.putPieceAt(piece, to);
        }
        if (!playerHasPieces(currentPlayer)) {
            String opponent = getOpponent(currentPlayer);
            int moverScore = scores.getOrDefault(currentPlayer, 0);
            int oppScore = scores.getOrDefault(opponent, 0);
            GameStatus.MoveResult outcome;
            if (moverScore == oppScore) {
                outcome = GameStatus.MoveResult.DRAW;
            } else if (oppScore > moverScore) {
                outcome = GameStatus.MoveResult.LOSE; // mover loses
            } else {
                outcome = GameStatus.MoveResult.WIN;
            }
            notifyObservers("No pieces left for " + currentPlayer + ". Outcome: " + outcome);
            return new GameStatusImpl(true, outcome, to, true);
        }

        // 8) Check SCORE rule immediately after processing the move.
        GameStatus.MoveResult result = checkGameEnd(currentPlayer);
        if (result == GameStatus.MoveResult.WIN) {
            notifyObservers("SCORE rule met. " + currentPlayer + " wins.");
            return new GameStatusImpl(true, result, to, true);
        }

        // 9) Check TURN_LIMIT rule.
        int turnLimit = getTurnLimit();
        if (turnLimit > 0 && moveCount >= turnLimit) {
            result = determineWinner();
            notifyObservers("Turn limit reached. Result: " + result);
            return new GameStatusImpl(true, result, to, true);
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        String nextPlayer = players.get(currentPlayerIndex);
        if (!playerHasPieces(nextPlayer) || !playerCanMove(nextPlayer)) {
            int nextScore = scores.getOrDefault(nextPlayer, 0);
            // Determine outcome: if the next player's score is lower or equal, the mover wins.
            // (Adjust the comparison as required by your game rules.)
            String mover = players.get((currentPlayerIndex + players.size() - 1) % players.size());
            int moverScore = scores.getOrDefault(mover, 0);
            result = (moverScore >= nextScore)
                    ? GameStatus.MoveResult.WIN
                    : GameStatus.MoveResult.LOSE;
            notifyObservers("No pieces or legal moves for " + nextPlayer + ". Outcome: " + result);
            return new GameStatusImpl(true, result, to, true);
        }

        notifyObservers("Move complete. Outcome: " + result);
        return new GameStatusImpl(true, result, to, conflictResolved);
    }




    @Override
    public EscapePiece getPieceAt(C coordinate) {
        return board.getPieceAt(coordinate);
    }

    @Override
    public GameObserver addObserver(GameObserver observer) {
        observers.add(observer);
        return observer;
    }

    @Override
    public GameObserver removeObserver(GameObserver observer) {
        observers.remove(observer);
        return observer;
    }

    private void notifyObservers(String message) {
        for (GameObserver o : observers) {
            o.notify(message);
        }
    }

    // --- Helper methods ---
    private boolean isValidCoordinate(C c) {
        return (c != null && board.isInBounds(c.getRow(), c.getColumn()));
    }

    private GameStatus invalidMove(C triedFrom) {
        return new GameStatusImpl(false, GameStatus.MoveResult.NONE, triedFrom, false);
    }

    /**
     * isLegalMove supports SQUARE and HEX boards.
     * For SQUARE boards, it distinguishes between ORTHOGONAL, DIAGONAL, and LINEAR.
     * Also applies rules based on piece attributes: FLY, UNBLOCK, JUMP.
     */
    private boolean isLegalMove(EscapePieceImpl piece, C from, C to) {
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getColumn() - from.getColumn();
        int maxDist = piece.getDistance();
        if (maxDist <= 0) maxDist = 1;

        if (coordinateType == Coordinate.CoordinateType.SQUARE) {
            MovementPattern mp = piece.getMovementPattern();
            if (mp == MovementPattern.ORTHOGONAL) {
                if (rowDiff != 0 && colDiff != 0) return false;
            } else if (mp == MovementPattern.DIAGONAL) {
                if (rowDiff == 0 || colDiff == 0) return false;
                if (Math.abs(rowDiff) != Math.abs(colDiff)) return false;
            } else if (mp == MovementPattern.LINEAR) {
                // Linear: allow either orthogonal or diagonal moves.
                if (!((rowDiff == 0 || colDiff == 0) || (Math.abs(rowDiff) == Math.abs(colDiff)))) return false;
            } else {
                return false;
            }
            if (Math.max(Math.abs(rowDiff), Math.abs(colDiff)) > maxDist) return false;

            // For non-flyers, check intermediate squares.
            if (piece.canFly()) {
                return true;
            } else if (piece.hasUnblock()) {
                int steps = Math.max(Math.abs(rowDiff), Math.abs(colDiff));
                int stepRow = (steps == 0) ? 0 : rowDiff / steps;
                int stepCol = (steps == 0) ? 0 : colDiff / steps;
                int currRow = from.getRow(), currCol = from.getColumn();
                for (int i = 1; i < steps; i++) {
                    currRow += stepRow;
                    currCol += stepCol;
                    Coordinate intermediate = new CoordinateImpl(currRow, currCol);
                    // UNBLOCK: ignore BLOCK obstacles.
                    if (board.getPieceAt(intermediate) != null) return false;
                }
                return true;
            } else if (piece.canJump()) {
                int steps = Math.max(Math.abs(rowDiff), Math.abs(colDiff));
                int stepRow = (steps == 0) ? 0 : rowDiff / steps;
                int stepCol = (steps == 0) ? 0 : colDiff / steps;
                int currRow = from.getRow(), currCol = from.getColumn();
                int occupiedCount = 0;
                for (int i = 1; i < steps; i++) {
                    currRow += stepRow;
                    currCol += stepCol;
                    Coordinate intermediate = new CoordinateImpl(currRow, currCol);
                    // If a BLOCK is encountered, the move is invalid.
                    if (board.getLocationType(intermediate) == LocationType.BLOCK) return false;
                    // Count occupied squares.
                    if (board.getPieceAt(intermediate) != null) {
                        occupiedCount++;
                        if (occupiedCount > 1) return false; // Can't jump over more than one consecutive piece.
                    }
                }
                return true;
            }
            else {
                // Regular piece: all intermediate squares must be unoccupied and not BLOCK.
                int steps = Math.max(Math.abs(rowDiff), Math.abs(colDiff));
                int stepRow = (steps == 0) ? 0 : rowDiff / steps;
                int stepCol = (steps == 0) ? 0 : colDiff / steps;
                int currRow = from.getRow(), currCol = from.getColumn();
                for (int i = 1; i < steps; i++) {
                    currRow += stepRow;
                    currCol += stepCol;
                    Coordinate intermediate = new CoordinateImpl(currRow, currCol);
                    if (board.getLocationType(intermediate) == LocationType.BLOCK) return false;
                    if (board.getPieceAt(intermediate) != null) return false;
                }
                return true;
            }
        } else if (coordinateType == Coordinate.CoordinateType.HEX) {
            // HEX board: use linear movement in one of six directions.
            int[][] hexDirs = { {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0} };
            boolean validDir = false;
            int steps = 0;
            for (int[] d : hexDirs) {
                if (d[0] != 0) {
                    double factor = (double) rowDiff / d[0];
                    if (factor > 0 && Math.abs(factor - Math.round(factor)) < 0.0001) {
                        if (d[1] * Math.round(factor) == colDiff) {
                            validDir = true;
                            steps = (int)Math.round(factor);
                            break;
                        }
                    }
                } else if (d[1] != 0) {
                    double factor = (double) colDiff / d[1];
                    if (factor > 0 && Math.abs(factor - Math.round(factor)) < 0.0001) {
                        if (d[0] * Math.round(factor) == rowDiff) {
                            validDir = true;
                            steps = (int)Math.round(factor);
                            break;
                        }
                    }
                }
            }
            if (!validDir || steps > maxDist) return false;
            if (piece.canFly()) {
                return true;
            } else if (piece.hasUnblock()) {
                int stepRow = rowDiff / steps;
                int stepCol = colDiff / steps;
                int currRow = from.getRow(), currCol = from.getColumn();
                for (int i = 1; i < steps; i++) {
                    currRow += stepRow;
                    currCol += stepCol;
                    Coordinate intermediate = new CoordinateImpl(currRow, currCol);
                    if (board.getPieceAt(intermediate) != null) return false;
                }
                return true;
            } else if (piece.canJump()) {
                int stepRow = rowDiff / steps;
                int stepCol = colDiff / steps;
                int currRow = from.getRow(), currCol = from.getColumn();
                for (int i = 1; i < steps; i++) {
                    currRow += stepRow;
                    currCol += stepCol;
                    Coordinate intermediate = new CoordinateImpl(currRow, currCol);
                    if (board.getLocationType(intermediate) == LocationType.BLOCK) return false;
                }
                return true;
            } else {
                int stepRow = rowDiff / steps;
                int stepCol = colDiff / steps;
                int currRow = from.getRow(), currCol = from.getColumn();
                for (int i = 1; i < steps; i++) {
                    currRow += stepRow;
                    currCol += stepCol;
                    Coordinate intermediate = new CoordinateImpl(currRow, currCol);
                    if (board.getLocationType(intermediate) == LocationType.BLOCK) return false;
                    if (board.getPieceAt(intermediate) != null) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Check game-end conditions:
     * - SCORE rule: current player wins if score reaches target.
     * - TURN_LIMIT: if reached, game ends and winner is determined by highest score.
     * - Also, if the next player has no legal moves, then the mover wins.
     */
    private GameStatus.MoveResult checkGameEnd(String currentPlayer) {
        // SCORE rule.
        if (ruleDescriptors != null) {
            for (RuleDescriptor rd : ruleDescriptors) {
                if (rd.ruleId == Rule.RuleID.SCORE) {
                    int targetScore = rd.ruleValue;
                    if (scores.getOrDefault(currentPlayer, 0) >= targetScore) {
                        return GameStatus.MoveResult.WIN;
                    }
                }
            }
        }
        return GameStatus.MoveResult.NONE;
    }

    private int getTurnLimit() {
        if (ruleDescriptors != null) {
            for (RuleDescriptor rd : ruleDescriptors) {
                if (rd.ruleId == Rule.RuleID.TURN_LIMIT) {
                    return rd.ruleValue;
                }
            }
        }
        return 0;
    }

    private GameStatus.MoveResult determineWinner() {
        // Assume two players.
        String playerA = players.get(0);
        String playerB = players.get(1);
        int scoreA = scores.getOrDefault(playerA, 0);
        int scoreB = scores.getOrDefault(playerB, 0);
        if (scoreA == scoreB) {
            return GameStatus.MoveResult.DRAW;
        }
        String current = players.get(currentPlayerIndex);
        return (current.equals(playerA) && scoreA > scoreB) || (current.equals(playerB) && scoreB > scoreA)
                ? GameStatus.MoveResult.WIN
                : GameStatus.MoveResult.LOSE;
    }


    private boolean playerHasPieces(String player) {
        for (EscapePiece piece : board.getAllPieces()) {
            if (piece.getPlayer().equals(player)) return true;
        }
        return false;
    }

    private String getOpponent(String currentPlayer) {
        for (String p : players) {
            if (!p.equals(currentPlayer)) return p;
        }
        return currentPlayer;
    }

    // Helper method: check if a player can make any legal move.
    private boolean playerCanMove(String player) {
        if (!playerHasPieces(player)) return false;
        for (Coordinate c : board.getAllCoordinates()) {
            EscapePiece piece = board.getPieceAt(c);
            if (piece != null && piece.getPlayer().equals(player)) {
                int maxDist = 1;
                if (piece instanceof EscapePieceImpl) {
                    maxDist = ((EscapePieceImpl) piece).getDistance();
                }
                if (coordinateType == Coordinate.CoordinateType.SQUARE) {
                    for (int dx = -maxDist; dx <= maxDist; dx++) {
                        for (int dy = -maxDist; dy <= maxDist; dy++) {
                            if (dx == 0 && dy == 0) continue;
                            Coordinate candidate = new CoordinateImpl(c.getRow() + dx, c.getColumn() + dy);
                            if (!board.isInBounds(candidate.getRow(), candidate.getColumn())) continue;
                            if (board.getPieceAt(candidate) != null) continue;
                            if (isLegalMove((EscapePieceImpl) piece, (C)c, (C)candidate)) {
                                return true;
                            }
                        }
                    }
                } else if (coordinateType == Coordinate.CoordinateType.HEX) {
                    int[][] hexDirs = { {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0} };
                    for (int[] d : hexDirs) {
                        for (int step = 1; step <= maxDist; step++) {
                            int candidateRow = c.getRow() + d[0] * step;
                            int candidateCol = c.getColumn() + d[1] * step;
                            Coordinate candidate = new CoordinateImpl(candidateRow, candidateCol);
                            if (!board.isInBounds(candidate.getRow(), candidate.getColumn())) break;
                            if (board.getPieceAt(candidate) != null) break;
                            if (board.getLocationType(candidate) == LocationType.BLOCK) continue;
                            if (isLegalMove((EscapePieceImpl) piece, (C)c, (C)candidate)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public int getxMax() { return xMax; }
    public int getyMax() { return yMax; }
    public CoordinateType getCoordinateType() { return coordinateType; }
    public List<String> getPlayers() { return new ArrayList<>(players); }
    private boolean containsPointConflict(RuleDescriptor[] rules) {
        if (rules != null) {
            for (RuleDescriptor rd : rules) {
                if (rd.ruleId == Rule.RuleID.POINT_CONFLICT) {
                    return true;
                }
            }
        }
        return false;
    }

}
