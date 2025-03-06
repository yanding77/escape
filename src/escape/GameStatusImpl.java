package escape;

import escape.required.Coordinate;
import escape.required.GameStatus;

public class GameStatusImpl implements GameStatus {
    private final boolean validMove;
    private final MoveResult moveResult;
    private final Coordinate finalLocation;
    private final boolean moreInfo;


    public GameStatusImpl(boolean validMove, MoveResult moveResult, Coordinate finalLocation, boolean moreInfo) {
        this.validMove = validMove;
        this.moveResult = moveResult;
        this.finalLocation = finalLocation;
        this.moreInfo = moreInfo;

    }

    @Override
    public boolean isValidMove() {
        return validMove;
    }

    @Override
    public boolean isMoreInformation() {
        // In Milestone4, if observers are notified, you may return true.
        return moreInfo;
    }

    @Override
    public MoveResult getMoveResult() {
        return moveResult;
    }

    @Override
    public Coordinate finalLocation() {
        return finalLocation;
    }
}
