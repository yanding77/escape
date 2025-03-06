import escape.EscapeGameManager;
import escape.builder.EscapeGameBuilder;
import escape.*;
import escape.required.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertTrue;



public class Milestone4SampleTestsHEX2 {

    private EscapeGameManager escapeGameManager2 = null;


    @AfterAll
    static void testBreakdown() {
    }

    @BeforeEach
    void setup() throws Exception {
        escapeGameManager2 = new EscapeGameBuilder("./configurations/milestone4SampleHEX2.egc").makeGameManager();

    }

    public Milestone4SampleTestsHEX2() {
    }

    /* GAME END RULE tests; using finite board; based on Milestone4SampleTestsHEX2.ecg */
    @Test
    void testPlayerCantMove() {
        // EXIT is at (1,1)
        // First move Dog (playerA) from (4,5) to (2,7)
        GameStatus status = escapeGameManager2.move(new CoordinateImpl(4, 5), new CoordinateImpl(2, 7));
        assertTrue(status.isValidMove());
        // Next move Bird (playerB) from (1,4) to (1,1) ; PlayerA score:0,  Player B score :4
        status = escapeGameManager2.move(new CoordinateImpl(1, 4), new CoordinateImpl(1, 1));
        assertTrue(status.isValidMove());
        // Next move Snail (playerA) from (5,1) to (1,1) ; PlayerA score:1,  Player B score :4
        status = escapeGameManager2.move(new CoordinateImpl(5, 1), new CoordinateImpl(1, 1));
        assertTrue(status.isValidMove());
        // current player (playerB) can't make any moves. Since the board is finite, Horse can't move outside of the board.
        // PlayerB who has the higher score wins
        assertTrue(status.getMoveResult() != GameStatus.MoveResult.NONE);

    }
    @Test
    void testPlayerNoPieceEndGame() {
        // EXIT is at (0,0)
        // Firt move Dog (playerA) from (4,5) to (4,1)
        GameStatus status = escapeGameManager2.move(new CoordinateImpl(4, 5), new CoordinateImpl(4, 1));
        assertTrue(status.isValidMove());
        // Next move Bird (playerB) from (1,4) to (1,1) -> EXIT; PlayerA score:0,  Player B score :4
        status = escapeGameManager2.move(new CoordinateImpl(1, 4), new CoordinateImpl(1, 1));
        assertTrue(status.isValidMove());
        // Next move Dog (playerA) from (4,1) to (1,1) -> EXIT ; PlayerA score:1,  Player B score :4
        status = escapeGameManager2.move(new CoordinateImpl(4, 1), new CoordinateImpl(1, 1));
        assertTrue(status.isValidMove());

        //Next move Horse (playerB) from (1,8) to (2,7) ; PlayerA score:1,  Player B score :4
        status = escapeGameManager2.move(new CoordinateImpl(1, 8), new CoordinateImpl(2, 7));
        assertTrue(status.isValidMove());

        // Next move Snail (playerA) from (5,1) to (1,1) -> EXIT ; PlayerA score:2,  Player B score :4
        status = escapeGameManager2.move(new CoordinateImpl(5, 1), new CoordinateImpl(1, 1));
        assertTrue(status.isValidMove());

        // ENABLE THE FOLLOWING LINES IF YOU EXPECT THAT OPPONENT MAKES THEIR MOVE BEFORE YOU END THE GAME. WE WILL ACCEPT BOTH VERSIONS.
        //Next move Horse (playerB) from (2,7) to (2,1) ; PlayerA score:1,  Player B score :4
        //status = escapeGameManager2.move(new CoordinateImpl(2, 7), new CoordinateImpl(2, 1));
         //assertTrue(status.isValidMove());
        // It is now playerA's turn, but PlayerA has no pieces left on board. PlayerB higher score, so playerA loses.

        assertTrue(status.getMoveResult() != GameStatus.MoveResult.NONE);
    }

    /* -----  GAME END TESTS with OBSERVER ------*/

    @Test
    void testPlayerCantMoveObserver() {
        // Create a test observer and register it with the manager
        MasterTestObserver observer = new MasterTestObserver();
        escapeGameManager2.addObserver(observer);
        // EXIT is at (1,1)
        // First move Dog (playerA) from (4,5) to (2,7)
        GameStatus status = escapeGameManager2.move(new CoordinateImpl(4, 5), new CoordinateImpl(2, 7));
        assertTrue(status.isValidMove());
        // Next move Bird (playerB) from (1,4) to (1,1) ; PlayerA score:0,  Player B score :4
        status = escapeGameManager2.move(new CoordinateImpl(1, 4), new CoordinateImpl(1, 1));
        assertTrue(status.isValidMove());
        // Next move Snail (playerA) from (5,1) to (1,1) ; PlayerA score:1,  Player B score :4
        status = escapeGameManager2.move(new CoordinateImpl(5, 1), new CoordinateImpl(1, 1));
        assertTrue(status.isValidMove());
        // current player (playerB) can't make any moves. Since the board is finite, Horse can't move outside the board.
        // PlayerB who has the higher score wins
        assertTrue(status.getMoveResult() != GameStatus.MoveResult.NONE);

        assertTrue(observer.messageCount() > 0);
        printMessages(observer, 1);
        // Sample output: 1 - MESSAGE from observer  : playerB has no valid moves left. playerB has higher score and wins with score 4.
    }

    @Test
    void testPlayerNoPieceEndGameObserver() {
        // Create a test observer and register it with the manager
        MasterTestObserver observer = new MasterTestObserver();
        escapeGameManager2.addObserver(observer);
        // EXIT is at (0,0)
        // Firt move Dog (playerA) from (4,5) to (4,1)
        GameStatus status = escapeGameManager2.move(new CoordinateImpl(4, 5), new CoordinateImpl(4, 1));
        assertTrue(status.isValidMove());
        // Next move Bird (playerB) from (1,4) to (1,1) -> EXIT; PlayerA score:0,  Player B score :4
        status = escapeGameManager2.move(new CoordinateImpl(1, 4), new CoordinateImpl(1, 1));
        assertTrue(status.isValidMove());
        // Next move Dog (playerA) from (4,1) to (1,1) -> EXIT ; PlayerA score:1,  Player B score :4
        status = escapeGameManager2.move(new CoordinateImpl(4, 1), new CoordinateImpl(1, 1));
        assertTrue(status.isValidMove());

        //Next move Horse (playerB) from (1,8) to (2,7) ; PlayerA score:1,  Player B score :4
        status = escapeGameManager2.move(new CoordinateImpl(1, 8), new CoordinateImpl(2, 7));
        assertTrue(status.isValidMove());

        // Next move Snail (playerA) from (5,1) to (1,1) -> EXIT ; PlayerA score:2,  Player B score :4
        status = escapeGameManager2.move(new CoordinateImpl(5, 1), new CoordinateImpl(1, 1));
        assertTrue(status.isValidMove());

        // ENABLE THE FOLLOWING LINES IF YOU EXPECT THAT OPPONENT MAKES THEIR MOVE BEFORE YOU END THE GAME. WE WILL ACCEPT BOTH VERSIONS.
        //Next move Horse (playerB) from (2,7) to (2,1) ; PlayerA score:1,  Player B score :4
        //status = escapeGameManager2.move(new CoordinateImpl(2, 7), new CoordinateImpl(2, 1));
        //assertTrue(status.isValidMove());
        // It is now playerA's turn, but PlayerA has no pieces left on board. PlayerB higher score, so playerA loses (i.e, playerB wins)

        assertTrue(status.getMoveResult() != GameStatus.MoveResult.NONE);
        assertTrue(observer.messageCount() > 0);
        printMessages(observer, 2);
        // Sample output: 2 - MESSAGE from observer  : playerA removed all their pieces. playerB has higher score and wins with score 4.
    }

    private void printMessages(MasterTestObserver observer, int n) {
        while (observer.messageCount() > 0) {
            System.err.println(n + " - MESSAGE from observer "
                    + " : " + observer.nextMessage().getMessage());
        }
    }


    // Test that a piece with JUMP cannot jump over more than one consecutive occupied square.
    @Test
    void testInvalidJumpOverMultiplePieces() {
        // Assume FROG has JUMP (and no FLY), and in this scenario there are two consecutive pieces in its path.
        // For instance, Frog from (4,4) tries to jump to (7,7), but two squares in between are occupied.
        Coordinate from = new CoordinateImpl(4, 4);
        Coordinate to = new CoordinateImpl(7, 7);
        GameStatus status = escapeGameManager2.move(from, to);
        // The move should be invalid because Frog cannot jump over more than one consecutive piece.
        assertFalse(status.isValidMove(), "Frog should not be allowed to jump over more than one consecutive piece.");
        assertEquals(from, status.finalLocation());
    }}

    // Test conflict draw: when attacker and defender have equal value, both exit.
