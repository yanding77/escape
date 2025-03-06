package escape;

import escape.builder.PieceAttribute;
import escape.required.EscapePiece;
import escape.builder.PieceTypeDescriptor;
import java.util.HashMap;
import java.util.Map;

public class EscapePieceImpl implements EscapePiece {
    private final PieceName name;
    private final String player;
    private final MovementPattern movementPattern;
    private final Map<PieceAttributeID, Integer> attributes = new HashMap<>();

    public EscapePieceImpl(String player, PieceTypeDescriptor descriptor) {
        this.name = descriptor.getPieceName();
        this.player = player;
        this.movementPattern = descriptor.getMovementPattern();
        if (descriptor.getAttributes() != null) {
            for (PieceAttribute attr : descriptor.getAttributes()) {
                attributes.put(attr.getId(), attr.getValue());
            }
        }
    }

    @Override
    public PieceName getName() {
        return name;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    public MovementPattern getMovementPattern() {
        return movementPattern;
    }

    public boolean canFly() {
        return attributes.containsKey(PieceAttributeID.FLY);
    }

    public int getDistance() {
        return attributes.getOrDefault(PieceAttributeID.DISTANCE, 1);
    }

    public int getValue() {
        return attributes.getOrDefault(PieceAttributeID.VALUE, 1);
    }

    // New: UNBLOCK attribute – if present, piece ignores BLOCK obstacles.
    public boolean hasUnblock() {
        return attributes.containsKey(PieceAttributeID.UNBLOCK);
    }

    // New: JUMP attribute – if present, piece can jump over occupied squares.
    public boolean canJump() {
        return attributes.containsKey(PieceAttributeID.JUMP);
    }

    public void reduceValue(int reduction) {
        int newVal = getValue() - reduction;
        // Optionally, ensure the new value is not negative.
        if(newVal < 0) newVal = 0;
        attributes.put(PieceAttributeID.VALUE, newVal);
    }

}
