package board;

import com.github.bhlangonijr.chesslib.Square;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    26.05.17
 * Poject:  jessy
 * Package: board
 * Desc.:   The test for moves
 */
class MoveTest {
    @Test
    void convertMoveToLib() {
        Move m = new Move(
                new Position(1, 1),
                new Position(3, 1)
        );
        assert Move.convertMoveToLib(m).getFrom().
                equals(Move.getSquare(m.getFrom()));
    }

    @Test
    void getRowColPair() {
        assert Arrays.toString(Move.getRowColPair(Square.A1)).equals("[0, 0]");
        assert Arrays.toString(Move.getRowColPair(Square.NONE)).equals("null");
        assert Arrays.toString(Move.getRowColPair(Square.B7)).equals("[6, 1]");
    }

    @Test
    void getSquare() {
        assert Move.getSquare(0, 0).equals(Square.A1);
        assert !Move.getSquare(6, 2).equals(Square.C6);
    }

    @Test
    void getSquare1() {
        assert Move.getSquare(new Position(0, 0)).equals(Square.A1);
        assert !Move.getSquare(new Position(5, 6)).equals(Square.NONE);
    }

    @Test
    void getMoveFromLib() {
        com.github.bhlangonijr.chesslib.move.Move m =
                new com.github.bhlangonijr.chesslib.move.Move(
                        Square.B1,
                        Square.B2
                );
        assert Move.getMoveFromLib(m).getTo().equals(1, 1);
    }

}