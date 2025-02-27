import escape.EscapeGameManager;
import escape.builder.EscapeGameBuilder;
import escape.*;
import escape.required.Coordinate;;
import escape.required.EscapePiece;
import escape.required.EscapePiece.*;
import escape.required.GameStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class Milestone3SampleTestsSQUARE {

    private EscapeGameManager escapeGameManager = null;

    @AfterAll
    static void testBreakdown() {
    }

    @BeforeEach
    void setup() throws Exception {
        escapeGameManager = new EscapeGameBuilder("./configurations/milestone3SampleSQUARE.egc").makeGameManager();
    }

    public Milestone3SampleTestsSQUARE() {
    }

    @Test
    void sqBoardNegativeIndex() {
        // SQUARE Board is initialized correctly - indices can be negative
        EscapePiece piece = escapeGameManager.getPieceAt(new CoordinateImpl(-2,-2));
        assertNotNull(piece);
        assertTrue(piece.getPlayer().equals("playerB"));
        assertEquals(piece.getName(), PieceName.DOG);

        piece = escapeGameManager.getPieceAt(new CoordinateImpl(3,-2));
        assertNotNull(piece);
        assertTrue(piece.getPlayer().equals("playerA"));
        assertEquals(piece.getName(), PieceName.HORSE);
    }

    @Test
    void diagonalMoveValid() {
        // Checks for valid DIAGONAL move; we move HORSE which has diagonal movement pattern.
        Coordinate from  = new CoordinateImpl(3,-2);
        Coordinate to  = new CoordinateImpl(1,0);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), to);
    }

    @Test
    void diagonalMoveInValidDistance() {
        // Checks for valid DIAGONAL move; we move HORSE having diagonal movement pattern.
        Coordinate from  = new CoordinateImpl(3,-2);
        Coordinate to  = new CoordinateImpl(9,-8);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertFalse(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), from);
    }

    @Test
    void diagonalInValidMoveCantFly() {
        // Checks for invalid DIAGONAL move; HORSE can't move over BIRD.
        Coordinate from  = new CoordinateImpl(3,-2);
        Coordinate to  = new CoordinateImpl(-1,2);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertFalse(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), from);
    }

    @Test
    void diagonalValidMoveCanFly() {
        // Checks for invalid DIAGONAL move; BIRD can move over DOG.
        Coordinate from  = new CoordinateImpl(-1,4);
        Coordinate to  = new CoordinateImpl(2,1);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertTrue(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), to);
    }

    @Test
    void orthogonalInvalidDiagonal() {
        // Checks for invalid ORTHOGONAL move; DOG can't move DIAGONAL
        Coordinate from  = new CoordinateImpl(-2, -2);
        Coordinate to  = new CoordinateImpl(-4,-4);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertFalse(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), from);
    }

    @Test
    void diagonalInvalidOrthogonal() {
        // Checks for invalid DIAGONAL move; BIRD can't move ORTHOGONAL
        Coordinate from  = new CoordinateImpl(-1, 4);
        Coordinate to  = new CoordinateImpl(-4,4);
        GameStatus gameStatus = escapeGameManager.move(from, to);
        assertFalse(gameStatus.isValidMove());
        assertEquals(gameStatus.finalLocation(), from);
    }


/*
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

     */


}
