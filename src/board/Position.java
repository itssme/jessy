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

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("x", x);
            obj.put("y", y);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
