package board;

import com.github.bhlangonijr.chesslib.Square;
import logging.LoggingSingleton;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TreeMap;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:   Move of a Chessfigure
 */
public class Move {

    /**
     * Where did the Move come from
     */
    private Position from;
    /**
     * Where will the Move go
     */
    private Position to;

    /**
     * A constructor which takes two Positions
     *
     * @param from The Position from where the Move came
     * @param to   The Position where the Move goes
     */
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

    /**
     * Returns a JSONObject representation of the Move-Object
     * @return The JSONObject of this Move
     */
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


    /**
     * A Map which converts Cells from the format E7 to two integers for
     * the JTable
     */
    private static TreeMap<Character, Integer> colToInt =
            new TreeMap<>();
    private static TreeMap<Integer, Character> intToCol =
            new TreeMap<>();

    /*
      A small static block for the conversion-Maps
     */
    static {
        colToInt.put('A', 0);
        colToInt.put('B', 1);
        colToInt.put('C', 2);
        colToInt.put('D', 3);
        colToInt.put('E', 4);
        colToInt.put('F', 5);
        colToInt.put('G', 6);
        colToInt.put('H', 7);

        intToCol.put(0, 'A');
        intToCol.put(1, 'B');
        intToCol.put(2, 'C');
        intToCol.put(3, 'D');
        intToCol.put(4, 'E');
        intToCol.put(5, 'F');
        intToCol.put(6, 'G');
        intToCol.put(7, 'H');
    }

    /**
     * Returns a Move object from the chesslib based on our Move-object
     * @param m The custom Move object defined for this Project
     * @return The Move-Object from the library
     */
    public static com.github.bhlangonijr.chesslib.move.Move
    convertMoveToLib(Move m) {

        Position from = m.getFrom();
        Position to = m.getTo();

        return new com.github.bhlangonijr.chesslib.move.Move(
                getSquare(from),
                getSquare(to));
    }


    /**
     * This function converts the E7 format of the library to a tuple of
     * integers.
     * It returns B7 as follows:
     * B7: {6, 1}, whereas the B is the second element, and 7 turns into the,
     * by one, decremented row
     *
     * @param sq The square, which should be converted
     * @return The tuple specifying the row and column or null.
     */
    public static int[] getRowColPair(Square sq) {
        char[] parts = sq.toString().toCharArray();
        if (sq == Square.NONE) {
            return null;
        }
        return new int[]{
                //(Character.getNumericValue(parts[1]) - 8) * (-1),
                Character.getNumericValue(parts[1]) - 1,
                colToInt.get(parts[0])
        };
    }

    /**
     * Returns a Square object pased on the Position
     *
     * @param p The position which generates the Square
     * @return The Square generated from the Position
     */
    public static Square getSquare(Position p) {
        return getSquare(p.getRow(), p.getCol());
    }

    /**
     * GetSquare returns an Enum-Element of the chesslibrary based on column
     * and row
     *
     * @param row The row of the Piece
     * @param col The column of the Piece
     * @return The Enum-Value of NONE
     */
    public static Square getSquare(int row, int col) {
        return Square.fromValue("" + intToCol.get(col) + (row + 1));
    }

    /**
     * Returns our Move-Object from the library-Move object
     *
     * @param mv The Library-defined Move object
     * @return A Move object
     */
    public static Move
    getMoveFromLib(com.github.bhlangonijr.chesslib.move.Move mv) {
        Square from = mv.getFrom();
        Square to = mv.getTo();

        int[] fromArr = getRowColPair(from);
        int[] toArr = getRowColPair(to);

        if (toArr != null && fromArr != null) {
            return new Move(
                    new Position(fromArr[0], fromArr[1]),
                    new Position(toArr[0], toArr[1])
            );
        } else {
            LoggingSingleton.getInstance().info(
                    "An invalid move got passed in, returning empty" +
                            "dummy move...");
            return new Move(
                    new Position(0, 0),
                    new Position(0, 0)
            );
        }
    }
}
