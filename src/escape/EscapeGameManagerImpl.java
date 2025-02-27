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

        // 2) Check there is a piece at 'from' belonging to the current player.
        EscapePiece piece = board.getPieceAt(from);
        if (piece == null) {
            return invalidMove(from);
        }
        String currentPlayer = players.get(currentPlayerIndex);
        if (!piece.getPlayer().equals(currentPlayer)) {
            return invalidMove(from);
        }
        // 3) Cannot move onto a square that already has a piece.
        if (board.getPieceAt(to) != null) {
            return invalidMove(from);
        }

        // 4) Validate movement based on board type and piece movement pattern.
        if (!isLegalMove((EscapePieceImpl) piece, from, to)) {
            return invalidMove(from);
        }

        // 5) Check destination location type.
        LocationType destType = board.getLocationType(to);
        boolean pieceExits = false;
        if (destType == LocationType.BLOCK) {
            return invalidMove(from);
        }
        if (destType == LocationType.EXIT) {
            pieceExits = true;
        }

        // 6) Process move.
        board.removePieceAt(from);
        if (pieceExits) {
            int currentScore = scores.getOrDefault(currentPlayer, 0);
            scores.put(currentPlayer, currentScore + ((EscapePieceImpl) piece).getValue());
            // The piece exits; do not place it on the board.
        }
        else {
            board.putPieceAt(piece, to);
        }

        // 7) Check game-end conditions (e.g., SCORE rule).
        GameStatus.MoveResult result = checkGameEnd(currentPlayer);

        // 8) Advance turn if game not ended.
        if (result == GameStatus.MoveResult.NONE) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
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
     * Validate the move. For SQUARE boards, support ORTHOGONAL or DIAGONAL moves.
     * For HEX boards, support LINEAR moves along one of six directions.
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
            // For SQUARE boards, allow:
            // - ORTHOGONAL: either rowDiff==0 or colDiff==0.
            // - DIAGONAL: absolute differences equal.
            if (piece.getMovementPattern() == MovementPattern.ORTHOGONAL) {
                if (rowDiff != 0 && colDiff != 0) return false;
            }
            else if (piece.getMovementPattern() == MovementPattern.DIAGONAL) {
                // Allow both orthogonal and diagonal moves.
                if (rowDiff != 0 && colDiff != 0 && Math.abs(rowDiff) != Math.abs(colDiff)) return false;
            }
            else {
                // Unsupported movement pattern for square board.
                return false;
            }
            // Check distance.
            if (Math.max(Math.abs(rowDiff), Math.abs(colDiff)) > maxDist) {
                return false;
            }
            // If the piece cannot fly, ensure intermediate squares are clear
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
                    // Cannot pass over BLOCK or EXIT.
                    LocationType lt = board.getLocationType(intermediate);
                    if (lt == LocationType.BLOCK || lt == LocationType.EXIT) {
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
            // For HEX boards, we assume LINEAR movement.
            // Define six hex directions.
            int[][] hexDirs = { {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0} };
            boolean validDir = false;
            int steps = 0;
            for (int[] d : hexDirs) {
                // Check if the move is along this direction.
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
            // For non-flyers, check intermediate squares.
            if (!piece.canFly()) {
                int stepRow = rowDiff / steps;
                int stepCol = colDiff / steps;
                int currRow = from.getRow();
                int currCol = from.getColumn();
                for (int i = 1; i < steps; i++) {
                    currRow += stepRow;
                    currCol += stepCol;
                    Coordinate intermediate = new CoordinateImpl(currRow, currCol);
                    LocationType lt = board.getLocationType(intermediate);
                    if (lt == LocationType.BLOCK || lt == LocationType.EXIT) {
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
     * For example, if a rule with ruleId SCORE exists and the current player's score
     * is at or above its ruleValue, then the current player wins.
     */
    private GameStatus.MoveResult checkGameEnd(String currentPlayer)
    {
        if (ruleDescriptors != null) {
            for (RuleDescriptor rd : ruleDescriptors) {
                if (rd.ruleId == Rule.RuleID.SCORE) {
                    int targetScore = rd.ruleValue;
                    if (scores.getOrDefault(currentPlayer, 0) >= targetScore) {
                        return GameStatus.MoveResult.WIN;
                    }
                }
                // Additional rules (e.g., no moves available, tie) can be added here.
            }
        }
        return GameStatus.MoveResult.NONE;
    }

    // --- Getters if needed ---
    public int getxMax() { return xMax; }
    public int getyMax() { return yMax; }
    public CoordinateType getCoordinateType() { return coordinateType; }
    public List<String> getPlayers() { return new ArrayList<>(players); }
}
