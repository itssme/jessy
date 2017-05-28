package board;

import org.junit.jupiter.api.Test;

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
    void equals1() {
        assert new Position(5, 5).equals(5, 5);
        assert !new Position(5, 5).equals(4, 5);
        assert new Position(6, 6).equals(6, 6);
        assert !new Position(6, 6).equals(8, 4);
    }

    @Test
    void equals2() {
        assert !new Position(5, 5).equals(6);
        assert new Position(5, 6).equals(new Position(5, 6));
    }

    @Test
    void testToString() {
        assert !new Position(5, 6).toString().equals("{6|5}");
    }

}