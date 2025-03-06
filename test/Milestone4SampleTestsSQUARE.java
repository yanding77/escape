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


public class Milestone4SampleTestsSQUARE {

    private EscapeGameManager escapeGameManager = null;

    @AfterAll
    static void testBreakdown() {
    }

    @BeforeEach
    void setup() throws Exception {
        escapeGameManager = new EscapeGameBuilder("/configurations/milestone4SampleSQUARE.egc").makeGameManager();
    }

    public Milestone4SampleTestsSQUARE() {
    }


    @Test
    void testlinearMoveValid1() {
        // Checks for valid LINEAR move; we move FROG which has linear movement pattern.
        Coordinate from  = new CoordinateImpl(4,4);
        Coordinate to  = new CoordinateImpl(8,4); // orthogonal
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), to);
    }

    @Test
    void testlinearMoveValid2() {
        // Checks for valid LINEAR move; we move FROG which has linear movement pattern.
        Coordinate from  = new CoordinateImpl(4,4);
        Coordinate to  = new CoordinateImpl(1,1); // diagonal
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), to);
    }

    @Test
    void testlinearMoveJumpValid() {
        // Checks for valid LINEAR move with JUMP;  FROG jumps over SNAIL.
        Coordinate from  = new CoordinateImpl(4,4);
        Coordinate to  = new CoordinateImpl(6,6);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), to);
    }

    @Test
    void testLinearMoveJumpInValid() {
        // Checks for invalid LINEAR move with JUMP;  FROG can't jump over more than one consecutive pieces.
        //First move BIRD (playerA) from (4,4) to (6,6); BIRD can fly so it can go over PlayerB BIRD.
        Coordinate from  = new CoordinateImpl(4,8);
        Coordinate to  = new CoordinateImpl(6,6);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());

        //Next move BIRD (playerB) from (5,7) to (7,5); BIRD can fly so it can go over PlayerA BIRD.
        from  = new CoordinateImpl(5,7);
        to  = new CoordinateImpl(7,5);
        gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());

        // FROG(playerA) attempts to jump over SNAIL and BIRD. Can't jump over 2 or more consecutive pieces; invalid move.
        from  = new CoordinateImpl(4,4);
        to  = new CoordinateImpl(7,7);
        gameStatus = escapeGameManager.move(from, to);
        assertFalse(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), from);
    }

    @Test
    void testLinearMoveOntoBlockInValid() {
        // FROG moves onto BLOCK coordinate; invalid move.
        Coordinate from  = new CoordinateImpl(4,4);
        Coordinate to  = new CoordinateImpl(4,5);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertFalse(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), from);
    }

    @Test
    void testLinearMoveUnblock() {
        // Checks for invalid LINEAR move with UNBLOCK;  DOG can move over  BLOCK coordinates.
        //First move a piece of playerA (BIRD); BIRD can fly so it can go over BLOCK
        Coordinate from  = new CoordinateImpl(4,8);
        Coordinate to  = new CoordinateImpl(2,6);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), to);

        //Next move DOG; DOG can go over BLOCK
        from  = new CoordinateImpl(3,2);
        to  = new CoordinateImpl(3,8);
        gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), to);
    }

    @Test
    void testPointConflict() {
        // FROG moves onto SNAIL; POINT_CONFLICT
        Coordinate from  = new CoordinateImpl(4,4);
        Coordinate to  = new CoordinateImpl(5,5);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        // Frog's value should now be 1

        //Next move DOG from (3,2) to (5,4)
        from  = new CoordinateImpl(3,2);
        to  = new CoordinateImpl(5,4);
        gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), to);

        //Move FROG onto DOG
        from  = new CoordinateImpl(5,5);
        to  = new CoordinateImpl(5,4);
        gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        // both FROG and DOG should exit game.

        from  = new CoordinateImpl(5,4);
        to  = new CoordinateImpl(5,6);
        gameStatus = escapeGameManager.move(from, to);
        assertFalse(gameStatus.isValidMove());
        //There is no piece at (5,4) ; invalid move
    }

}
