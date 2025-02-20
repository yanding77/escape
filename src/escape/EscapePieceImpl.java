package escape;

import escape.builder.PieceAttribute;
import escape.required.EscapePiece;
import escape.builder.PieceTypeDescriptor;

import java.util.HashMap;
import java.util.Map;

/**
 * Concrete implementation of EscapePiece that stores
 * piece name, player, movement pattern, and any attributes.
 */
public class EscapePieceImpl implements EscapePiece
{
    private final PieceName name;
    private final String player;
    private final MovementPattern movementPattern;
    private final Map<PieceAttributeID, Integer> attributes = new HashMap<>();

    /**
     * Construct a piece from a PieceTypeDescriptor + player name.
     */
    public EscapePieceImpl(String player, PieceTypeDescriptor descriptor)
    {
        this.name = descriptor.getPieceName();
        this.player = player;
        this.movementPattern = descriptor.getMovementPattern();

        // Copy attributes (e.g., FLY => value=0 or 1, DISTANCE => value=2, etc.)
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

    /**
     * Return true if the piece has a FLY attribute.
     */
    public boolean canFly()
    {
        return attributes.containsKey(PieceAttributeID.FLY);
    }

    /**
     * Get the DISTANCE attribute's integer value.
     * If none, returns 0 (interpretation up to your rules).
     */
    public int getDistance()
    {
        return attributes.getOrDefault(PieceAttributeID.DISTANCE, 0);
    }
}
