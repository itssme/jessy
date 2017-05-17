package board;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   Move of a Chessfigure
 */
public class Move {

    private Position from;
    private Position to;

    public Move(Position from, Position to) {
        this.from = from;
        this.to = to;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("from x", from.getRow());
            obj.put("from y", from.getCol());

            obj.put("to x", to.getRow());
            obj.put("to y", to.getCol());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
