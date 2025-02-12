/*******************************************************************************
 * This file was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 * All rights reserved.
 *******************************************************************************/

package escape;

import escape.required.*;
import java.util.*;

/**
 * Implementation of the EscapeGameManager interface.
 * This class manages the game logic and configurations.
 */
public class EscapeGameManagerImpl<C extends Coordinate> implements EscapeGameManager<C> {
    private final int xMax;
    private final int yMax;
    private final Coordinate.CoordinateType coordinateType;
    private final List<String> players;

    public EscapeGameManagerImpl(int xMax, int yMax, Coordinate.CoordinateType coordinateType, List<String> players) {
        this.xMax = xMax;
        this.yMax = yMax;
        this.coordinateType = coordinateType;
        this.players = new ArrayList<>(players);
    }

    @Override
    public C makeCoordinate(int x, int y) {
        if (x < 1 || x > xMax || y < 1 || y > yMax) {
            return null;
        }
        return (C) new CoordinateImpl(x, y);    }

    @Override
    public GameStatus move(C from, C to) {
        throw new EscapeException("Not implemented");
    }

    @Override
    public EscapePiece getPieceAt(C coordinate) {
        throw new EscapeException("Not implemented");
    }

    @Override
    public GameObserver addObserver(GameObserver observer) {
        throw new EscapeException("Not implemented");
    }

    @Override
    public GameObserver removeObserver(GameObserver observer) {
        throw new EscapeException("Not implemented");
    }

    public int getxMax() {
        return xMax;
    }

    public int getyMax() {
        return yMax;
    }

    public Coordinate.CoordinateType getCoordinateType() {
        return coordinateType;
    }

    public List<String> getPlayers() {
        return new ArrayList<>(players);
    }
}
