package escape.builder;

import escape.LocationType;
import escape.required.LocationType.*;
import escape.required.EscapePiece.*;

public class LocationInitializer {
    public int x, y;
    public LocationType locationType;
    public String player;
    public PieceName pieceName;

    public LocationInitializer() { }

    public LocationInitializer(int x, int y, LocationType locationType,
                               String player, PieceName pieceName) {
        this.x = x;
        this.y = y;
        this.locationType = locationType;
        this.player = player;
        this.pieceName = pieceName;
    }

    @Override
    public String toString() {
        return "LocationInitializer [x=" + x + ", y=" + y +
                ", locationType=" + locationType + ", player=" + player +
                ", pieceName=" + pieceName + "]";
    }
}
