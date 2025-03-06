package escape.builder;

import escape.required.EscapePiece.*;
import java.util.*;

public class PieceTypeDescriptor {
    private PieceName pieceName;
    private MovementPattern movementPattern;
    private PieceAttribute[] attributes;

    public PieceTypeDescriptor() {}

    public PieceName getPieceName() {
        return pieceName;
    }
    public void setPieceName(PieceName pieceName) {
        this.pieceName = pieceName;
    }

    public MovementPattern getMovementPattern() {
        return movementPattern;
    }
    public void setMovementPattern(MovementPattern movementPattern) {
        this.movementPattern = movementPattern;
    }

    public PieceAttribute[] getAttributes() {
        return attributes;
    }
    public void setAttributes(PieceAttribute... attributes) {
        this.attributes = attributes;
    }

    public PieceAttribute getAttribute(PieceAttributeID id) {
        return Arrays.stream(attributes)
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "PieceTypeDescriptor [pieceName=" + pieceName +
                ", movementPattern=" + movementPattern +
                ", attributes=" + Arrays.toString(attributes) + "]";
    }
}
