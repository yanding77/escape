import escape.EscapeGameManager;
import escape.builder.EscapeGameBuilder;
import escape.coordinate.CoordinateImpl;
import escape.required.Coordinate;
import escape.required.EscapePiece;
import escape.required.EscapePiece.PieceName;
import escape.required.GameStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class Milestone4SampleTestsHEX1 {

    private EscapeGameManager escapeGameManager1 = null;

    @AfterAll
    static void testBreakdown() {
    }

    @BeforeEach
    void setup() throws Exception {
        escapeGameManager1 = new EscapeGameBuilder("/configurations/milestone4SampleHEX1.egc").makeGameManager();
    }

    public Milestone4SampleTestsHEX1() {
    }


    @Test
    void moveOntoBlockedLocation() {
        // move PlayerA BIRD from (2, -2) to (2, -3), location is BLOCK
        Coordinate from = new CoordinateImpl(2, -2);
        Coordinate to = new CoordinateImpl(2, -3);
        GameStatus status = escapeGameManager1.move(from, to);
        assertFalse(status.isValidMove());
        assertEquals(from, status.finalLocation());

        // move PlayerA BIRD from (2, -2) to (2, -1): VALID
        to = new CoordinateImpl(2, -1);
        status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        assertEquals(to, status.finalLocation());

        // move PlayerB DOG from (0, 3) to (-1, 3), location is BLOCK
        from = new CoordinateImpl(0, 3);
        to = new CoordinateImpl(-1, 3);
        status = escapeGameManager1.move(from, to);
        assertFalse(status.isValidMove());
        assertEquals(from, status.finalLocation());
    }

    @Test
    void moveOverBlockedLocationValidMove() {
        // move PlayerA BIRD from (2, -2) to (2, -4), BIRD can fly over BLOCK
        Coordinate from = new CoordinateImpl(2, -2);
        Coordinate to = new CoordinateImpl(2, -4);
        GameStatus status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        assertEquals(to, status.finalLocation());
    }
    @Test
    void moveOverBlockedLocationInValidMove() {
        // move PlayerB DOG from (0, 3) to (-2, 3), (-1, 3) is BLOCK
        Coordinate from = new CoordinateImpl(0, 3);
        Coordinate to = new CoordinateImpl(-2, 3);
        GameStatus status = escapeGameManager1.move(from, to);
        assertFalse(status.isValidMove());
        assertEquals(from, status.finalLocation());
    }

    @Test
    void testMoveOntoExitLocationExitsPiece() {
        // EXIT is at (0,0)
        Coordinate from = new CoordinateImpl(0, 1);
        Coordinate to   = new CoordinateImpl(0, 0);
        GameStatus status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        // the following assertion is optional
        assertNull(status.finalLocation());
    }

    @Test
    void testMoveOverExitLocationExitsPiece() {
        // Move onto an EXIT location -> piece exits the game.
        // Move DOG *Over* Exit Location -> DOG Should Exit Game
        Coordinate from = new CoordinateImpl(0, 1);
        Coordinate to   = new CoordinateImpl(0, -1);
        GameStatus status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        assertNull(status.finalLocation());
    }

    /*Testing JUMP piece attribute*/
    @Test
    void testJumpValidMove() {
        // move Frog (playerA) from (-2,-2) to (-1,-2)
        Coordinate from = new CoordinateImpl(-2, -2);
        Coordinate to   = new CoordinateImpl(-1, -2);
        GameStatus status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        // move Snail (playerB) from (-3,1) to (-1,1)
        from = new CoordinateImpl(-3, 1);
        to   = new CoordinateImpl(-1, 1);
        status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        // move Frog (playerA) from (-1,-2) to (-1,0) ; JUMPS over Horse ; valid move
        from = new CoordinateImpl(-1, -2);
        to   = new CoordinateImpl(-1, 0);
        status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        assertEquals(status.finalLocation(),to);
    }
    @Test
    void testJumpValidMove2() {
        // move Frog (playerA) from (-2,-2) to (-1,-2)
        Coordinate from = new CoordinateImpl(-2, -2);
        Coordinate to   = new CoordinateImpl(-1, -2);
        GameStatus status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        // move Snail (playerB) from (-3,1) to (-1,1)
        from = new CoordinateImpl(-3, 1);
        to   = new CoordinateImpl(-1, 1);
        status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        // move Frog (playerA) from (-1,-2) to (-1,2) ; tries to JUMP over Horse ; can't jump over more than one coordinates; invalid move
        from = new CoordinateImpl(-1, -2);
        to   = new CoordinateImpl(-1, 2);
        status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        assertEquals(status.finalLocation(),to);
    }

    @Test
    void testMoveOverBlockedLocationwithUnblockPiece() {
        // move PlayerA BIRD from (2, -2) to (2, -4), BIRD can fly over BLOCK
        Coordinate from = new CoordinateImpl(2, -2);
        Coordinate to = new CoordinateImpl(2, -4);
        GameStatus status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        // move PlayerB DOG from (0, 3) to (-1, 3), DOG has UNBLOCK attribute; valid move
        from = new CoordinateImpl(0, 3);
        to = new CoordinateImpl(-2, 3);
        status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        assertEquals(to, status.finalLocation());
    }

    /*Testing POINT_CONFLICT rule*/
    @Test
    void testPOINTCONFLICTDraw() {
        // move Dog (playerA) from (0,1) to (0,3) ; (0,3) already have PlayerB's Dog ; POINT_CONFLICT
        Coordinate from = new CoordinateImpl(0, 1); // Dog has value 1.
        Coordinate to   = new CoordinateImpl(0, 3); //Dog has value 1
        GameStatus status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        // Since both pieces have equal value, they both should exit board. The values of exiting pieces are not added to player scores.
        assertNull(status.finalLocation());
    }
    @Test
    void testPOINTCONFLICTAttackerWins() {
        // move Horse (playerA) from (3,0) to (0,3) ; (0,3) already have PlayerB's Dog ; POINT_CONFLICT
        Coordinate from = new CoordinateImpl(3, 0); // Horse has value 2
        Coordinate to   = new CoordinateImpl(0, 3); // Dog has value 1
        GameStatus status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        // Dog exits game; Horse's value is updated as 2-1=1. The value of the exiting piece is not added to playerB score.
        //assertEquals(status.finalLocation(), to);
    }


    /* GAME END RULE tests; based on Milestone3SampleTestsHEX1.ecg */

    @Test
    void testSCOREGameRule() {
        // EXIT is at (0,0)
        // First move Dog (playerA) from (0,1) to (0,0)->EXIT ; Player A score :1, Player B score :0
        GameStatus status = escapeGameManager1.move(new CoordinateImpl(0, 1), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Dog (playerB) from (0,3) to (0,0)->EXIT ; Player A score :1; Player B score :1
        status = escapeGameManager1.move(new CoordinateImpl(0, 3), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Horse (playerA) from (3,0) to (0,0)->EXIT ; Player A score :3, Player B score :1
        status = escapeGameManager1.move(new CoordinateImpl(3, 0), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // SCORE is 3; current player (playerA) wins
        assertTrue(status.getMoveResult() == GameStatus.MoveResult.WIN);
        assertTrue(status.isMoreInformation());
    }

    @Test
    void testTURNLIMITGameRuleDRAW() {
        // EXIT is at (0,0)
        // Create a test observer and register it with the manager
        // First move Frog (playerA) from (-2,-2) to (-2,0)  ; Player A score :0 ;  Player B score :0
        GameStatus status = escapeGameManager1.move(new CoordinateImpl(-2, -2), new CoordinateImpl(-2, 0));
        assertTrue(status.isValidMove());
        // Next move Horse (playerB) from (-1,-1) to (-1,0); Player A score :0 ;  Player B score :0
        status = escapeGameManager1.move(new CoordinateImpl(-1, -1), new CoordinateImpl(-1, 0));
        assertTrue(status.isValidMove());
        // Next move Dog (playerA) from (0,1) to (0,0)->EXIT ; Player A score :1; Player B score :0
        status = escapeGameManager1.move(new CoordinateImpl(0, 1), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Dog (playerB) from (0,3) to (0,0) ; Player A score :1; Player B score :1
        status = escapeGameManager1.move(new CoordinateImpl(0, 3), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // TURN_LIMIT (4) is reached
        // both players have the same score, there is a DRAW
        assertTrue(status.getMoveResult() == GameStatus.MoveResult.DRAW);
        assertTrue(status.isMoreInformation());
    }

    @Test
    void testTURNLIMITGameRuleWIN() {
        // EXIT is at (0,0)
        // Create a test observer and register it with the manager
        // First move Dog (playerA) from (0,1) to (0,0)->EXIT ; Player A score :1; Player B score :0
        GameStatus status = escapeGameManager1.move(new CoordinateImpl(0, 1), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Horse (playerB) from (-1,-1) to (-1,0); Player A score :1 ;  Player B score :0
        status = escapeGameManager1.move(new CoordinateImpl(-1, -1), new CoordinateImpl(-1, 0));
        assertTrue(status.isValidMove());
        // Next move Bird (playerA) from (2,-2) to (0,0)  ; Player A score :2 ;  Player B score :0
        status = escapeGameManager1.move(new CoordinateImpl(2, -2), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Dog (playerB) from (0,3) to (0,0) ; Player A score :2; Player B score :1
        status = escapeGameManager1.move(new CoordinateImpl(0, 3), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // TURN_LIMIT (4) is reached
        // PlayerA higher score, so playerB(current player)  LOSE
        assertTrue(status.getMoveResult() == GameStatus.MoveResult.LOSE);
        assertTrue(status.isMoreInformation());
    }

    @Test
    void testTURNLIMITGameRulewithPOINTCONFLICT() {
        // EXIT is at (0,0)
        // First move Dog (playerA) from (0,1) to (-3,1) ; - POINT_CONFLICT ; Dog leaves game; Snail's value drops to 2; Player A score :0, Player B score :0
        GameStatus status = escapeGameManager1.move(new CoordinateImpl(0, 1), new CoordinateImpl(-3, 1));
        assertTrue(status.isValidMove());
        // Next move Snail (playerB) from (-3,1) to (0,1);  Player A score :0; Player B score :0
        status = escapeGameManager1.move(new CoordinateImpl(-3, 1), new CoordinateImpl(0, 1));
        assertTrue(status.isValidMove());
        // Next move Bird (playerA) from (2,-2) to (0,0) -> EXIT ; Player A score :1, Player B score :0
        status = escapeGameManager1.move(new CoordinateImpl(2, -2), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Snail (playerB) from (0,1) to (0,0);  Player A score :0; Player B score :2
        status = escapeGameManager1.move(new CoordinateImpl(0, 1), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // TURN_LIMIT (4) reached; current player (playerB) wins
        assertTrue(status.getMoveResult() == GameStatus.MoveResult.WIN);
        assertTrue(status.isMoreInformation());
    }

    /* -----  GAME END TESTS repeater with OBSERVER ------*/
    @Test
    void testSCOREGameRuleObserver() {
        // EXIT is at (0,0)
        MasterTestObserver observer = new MasterTestObserver();
        escapeGameManager1.addObserver(observer);
        // First move Dog (playerA) from (0,1) to (0,0)->EXIT ; Player A score :1, Player B score :0
        GameStatus status = escapeGameManager1.move(new CoordinateImpl(0, 1), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Dog (playerB) from (0,3) to (0,0)->EXIT ; Player A score :1; Player B score :1
        status = escapeGameManager1.move(new CoordinateImpl(0, 3), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Horse (playerA) from (3,0) to (0,0)->EXIT ; Player A score :3, Player B score :1
        status = escapeGameManager1.move(new CoordinateImpl(3, 0), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // SCORE is 3; current player (playerA) wins
        assertTrue(status.getMoveResult() == GameStatus.MoveResult.WIN);
        assertTrue(status.isMoreInformation());
        assertTrue(observer.messageCount() > 0);
        printMessages(observer, 1);
        // Sample output : 1 - MESSAGE from observer  : playerA wins with a score of 3.
    }

    @Test
    void testTURNLIMITGameRuleDRAWObserver() {
        // EXIT is at (0,0)
        // Create a test observer and register it with the manager
        MasterTestObserver observer = new MasterTestObserver();
        escapeGameManager1.addObserver(observer);
        // First move Frog (playerA) from (-2,-2) to (-2,0)  ; Player A score :0 ;  Player B score :0
        GameStatus status = escapeGameManager1.move(new CoordinateImpl(-2, -2), new CoordinateImpl(-2, 0));
        assertTrue(status.isValidMove());
        // Next move Horse (playerB) from (-1,-1) to (-1,0); Player A score :0 ;  Player B score :0
        status = escapeGameManager1.move(new CoordinateImpl(-1, -1), new CoordinateImpl(-1, 0));
        assertTrue(status.isValidMove());
        // Next move Dog (playerA) from (0,1) to (0,0)->EXIT ; Player A score :1; Player B score :0
        status = escapeGameManager1.move(new CoordinateImpl(0, 1), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Dog (playerB) from (0,3) to (0,0) ; Player A score :1; Player B score :1
        status = escapeGameManager1.move(new CoordinateImpl(0, 3), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // TURN_LIMIT (4) is reached
        // both players have the same score, there is a DRAW
        assertTrue(status.getMoveResult() == GameStatus.MoveResult.DRAW);
        assertTrue(status.isMoreInformation());
        assertTrue(observer.messageCount() > 0);
        printMessages(observer, 2);
        // Sample output: 2 - MESSAGE from observer  : Turn limit reached. Game ends in a draw. Both players have same score.
    }

    @Test
    void testTURNLIMITGameRuleWINObserver() {
        // EXIT is at (0,0)
        // Create a test observer and register it with the manager
        MasterTestObserver observer = new MasterTestObserver();
        escapeGameManager1.addObserver(observer);
        // First move Dog (playerA) from (0,1) to (0,0)->EXIT ; Player A score :1; Player B score :0
        GameStatus status = escapeGameManager1.move(new CoordinateImpl(0, 1), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Horse (playerB) from (-1,-1) to (-1,0); Player A score :1 ;  Player B score :0
        status = escapeGameManager1.move(new CoordinateImpl(-1, -1), new CoordinateImpl(-1, 0));
        assertTrue(status.isValidMove());
        // Next move Bird (playerA) from (2,-2) to (0,0)  ; Player A score :2 ;  Player B score :0
        status = escapeGameManager1.move(new CoordinateImpl(2, -2), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Dog (playerB) from (0,3) to (0,0) ; Player A score :2; Player B score :1
        status = escapeGameManager1.move(new CoordinateImpl(0, 3), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // TURN_LIMIT (4) is reached
        // PlayerA higher score, so playerB(current player)  LOSE
        assertTrue(status.getMoveResult() == GameStatus.MoveResult.LOSE);
        assertTrue(status.isMoreInformation());
        assertTrue(observer.messageCount() > 0);
        printMessages(observer, 3);
        // Sample output: 3 - MESSAGE from observer  : Turn limit reached.playerA has higher score and wins with score 2.
    }

    @Test
    void testTURNLIMITGameRulewithPOINTCONFLICTObserver() {
        // EXIT is at (0,0)
        // First move Dog (playerA) from (0,1) to (-3,1) ; - POINT_CONFLICT ; Dog leaves game; Snail's value drops to 2; Player A score :0, Player B score :0
        // Create a test observer and register it with the manager
        MasterTestObserver observer = new MasterTestObserver();
        escapeGameManager1.addObserver(observer);
        GameStatus status = escapeGameManager1.move(new CoordinateImpl(0, 1), new CoordinateImpl(-3, 1));
        assertTrue(status.isValidMove());
        // Next move Snail (playerB) from (-3,1) to (0,1);  Player A score :0; Player B score :0
        status = escapeGameManager1.move(new CoordinateImpl(-3, 1), new CoordinateImpl(0, 1));
        assertTrue(status.isValidMove());
        // Next move Bird (playerA) from (2,-2) to (0,0) -> EXIT ; Player A score :1, Player B score :0
        status = escapeGameManager1.move(new CoordinateImpl(2, -2), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Snail (playerB) from (0,1) to (0,0);  Player A score :0; Player B score :2
        status = escapeGameManager1.move(new CoordinateImpl(0, 1), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // TURN_LIMIT (4) reached; current player (playerB) has higher score and wins
        assertTrue(status.getMoveResult() == GameStatus.MoveResult.WIN);
        assertTrue(status.isMoreInformation());
        assertTrue(observer.messageCount() > 0);
        printMessages(observer, 4);
        // Sample output: 4 - MESSAGE from observer  : Turn limit reached.playerB has higher score and wins with score 2.
    }

    private void printMessages(MasterTestObserver observer, int n) {
        while (observer.messageCount() > 0) {
            System.err.println( n + " - MESSAGE from observer "
                    + " : " + observer.nextMessage().getMessage());
        }
    }
}
