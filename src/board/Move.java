package board;

import chessfigure.ChessFigure;
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

    private ChessFigure chessFigure;
    private Position from;
    private Position to;

    public Move(ChessFigure chessFigure, Position from,Position to) {
        this.chessFigure = chessFigure;
        this.from = from;
        this.to = to;
    }

    public ChessFigure getChessFigure() {
        return chessFigure;
    }

    public Position getFrom() { return from; }

    public Position getTo() {
        return to;
    }

    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("from x", from.getX());
            obj.put("from y", from.getY());

            obj.put("to x", to.getX());
            obj.put("to y", to.getY());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
