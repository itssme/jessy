package utils;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    27.05.17
 * Poject:  jessy
 * Package: utils
 * Desc.:
 */
class PlayerBinaryTreeTest {
    @Test
    void insert() {
        PlayerBinaryTree<String, Float> tree = new PlayerBinaryTree<>();
        assert tree.insert("Simon", 10f);
        assert tree.insert("Simon", 11f);
        assert tree.insert("Joel", 10f);

    }

    @Test
    void asCollection() {
        PlayerBinaryTree<String, Float> tree = new PlayerBinaryTree<>();
        assert tree.insert("Simon", 10f);
        assert tree.insert("Simon", 11f);
        assert tree.insert("Joel", 10f);

        Object[][] sortedList = new Object[][]{
                {"Joel", 10f},
                {"Simon", 10f},
                {"Simon", 11f}
        };

        Iterator<String> it = tree.asCollection().iterator();

        String elem;

        elem = it.next();
        assert elem.equals("" + sortedList[0][0] + " - " + sortedList[0][1]);

        elem = it.next();
        assert elem.equals("" + sortedList[1][0] + " - " + sortedList[1][1]);

        elem = it.next();
        assert elem.equals("" + sortedList[2][0] + " - " + sortedList[2][1]);
        assert !elem.equals("Joel");


    }

}