package escape;

import java.util.HashMap;
import java.util.Map;

import escape.required.Coordinate;
import escape.required.EscapePiece;

/**
 * Represents a SQUARE board that can be finite or infinite.
 * In milestone2, all locations are CLEAR, so no EXIT/BLOCK checks yet.
 */
public class Board
{
    private final int xMax;  // 0 => infinite in the x-dimension
    private final int yMax;  // 0 => infinite in the y-dimension

    // For an infinite board, store pieces in a map instead of a 2D array
    private final Map<Coordinate, EscapePiece> pieceMap = new HashMap<>();

    public Board(int xMax, int yMax)
    {
        this.xMax = xMax;
        this.yMax = yMax;
    }

    /**
     * Check if (x, y) is in valid bounds.
     * Milestone2: we allow "infinite" if xMax/yMax == 0.
     * But x,y must be >= 1 (positive indices).
     */
    public boolean isInBounds(int x, int y)
    {
        // Must be positive
        if (x < 1 || y < 1) {
            return false;
        }
        // infinite in both directions
        if (xMax == 0 && yMax == 0) {
            return true;
        }
        // infinite in x only
        if (xMax == 0 && yMax > 0) {
            return (y <= yMax);
        }
        // infinite in y only
        if (yMax == 0 && xMax > 0) {
            return (x <= xMax);
        }
        // finite board
        return (x <= xMax && y <= yMax);
    }

    public EscapePiece getPieceAt(Coordinate c)
    {
        return pieceMap.get(c);
    }

    public void putPieceAt(EscapePiece piece, Coordinate c)
    {
        pieceMap.put(c, piece);
    }

    public void removePieceAt(Coordinate c)
    {
        pieceMap.remove(c);
    }
}
