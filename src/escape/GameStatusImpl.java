package escape;

import escape.required.Coordinate;
import escape.required.GameStatus;

/**
 * Basic implementation of the GameStatus interface for milestone2.
 */
public class GameStatusImpl implements GameStatus
{
    private final boolean validMove;
    private final MoveResult moveResult;
    private final Coordinate finalLocation;

    /**
     * @param validMove   Whether the last move was valid
     * @param moveResult  For milestone2, always NONE
     * @param finalLocation The piece's final location
     */
    public GameStatusImpl(boolean validMove, MoveResult moveResult, Coordinate finalLocation)
    {
        this.validMove = validMove;
        this.moveResult = moveResult;
        this.finalLocation = finalLocation;
    }

    @Override
    public boolean isValidMove()
    {
        return validMove;
    }

    /**
     * Milestone2: Always false (no additional info).
     */
    @Override
    public boolean isMoreInformation()
    {
        return false;
    }

    @Override
    public MoveResult getMoveResult()
    {
        return moveResult;
    }

    @Override
    public Coordinate finalLocation()
    {
        return finalLocation;
    }
}
