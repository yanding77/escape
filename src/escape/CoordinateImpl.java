package escape;

import escape.required.Coordinate;

/**
 * Implementation of the Coordinate interface.
 */
public class CoordinateImpl implements Coordinate {
    private final int row;
    private final int column;

    public CoordinateImpl(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CoordinateImpl that = (CoordinateImpl) obj;
        return row == that.row && column == that.column;
    }
}


