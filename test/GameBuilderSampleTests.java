import escape.*;
import escape.builder.*;
import escape.required.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class GameBuilderSampleTests {

    @Test
    void makeGameObject(){
        EscapeGameManager<Coordinate> escapeGameManager = null;
        try{
            escapeGameManager = new EscapeGameBuilder("configurations/minimalEGC5x10SQ.egc").makeGameManager();
        }catch(Exception ex){
            fail("Exception from builder: " + ex.getMessage());
        }
        assertNotNull(escapeGameManager);
    }

    @Test
    void checkBoardSize(){
        EscapeGameManagerImpl<Coordinate> escapeGameManager = null;
        try{
            escapeGameManager = (EscapeGameManagerImpl<Coordinate>) new EscapeGameBuilder("configurations/minimalEGC5x10SQ.egc").makeGameManager();
        }catch(Exception ex){
            fail("Exception from builder: " + ex.getMessage());
        }
        assertEquals(5, escapeGameManager.getxMax());
        assertEquals(10, escapeGameManager.getyMax());

    }

    @Test
    void checkCoordinateType() {
        EscapeGameManagerImpl<Coordinate> escapeGameManager = null;
        try {
            escapeGameManager = (EscapeGameManagerImpl<Coordinate>) new EscapeGameBuilder("configurations/minimalEGC5x10SQ.egc").makeGameManager();
        } catch (Exception ex) {
            fail("Exception from builder: " + ex.getMessage());
        }
        assertEquals(Coordinate.CoordinateType.SQUARE, escapeGameManager.getCoordinateType());
    }

    @Test
    void checkPlayersNames() {
        EscapeGameManagerImpl<Coordinate> escapeGameManager = null;
        try {
            escapeGameManager = (EscapeGameManagerImpl<Coordinate>) new EscapeGameBuilder("configurations/minimalEGC5x10SQ.egc").makeGameManager();
        } catch (Exception ex) {
            fail("Exception from builder: " + ex.getMessage());
        }
        List<String> playersNames = escapeGameManager.getPlayers();
        assertEquals(0, playersNames.get(0).compareTo("Chris"));
        assertEquals(0, playersNames.get(1).compareTo("Pat"));
    }

    @Test
    void checkSquareCoordinateInBounds(){
        EscapeGameManager<Coordinate> escapeGameManager = null;
        try{
            escapeGameManager = new EscapeGameBuilder("configurations/minimalEGC5x10SQ.egc").makeGameManager();
        }catch(Exception ex){
            fail("Exception from builder: " + ex.getMessage());
        }
        Coordinate coordinate = new CoordinateImpl(5,4);
        Coordinate escapeCoordinate = escapeGameManager.makeCoordinate(5,4);
        assertEquals(coordinate.getRow(), escapeCoordinate.getRow());
        assertEquals(coordinate.getColumn(), escapeCoordinate.getColumn());

        //alternatively you can have CoordinateImpl override the equals method from the Object class and
        // have your assert statements directly compare coordinate values.
        //assertEquals(coordinate, escapeGameManager.makeCoordinate(5,4));
    }

    @Test
    void checkSquareCoordinateOutOfBounds(){
        EscapeGameManager<Coordinate> escapeGameManager = null;
        try{
            escapeGameManager = new EscapeGameBuilder("configurations/minimalEGC5x10SQ.egc").makeGameManager();
        }catch(Exception ex){
            fail("Exception from builder: " + ex.getMessage());
        }
        // CoordinateImpl(10,10) is out of bounds. makeCoordinate method should return null.
        assertEquals(null, escapeGameManager.makeCoordinate(10,10));
    }

}
