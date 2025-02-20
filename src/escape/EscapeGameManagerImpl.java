package escape;

import escape.required.*;
import escape.required.Coordinate.CoordinateType;
import escape.required.EscapePiece.*;
import escape.builder.PieceTypeDescriptor;

import java.util.*;

public class EscapeGameManagerImpl<C extends Coordinate> implements EscapeGameManager<C>
{
    private final int xMax;
    private final int yMax;
    private final CoordinateType coordinateType;
    private final List<String> players;
    private int currentPlayerIndex = 0;

    private final Board board;  // The new board object
    private final Map<PieceName, PieceTypeDescriptor> pieceTypes;

    public EscapeGameManagerImpl(
            int xMax,
            int yMax,
            CoordinateType coordinateType,
            List<String> players,
            Board board,
            Map<PieceName, PieceTypeDescriptor> pieceTypes)
    {
        this.xMax = xMax;
        this.yMax = yMax;
        this.coordinateType = coordinateType;
        this.players = new ArrayList<>(players);
        this.board = board;
        this.pieceTypes = pieceTypes;
    }

    @Override
    public C makeCoordinate(int x, int y)
    {
        // For milestone2, return null if it's not a valid "positive index" location.
        // If you want to allow out-of-bounds coords in future, you can adapt this.
        if (!board.isInBounds(x, y)) {
            return null;
        }
        return (C) new CoordinateImpl(x, y);
    }

    @Override
    public GameStatus move(C from, C to)
    {
        // 1) Check bounds
        if (!isValidCoordinate(from) || !isValidCoordinate(to)) {
            return invalidMove(from);
        }

        // 2) Check there's a piece at 'from' that belongs to current player
        EscapePiece piece = board.getPieceAt(from);
        if (piece == null) {
            return invalidMove(from);
        }

        String currentPlayerName = players.get(currentPlayerIndex);
        if (!piece.getPlayer().equals(currentPlayerName)) {
            return invalidMove(from);
        }

        if (board.getPieceAt(to) != null) {
            return invalidMove(from);
        }
        // 3) Validate the movement (ORTHOGONAL, DISTANCE, FLY)
        if (!isLegalMove((EscapePieceImpl) piece, from, to)) {
            return invalidMove(from);
        }

        // 4) If valid, move the piece
        board.removePieceAt(from);
        board.putPieceAt(piece, to);

        // 5) Advance the player turn
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

        // 6) Return GameStatus => isValidMove=true, MoveResult=NONE, finalLocation=to
        return new GameStatusImpl(true, GameStatus.MoveResult.NONE, to);
    }

    @Override
    public EscapePiece getPieceAt(C coordinate)
    {
        return board.getPieceAt(coordinate);
    }

    @Override
    public GameObserver addObserver(GameObserver observer) {
        // Not needed for milestone2
        throw new EscapeException("Not implemented");
    }

    @Override
    public GameObserver removeObserver(GameObserver observer) {
        // Not needed for milestone2
        throw new EscapeException("Not implemented");
    }

    // -------- Helper methods ----------

    private boolean isValidCoordinate(C c)
    {
        return (c != null && board.isInBounds(c.getRow(), c.getColumn()));
    }

    private GameStatus invalidMove(C triedFrom)
    {
        return new GameStatusImpl(false, GameStatus.MoveResult.NONE, triedFrom);
    }

    /**
     * Check if the piece can move from "from" to "to" using orthogonal rules
     * plus DISTANCE/FLY.
     */
    private boolean isLegalMove(EscapePieceImpl piece, C from, C to)
    {
        // For milestone2, we only support ORTHOGONAL.
        if (piece.getMovementPattern() != MovementPattern.ORTHOGONAL) {
            return false;
        }

        // If the piece can FLY, we ignore path-blocking (and everything is CLEAR anyway).
        // Check distance with Manhattan distance for orth moves:
        int maxDist = piece.getDistance();
        // If no DISTANCE attribute is set, you might interpret it as 1 or unlimited.
        if (maxDist <= 0) {
            maxDist = 1;
        }

        int manhattanDist =
                Math.abs(from.getRow() - to.getRow()) + Math.abs(from.getColumn() - to.getColumn());

        if (manhattanDist > maxDist) {
            return false;
        }

        // If the piece has FLY => no need to BFS check any blocking
        if (piece.canFly()) {
            return true;
        }
        // else BFS to ensure there's a connected path in orth steps (all CLEAR in milestone2).
        return canReachWithOrthSteps(from, to, maxDist);
    }

    /**
     * For non-FLY pieces, you can do a BFS or DFS to ensure "from" can reach "to" via orth steps
     * within 'maxDist'.
     * Since all squares are CLEAR in milestone2, this is mostly just checking that the
     * Manhattan distance is feasible. But let's do a BFS for completeness.
     */
    private boolean canReachWithOrthSteps(C from, C to, int maxSteps)
    {
        // Simple BFS in up/down/left/right directions
        Set<C> visited = new HashSet<>();
        Queue<C> queue = new LinkedList<>();
        queue.add(from);
        visited.add(from);

        int steps = 0;
        while (!queue.isEmpty() && steps <= maxSteps)
        {
            int size = queue.size();
            // We'll increment steps each "ring" of BFS
            for (int i = 0; i < size; i++) {
                C current = queue.poll();
                if (current.equals(to)) {
                    return true;
                }
                // Check the 4 orth neighbors
                int r = current.getRow();
                int cCol = current.getColumn();

                List<C> neighbors = List.of(
                        (C) new CoordinateImpl(r + 1, cCol),
                        (C) new CoordinateImpl(r - 1, cCol),
                        (C) new CoordinateImpl(r, cCol + 1),
                        (C) new CoordinateImpl(r, cCol - 1)
                );

                for (C neigh : neighbors) {
                    if (isValidCoordinate(neigh) && !visited.contains(neigh)) {

                        // If there's a piece in 'neigh', block it for non-flyers
                        if (board.getPieceAt(neigh) != null) {
                            // cannot pass through occupant squares
                            continue;  // <--- NEW LINE
                        }

                        visited.add(neigh);
                        queue.add(neigh);
                    }
                }
            }
            steps++;
        }
        return false;
    }

    // --- Getters if needed ---
    public int getxMax() { return xMax; }
    public int getyMax() { return yMax; }
    public CoordinateType getCoordinateType() { return coordinateType; }
    public List<String> getPlayers() { return new ArrayList<>(players); }
}
