package escape;

import escape.builder.PieceAttribute;
import escape.required.EscapePiece;
import escape.builder.PieceTypeDescriptor;

import java.util.HashMap;
import java.util.Map;

public class EscapePieceImpl implements EscapePiece
{
    private final PieceName name;
    private final String player;
    private final MovementPattern movementPattern;
    private final Map<PieceAttributeID, Integer> attributes = new HashMap<>();

    public EscapePieceImpl(String player, PieceTypeDescriptor descriptor)
    {
        this.name = descriptor.getPieceName();
        this.player = player;
        this.movementPattern = descriptor.getMovementPattern();

        // Copy attributes (FLY, DISTANCE, VALUE)
        if (descriptor.getAttributes() != null) {
            for (PieceAttribute attr : descriptor.getAttributes()) {
                attributes.put(attr.getId(), attr.getValue());
            }
        }
    }

    @Override
    public PieceName getName()
    {
        return name;
    }

    @Override
    public String getPlayer()
    {
        return player;
    }

    public MovementPattern getMovementPattern()
    {
        return movementPattern;
    }

    public boolean canFly()
    {
        return attributes.containsKey(PieceAttributeID.FLY);
    }

    public int getDistance()
    {
        return attributes.getOrDefault(PieceAttributeID.DISTANCE, 1);
    }

    public int getValue()
    {
        return attributes.getOrDefault(PieceAttributeID.VALUE, 1);
    }
}
