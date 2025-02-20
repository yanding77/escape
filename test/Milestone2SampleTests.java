
import escape.*;
import escape.required.*;
import escape.builder.*;
import escape.builder.EscapeGameBuilder;
import escape.required.Coordinate;
import escape.required.GameStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class Milestone2SampleTests {

    private EscapeGameManager escapeGameManager = null;

    @AfterAll
    static void testBreakdown() {
    }

    @BeforeEach
    void setup() throws Exception {
        escapeGameManager = new EscapeGameBuilder("configurations/milestone2Sample.egc").makeGameManager();
    }

    public Milestone2SampleTests() {
    }

    @Test
    void placePieceOnBoard() {
        // Checks if piece is placed on board correctly.
        EscapePiece piece = escapeGameManager.getPieceAt(new CoordinateImpl(4,4));
        assertNotNull(piece);
    }

    @Test
    void getPlayer() {
        // Verifies the player associated with piece.;
        EscapePiece piece = escapeGameManager.getPieceAt(new CoordinateImpl(4,4));
        assertTrue(piece.getPlayer().equals("John"));
    }

    @Test
    void orthogonalMoveValid() {
        // Checks for valid ORTHOGONAL move
        Coordinate from  = new CoordinateImpl(4,4);
        Coordinate to  = new CoordinateImpl(4,5);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), to);
    }

    @Test
    void orthogonalMoveInvalid() {
        // Checks for invalid ORTHOGONAL move
        Coordinate from  = new CoordinateImpl(4,4);
        Coordinate to  = new CoordinateImpl(5,5);
        // moves the piece diagonally
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertFalse(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), from);
    }

    @Test
    void moveOppositePlayerPiece() {
        // Attempts to move opposite player piece; move should be invalid
        Coordinate from  = new CoordinateImpl(3,1);
        Coordinate to  = new CoordinateImpl(1,1);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertFalse(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), from);
    }

    @Test
    void firstTurnWrongPlayer() {
        // First player (John) should make the first move.
        // John (player 1) should make the first move, but Paul moves FROG first - INVALID move
        Coordinate from  = new CoordinateImpl(1,4);
        Coordinate to  = new CoordinateImpl(2,4);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertFalse(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), from);
    }

    @Test
    void maxMovementDistance() {
        // Check move distance of the piece.
        // move HORSE from (4,1) to (12,1) - invalid move, HORSE can move max 7 coordinates
        Coordinate from  = new CoordinateImpl(4,1);
        Coordinate to  = new CoordinateImpl(12,1);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertFalse(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), from);

        // move HORSE from (4,1) to (11,1) - valid move
        from  = new CoordinateImpl(4,1);
        to  = new CoordinateImpl(11,1);
        gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), to);
    }

    @Test
    void validFlyMove() {
        // Checks Fly attribute - BIRD flies over FROG, VALID move
        // first John moves SNAIL from (4,4) to (3,4) ; the first player (John) should make the first move
        Coordinate from  = new CoordinateImpl(4,4);
        Coordinate to  = new CoordinateImpl(3,4);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), to);

        // next Paul moves BIRD from (3,1) to (3,6) - valid move - BIRD can move max 5 coordinates; it can fly over other pieces
        from  = new CoordinateImpl(3,1);
        to  = new CoordinateImpl(3,6);
        gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), to);
    }

    @Test
    void invalidFlyMove() {
        // Check Fly attribute - HORSE attempts to fly over SNAIL, INVALID move
        // move HORSE from (4,1) to (4,6) - valid distance, however HORSE can't fly over other pieces
        Coordinate from  = new CoordinateImpl(4,1);
        Coordinate to  = new CoordinateImpl(4,6);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertFalse(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), from);

        // move HORSE from (4,1) to (11,1) - valid distance
        from  = new CoordinateImpl(4,1);
        to  = new CoordinateImpl(11,1);
        gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), to);
    }

    @Test
    void targetLocationOccupied() {
        // Move HORSE to a coordinate that already contains a piece
        // Pieces can be moved to a location that already has a piece only when POINT_CONFLICT rule is true. You will support this rule in future milestones.
        Coordinate from  = new CoordinateImpl(4,1);
        Coordinate to  = new CoordinateImpl(4,4);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertFalse(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), from);
    }


}
