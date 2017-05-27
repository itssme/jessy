package model;

import com.github.bhlangonijr.chesslib.Square;
import org.junit.jupiter.api.Test;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    27.05.17
 * Poject:  jessy
 * Package: model
 * Desc.:
 */
class BoardModelTest {
    @Test
    void mouseClick() {
        BoardModel board = new BoardModel(8, 8);
        board.changeSelection(0, 0, false, false);
        if (!board.mouseClick().equals(Square.A1)) throw new AssertionError();

        board.changeSelection(5, 7, false, false);
        if (!board.mouseClick().equals(Square.H6)) throw new AssertionError();

        board.changeSelection(7, 2, false, false);
        if (!board.mouseClick().equals(Square.C8)) throw new AssertionError();
    }

}