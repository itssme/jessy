package board;

import chessfigure.Queen;
import model.BoardModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    15.05.17
 * Poject:  jessy
 * Package: board
 * Desc.:
 */
class PositionTest {
    @Test
    void isValid() {
        BoardModel board = new BoardModel(8, 8);
        System.out.println(new Position(1, 2).isValid(
                new Queen(new Position(2, 2),
                        "graphics/queen_white.png", true)));
    }

    @Test
    void equals() {

        assertTrue(new Position(5, 3).
                equals(new Position(5, 3)));
        assertTrue(new Position(5, 5).equals(new Position(5, 5)));

        assertFalse(new Position(5, 3).equals(5));
        //assert new Position(5, 5).equals(5, 3);

    }


}