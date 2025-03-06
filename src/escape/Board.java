package escape;

import escape.required.Coordinate;
import escape.required.EscapePiece;
import java.util.*;

public class Board {
    private final int xMax;  // if 0, infinite in x-dimension
    private final int yMax;  // if 0, infinite in y-dimension

    // Map to hold pieces.
    private final Map<Coordinate, EscapePiece> pieceMap = new HashMap<>();

    // Map to hold location types. Default is CLEAR.
    private final Map<Coordinate, LocationType> locationMap = new HashMap<>();

    public Board(int xMax, int yMax) {
        this.xMax = xMax;
        this.yMax = yMax;
    }

    /**
     * For infinite boards (xMax==0 and yMax==0), any integer coordinate is allowed.
     * For finite boards, assume indices start at 1 and are exclusive (or adjust as needed).
     */
    public boolean isInBounds(int x, int y) {
        if (xMax == 0 && yMax == 0) {
            return true;
        }
        if (x < 1 || y < 1) {
            return false;
        }
        return (x <= xMax && y <= yMax);
    }

    public EscapePiece getPieceAt(Coordinate c) {
        return pieceMap.get(c);
    }

    public void putPieceAt(EscapePiece piece, Coordinate c) {
        pieceMap.put(c, piece);
    }

    public void removePieceAt(Coordinate c) {
        pieceMap.remove(c);
    }

    public LocationType getLocationType(Coordinate c) {
        return locationMap.getOrDefault(c, LocationType.CLEAR);
    }

    public void setLocationType(Coordinate c, LocationType type) {
        locationMap.put(c, type);
    }

    public Collection<EscapePiece> getAllPieces() {
        return pieceMap.values();
    }

    public Set<Coordinate> getAllCoordinates() {
        return pieceMap.keySet();
    }
}
