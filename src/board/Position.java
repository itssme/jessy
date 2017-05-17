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

    private int row;
    private int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean equals(int x, int y) {
        return this.row == x && this.col == y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position other = (Position) obj;
            return other.getRow() == this.getRow() &&
                    other.getCol() == this.getCol();
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

    @Override
    public String toString() {
        return "{" + row + "|" + col + "}";
    }

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
