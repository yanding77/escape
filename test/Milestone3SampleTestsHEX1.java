import escape.EscapeGameManager;
import escape.builder.EscapeGameBuilder;
import escape.*;
import escape.required.Coordinate;
import escape.required.EscapePiece;
import escape.required.EscapePiece.*;
import escape.required.GameStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class Milestone3SampleTestsHEX1 {

    private EscapeGameManager escapeGameManager1 = null;

    @AfterAll
    static void testBreakdown() {
    }

    @BeforeEach
    void setup() throws Exception {
        escapeGameManager1 = new EscapeGameBuilder("./configurations/milestone3SampleHEX1.egc").makeGameManager();
    }

    public Milestone3SampleTestsHEX1() {
    }

    @Test
    void hexBoardSetup() {
        // HEX Board is initialized correctly - indices can be positive and negative
        EscapePiece piece = escapeGameManager1.getPieceAt(new CoordinateImpl(-2,-2));
        assertNotNull(piece);
        assertTrue(piece.getPlayer().equals("playerA"));
        assertEquals(piece.getName(), PieceName.FROG);

        piece = escapeGameManager1.getPieceAt(new CoordinateImpl(0,3));
        assertNotNull(piece);
        assertTrue(piece.getPlayer().equals("playerB"));
        assertEquals(piece.getName(), PieceName.DOG);
    }

    @Test
    void linearValidDistance() {
        // LINEAR move - valid distance on HEX board positive index
        // move DOG from (1, 1) to (1, 0)
        Coordinate from  = new CoordinateImpl(0,1);
        Coordinate to  = new CoordinateImpl(1,1);
        GameStatus status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        assertEquals(to, status.finalLocation());
    }

    @Test
    void linearValidNegativeIndex() {
        // LINEAR move - to negative index, valid distance.
        // move DOG from (1, 1) to (1, -3)
        Coordinate from = new CoordinateImpl(0,1);
        Coordinate to = new CoordinateImpl(-2, 3);
        GameStatus status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        assertEquals(to, status.finalLocation());
    }

    @Test
    void linearInvalidDistance() {
        // LINEAR move, invalid distance.
        // move BIRD from (2, -2) to (2, 3): Invalid distance, BIRD can only move 4
        Coordinate from = new CoordinateImpl(2, -2);
        Coordinate to = new CoordinateImpl(2, 3);
        GameStatus status = escapeGameManager1.move(from, to);
        assertFalse(status.isValidMove());
        assertEquals(from, status.finalLocation());
    }

    @Test
    void emptyLocation() {
        // move from empty location ; there is no piece to move.
        Coordinate from = new CoordinateImpl(-2, 3);
        Coordinate to = new CoordinateImpl(-2, 2);
        GameStatus status = escapeGameManager1.move(from, to);
        assertFalse(status.isValidMove());
    }

    @Test
    void locationOccupied() {
        // move PlayerA FROG from (-2, -2) to (-1, 1); location is occupied by PlayerB DOG
        Coordinate from = new CoordinateImpl(-2, -2);
        Coordinate to = new CoordinateImpl(-1, -1);
        GameStatus status = escapeGameManager1.move(from, to);
        assertFalse(status.isValidMove());
        assertEquals(from, status.finalLocation());
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
        // move PlayerA FROG from (-2, -2) to (0, -2), VALID move
        Coordinate from = new CoordinateImpl(-2, -2);
        Coordinate to = new CoordinateImpl(0, -2);
        GameStatus status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        assertEquals(to, status.finalLocation());

        // move PlayerB DOG from (0, 3) to (-2, 3), (-1, 3) is BLOCK
        from = new CoordinateImpl(0, 3);
        to = new CoordinateImpl(-2, 3);
        status = escapeGameManager1.move(from, to);
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



    /* GAME END RULE tests; based on Milestone3SampleTestsHEX1.ecg */

    @Test
    void testSCOREGameRule() {
        // EXIT is at (0,0)
        // First move Dog (playerA) from (0,1) to (0,0)->EXIT ; Player A score :1
        GameStatus status = escapeGameManager1.move(new CoordinateImpl(0, 1), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Dog (playerB) from (0,3) to (0,0)->EXIT ; Player B score :1
        status = escapeGameManager1.move(new CoordinateImpl(0, 3), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Bird (playerA) from (2,-2) to (0,0)->EXIT ; Player A score :2
        status = escapeGameManager1.move(new CoordinateImpl(2, -2), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // Next move Horse (playerB) from (-1,-1) to (0,-1) ; Player B score :1
        status = escapeGameManager1.move(new CoordinateImpl(-1, -1), new CoordinateImpl(0, -1));
        assertTrue(status.isValidMove());
        // Next move Frog (playerA) from (-2,-2) to (0,-2) ; Player A score :2
        status = escapeGameManager1.move(new CoordinateImpl(-2, -2), new CoordinateImpl(0, -2));
        assertTrue(status.isValidMove());
        // Next move Horse (playerB) from (0,-1) to (0,0)->EXIT ; Player B score :3 ; PlayerB should win
        status = escapeGameManager1.move(new CoordinateImpl(0, -1), new CoordinateImpl(0, 0));
        assertTrue(status.isValidMove());
        // current player (playerB) wins
        assertTrue(status.getMoveResult() == GameStatus.MoveResult.WIN);

    }

    @Test
    void testMoveFromOutOfBounds() {
        // Attempt to move from a coordinate that is out of bounds.
        // For a finite board (or even an infinite board with positive indices required),
        // coordinates with non-positive indices may be considered out of bounds.
        Coordinate from = new CoordinateImpl(0, 5); // x=0 is out-of-bound if indices must be >=1
        Coordinate to = new CoordinateImpl(1, 1);
        GameStatus status = escapeGameManager1.move(from, to);
        assertFalse(status.isValidMove());
    }

    @Test
    void testMoveToOutOfBounds() {
        // Attempt to move to a coordinate that is out-of-bounds.
        Coordinate from = new CoordinateImpl(2, -2); // valid if on HEX (may be negative on infinite HEX)
        // But if our board is defined such that some coordinates are invalid,
        // choose one that is invalid. For instance, if x must be >=1:
        Coordinate to = new CoordinateImpl(0, -2); // x=0 is out-of-bound
        GameStatus status = escapeGameManager1.move(from, to);
        assertFalse(status.isValidMove());
    }

    @Test
    void testMoveWithWrongPlayerPiece() {
        // Attempt to move a piece that does not belong to the current player.
        // For example, if the current player is playerA, try to move a piece owned by playerB.
        // Assume there's a piece for playerB at (0,3) (e.g., DOG).
        Coordinate from = new CoordinateImpl(0, 3);
        Coordinate to = new CoordinateImpl(0, 2);
        // Since currentPlayer is playerA at the start, this move should be invalid.
        GameStatus status = escapeGameManager1.move(from, to);
        assertFalse(status.isValidMove());
    }

    @Test
    void testConflictLostRemovesAttacker() {
        // Test that when a move results in conflict where the attacker loses,
        // the attacker is removed (finalLocation is null) and the move is valid.
        // For this, assume that a piece (say DOG with value 1) from playerA moves onto
        // a square occupied by a piece (say SNAIL with value 3) from playerB.
        // This should result in the attacker's removal and reduction of the occupant’s value.
        // (Adjust coordinates as per your configuration.)
        Coordinate from = new CoordinateImpl(0, 1);  // Dog (playerA, value 1)
        Coordinate to = new CoordinateImpl(1, 1);      // Assume this location is occupied by SNAIL (playerB, value 3)
        GameStatus status = escapeGameManager1.move(from, to);
        // Expect the move to be valid and final location null.
        assertTrue(status.isValidMove());
        assertNull(status.finalLocation());
        // Optionally, you can check that the occupant’s value is reduced.
    }

    @Test
    void testFlyMovementIgnoresObstacles() {
        // Test that a piece with the FLY attribute can move over occupied squares.
        // For example, move a BIRD (with fly) from one coordinate to another,
        // where intermediate squares are occupied.
        // (Coordinates should be chosen such that the move is linear and passes over occupied cells.)
        Coordinate from = new CoordinateImpl(2, -2);
        Coordinate to = new CoordinateImpl(2, 2);
        GameStatus status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());
        assertEquals(to, status.finalLocation());
    }

    @Test
    void testIllegalMovementPattern() {
        // Test that a move that doesn't follow the piece's movement pattern is invalid.
        // For example, if a piece with ORTHOGONAL movement tries to move diagonally.
        Coordinate from = new CoordinateImpl(2, -2);
        // Attempt a diagonal move
        Coordinate to = new CoordinateImpl(3, -1);
        GameStatus status = escapeGameManager1.move(from, to);
        assertFalse(status.isValidMove());
        assertEquals(from, status.finalLocation());
    }

    @Test
    void testNoLegalMovesForPlayer() {

        Coordinate from = new CoordinateImpl(0, 1);
        Coordinate to = new CoordinateImpl(0, 0);  // EXIT location
        GameStatus status = escapeGameManager1.move(from, to);
        assertTrue(status.isValidMove());

        assertTrue(status.getMoveResult() != GameStatus.MoveResult.NONE);
    }

}
