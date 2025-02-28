package escape;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import escape.required.Coordinate;
import escape.required.EscapePiece;

/**
 * Represents a SQUARE board that can be finite or infinite.
 * For milestone3, an infinite board (xMax==0 and yMax==0) supports negative indices.
 * Also supports special location types: CLEAR, BLOCK, EXIT.
 */

public class Board
{
    private final int xMax;  // if 0, the board is infinite in x-dimension
    private final int yMax;  // if 0, the board is infinite in y-dimension

    // Map to hold pieces.
    private final Map<Coordinate, EscapePiece> pieceMap = new HashMap<>();

    // Map to hold location types. Default for any location is CLEAR.
    private final Map<Coordinate, LocationType> locationMap = new HashMap<>();

    public Board(int xMax, int yMax)
    {
        this.xMax = xMax;
        this.yMax = yMax;
    }

    /**
     * Check if (x,y) is in bounds.
     * For infinite boards (xMax==0 and yMax==0), any integer coordinate is allowed.
     * For finite boards, we assume positive indices (>=1) and x <= xMax, y <= yMax.
     */
    public boolean isInBounds(int x, int y)
    {
        if (xMax == 0 && yMax == 0) {
            return true; // infinite board: allow negatives and positives
        }
        if (x < 1 || y < 1) {
            return false;
        }
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

    /**
     * Get the type of a location; if none is set, assume CLEAR.
     */
    public LocationType getLocationType(Coordinate c)
    {
        return locationMap.getOrDefault(c, LocationType.CLEAR);
    }

    /**
     * Set a special location type at the given coordinate.
     */
    public void setLocationType(Coordinate c, LocationType type)
    {
        locationMap.put(c, type);
    }
    public Collection<EscapePiece> getAllPieces() {
        return pieceMap.values();
    }
    public Set<Coordinate> getAllCoordinates() {
        return pieceMap.keySet();
    }


}
