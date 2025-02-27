package escape;

import escape.required.*;
import escape.required.Coordinate.CoordinateType;
import escape.required.EscapePiece.*;
import escape.builder.PieceTypeDescriptor;
import escape.builder.RuleDescriptor;
import escape.required.Rule;
import java.util.*;

public class EscapeGameManagerImpl<C extends Coordinate> implements EscapeGameManager<C>
{
    private final int xMax;
    private final int yMax;
    private final CoordinateType coordinateType;
    private final List<String> players;
    private int currentPlayerIndex = 0;

    private final Board board;
    private final Map<PieceName, PieceTypeDescriptor> pieceTypes;
    private final RuleDescriptor[] ruleDescriptors;  // Game rules from configuration

    // Track scores for each player.
    private final Map<String, Integer> scores = new HashMap<>();

    public EscapeGameManagerImpl(
            int xMax,
            int yMax,
            CoordinateType coordinateType,
            List<String> players,
            Board board,
            Map<PieceName, PieceTypeDescriptor> pieceTypes,
            RuleDescriptor[] ruleDescriptors)
    {
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
    public C makeCoordinate(int x, int y)
    {
        if (!board.isInBounds(x, y)) {
            return null;
        }
        return (C) new CoordinateImpl(x, y);
    }

    @Override
    public GameStatus move(C from, C to)
    {
        // 1) Check bounds.
        if (!isValidCoordinate(from) || !isValidCoordinate(to)) {
            return invalidMove(from);
        }

        // 2) Check that a piece exists at 'from' belonging to the current player.
        EscapePiece piece = board.getPieceAt(from);
        if (piece == null) {
            return invalidMove(from);
        }
        String currentPlayer = players.get(currentPlayerIndex);
        if (!piece.getPlayer().equals(currentPlayer)) {
            return invalidMove(from);
        }
        // 3) Cannot move onto an occupied square.
        if (board.getPieceAt(to) != null) {
            return invalidMove(from);
        }

        // 4) Validate movement.
        if (!isLegalMove((EscapePieceImpl) piece, from, to)) {
            return invalidMove(from);
        }

        // 5) Check for EXIT along the move path.
        boolean exitEncountered = false;
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getColumn() - from.getColumn();
        int steps = Math.max(Math.abs(rowDiff), Math.abs(colDiff));
        if (steps > 1) {
            int stepRow = rowDiff / steps;
            int stepCol = colDiff / steps;
            int currRow = from.getRow();
            int currCol = from.getColumn();
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
            return invalidMove(from);
        }
        if (destType == LocationType.EXIT || exitEncountered) {
            pieceExits = true;
        }

        // 7) Process the move.
        board.removePieceAt(from);
        if (pieceExits) {
            int currentScore = scores.getOrDefault(currentPlayer, 0);
            scores.put(currentPlayer, currentScore + ((EscapePieceImpl) piece).getValue());
            to = null;  // Piece exits the game.
        }
        else {
            board.putPieceAt(piece, to);
        }

        // 8) Check game-end conditions for the mover.
        GameStatus.MoveResult result = checkGameEnd(currentPlayer);

        // 9) Advance turn if game not ended.
        if (result == GameStatus.MoveResult.NONE) {
            // Advance turn to next player.
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            // Check if the next player has any pieces; if not, the opponent wins.
            String nextPlayer = players.get(currentPlayerIndex);
            if (!playerHasPieces(nextPlayer)) {
                result = GameStatus.MoveResult.WIN;
                // Optionally, you might want to set currentPlayerIndex to the winning player.
            }
        }

        return new GameStatusImpl(true, result, to);
    }

    @Override
    public EscapePiece getPieceAt(C coordinate)
    {
        return board.getPieceAt(coordinate);
    }

    @Override
    public GameObserver addObserver(GameObserver observer) {
        throw new EscapeException("Not implemented");
    }

    @Override
    public GameObserver removeObserver(GameObserver observer) {
        throw new EscapeException("Not implemented");
    }

    // --- Helper methods ---

    private boolean isValidCoordinate(C c)
    {
        return (c != null && board.isInBounds(c.getRow(), c.getColumn()));
    }

    private GameStatus invalidMove(C triedFrom)
    {
        return new GameStatusImpl(false, GameStatus.MoveResult.NONE, triedFrom);
    }

    /**
     * Validate the move.
     * For SQUARE boards, supports ORTHOGONAL or DIAGONAL moves.
     * For HEX boards, supports LINEAR moves.
     * For non-flying pieces, intermediate BLOCK locations are illegal.
     * (EXIT locations are allowed, as the exit behavior is handled in move().)
     */
    private boolean isLegalMove(EscapePieceImpl piece, C from, C to)
    {
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getColumn() - from.getColumn();
        int maxDist = piece.getDistance();
        if (maxDist <= 0) {
            maxDist = 1;
        }

        if (coordinateType == Coordinate.CoordinateType.SQUARE) {
            if (piece.getMovementPattern() == MovementPattern.ORTHOGONAL) {
                if (rowDiff != 0 && colDiff != 0) return false;
            }
            else if (piece.getMovementPattern() == MovementPattern.DIAGONAL) {
                if (rowDiff == 0 || colDiff == 0) return false;
                if (Math.abs(rowDiff) != Math.abs(colDiff)) return false;
            }

            else {
                return false;
            }
            if (Math.max(Math.abs(rowDiff), Math.abs(colDiff)) > maxDist) {
                return false;
            }
            if (!piece.canFly()) {
                int steps = Math.max(Math.abs(rowDiff), Math.abs(colDiff));
                int stepRow = (steps == 0) ? 0 : rowDiff / steps;
                int stepCol = (steps == 0) ? 0 : colDiff / steps;
                int currRow = from.getRow();
                int currCol = from.getColumn();
                for (int i = 1; i < steps; i++) {
                    currRow += stepRow;
                    currCol += stepCol;
                    Coordinate intermediate = new CoordinateImpl(currRow, currCol);
                    // For non-flyers, BLOCK locations are illegal.
                    if (board.getLocationType(intermediate) == LocationType.BLOCK) {
                        return false;
                    }
                    if (board.getPieceAt(intermediate) != null) {
                        return false;
                    }
                }
            }
            return true;
        }
        else if (coordinateType == Coordinate.CoordinateType.HEX) {
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
            if (!validDir || steps > maxDist) {
                return false;
            }
            if (!piece.canFly()) {
                int stepRow = rowDiff / steps;
                int stepCol = colDiff / steps;
                int currRow = from.getRow();
                int currCol = from.getColumn();
                for (int i = 1; i < steps; i++) {
                    currRow += stepRow;
                    currCol += stepCol;
                    Coordinate intermediate = new CoordinateImpl(currRow, currCol);
                    if (board.getLocationType(intermediate) == LocationType.BLOCK) {
                        return false;
                    }
                    if (board.getPieceAt(intermediate) != null) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Check game-end conditions using rule descriptors.
     * In addition to a SCORE rule, if the next player (whose turn it is) has no pieces,
     * then the opponent (the player who just moved) wins.
     */
    private GameStatus.MoveResult checkGameEnd(String currentPlayer)
    {
        // Check SCORE rule.
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
        // Also, if the current mover has no pieces left, they lose.
        if (!playerHasPieces(currentPlayer)) {
            String opponent = getOpponent(currentPlayer);
            int currentScore = scores.getOrDefault(currentPlayer, 0);
            int opponentScore = scores.getOrDefault(opponent, 0);
            if (currentScore >= opponentScore) {
                return GameStatus.MoveResult.WIN;
            } else {
                return GameStatus.MoveResult.LOSE;
            }
        }
        return GameStatus.MoveResult.NONE;
    }

    private boolean playerHasPieces(String player) {
        // Assuming Board.java has a method to return all pieces.
        for (EscapePiece piece : board.getAllPieces()) {
            if (piece.getPlayer().equals(player)) {
                return true;
            }
        }
        return false;
    }

    private String getOpponent(String currentPlayer) {
        for (String p : players) {
            if (!p.equals(currentPlayer)) {
                return p;
            }
        }
        return currentPlayer; // fallback
    }

    // --- Getters if needed ---
    public int getxMax() { return xMax; }
    public int getyMax() { return yMax; }
    public CoordinateType getCoordinateType() { return coordinateType; }
    public List<String> getPlayers() { return new ArrayList<>(players); }
}
