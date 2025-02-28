import escape.EscapeGameManager;
import escape.builder.EscapeGameBuilder;
import escape.*;
import escape.required.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class Milestone3SampleTestsHEX2 {

    private EscapeGameManager escapeGameManager2 = null;


    @AfterAll
    static void testBreakdown() {
    }

    @BeforeEach
    void setup() throws Exception {
        escapeGameManager2 = new EscapeGameBuilder("./configurations/milestone3SampleHEX2.egc").makeGameManager();

    }

    public Milestone3SampleTestsHEX2() {
    }

    /* GAME END RULE tests; based on Milestone3SampleTestsHEX2.ecg */
    @Test
    void testPlayerCantMakeMove() {
        // EXIT is at (0,0)
        // First move Dog (playerA) from (0,1) to (0,0)->EXIT ; Player A score :1
        GameStatus status = escapeGameManager2.move(new CoordinateImpl(0, 1), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Horse (playerB) from (-1,-1) to (0,-1) ; Player B score :0
        status = escapeGameManager2.move(new CoordinateImpl(-1, -1), new CoordinateImpl(0, -1));
        assertTrue(status.isValidMove());
        // current player (playerA) can't make any moves. PlayerA who has the higher score wins
        assertTrue(status.getMoveResult() == GameStatus.MoveResult.WIN);

    }
    @Test
    void testPlayerNoPieceEndGame2() {
        // EXIT is at (0,0)
        // Next move Dog (playerA) from (0,1) to (0,0)->EXIT ; Player A score :1
        GameStatus status = escapeGameManager2.move(new CoordinateImpl(0, 1), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Dog (playerB) from (0,2) to (0,0)->EXIT ; Player B score :1
        status = escapeGameManager2.move(new CoordinateImpl(0, 2), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move FROG (playerA) from (0,3) to (0,0)->EXIT ; Player A score :2
        status = escapeGameManager2.move(new CoordinateImpl(0, 3), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move SNAIL (playerB) from (1,2) to (1,0) ; Player A score :2
        status = escapeGameManager2.move(new CoordinateImpl(1, 2), new CoordinateImpl(1, 1));
        assertTrue(status.isValidMove());
        // It is now playerA's turn, but PlayerA has no pieces left on board and has higher score, so they win.
        assertTrue(status.getMoveResult() == GameStatus.MoveResult.WIN);
    }

}
