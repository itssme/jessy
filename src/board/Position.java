package board;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   Position of a chess figure on the board
 */
public class Position {

    /**
     * The row for this Position
     */
    private int row;
    /**
     * The column for this Position
     */
    private int col;

    /**
     * A simple constructor which takes a row and a column as an Argument
     *
     * @param row The row for this Position on the ChessBoard
     * @param col The column for this Position on the ChessBoard
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Compares two Positions based on their x and y coordinates
     * @param x The x-Coordinate of the other Position
     * @param y The y-Coordinate of the other Position
     * @return True, if x and y are the same as row and column
     */
    public boolean equals(int x, int y) {
        return this.row == x && this.col == y;
    }

    /**
     * Compares two Objects. It firstly checks whether the other Object is of
     * type Position. If it is not of Type Position, it will return false;
     * Otherwise it will return the result of #equals(int x, int y).
     *
     * @param obj The other Object. Should be of type Position
     * @return True, if the other Object is a Position object and represents
     *         the same Position on the Board, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position other = (Position) obj;
            return this.equals(other.getRow(), other.getCol());
        } else {
            return false;
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * A method which will return a String representing the Position on the Board
     * @return The Position in the following format:
     * <code>'{row|col}'</code>
     */
    @Override
    public String toString() {
        return "{" + row + "|" + col + "}";
    }

    /**
     * Turns the Position into a JSONObject
     * @return The JSONObject representation of the Position.
     */
    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("row", row);
            obj.put("col", col);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
