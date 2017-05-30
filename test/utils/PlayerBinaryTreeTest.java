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
    void insertTest2() {
        PlayerBinaryTree<String, Float> tree = new PlayerBinaryTree<>();
        assert tree.insert("Koka", 10f);
        assert tree.insert("Simon", 16f);
        assert tree.insert("Mario", 13f);
        assert tree.insert("Joel", 15f);
        assert tree.insert("Matthias", 12f);
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

    @Test
    void asCollectionLongTest() {
        PlayerBinaryTree<String, Float> tree = new PlayerBinaryTree<>();
        assert tree.insert("Koka", 10f);
        assert tree.insert("Simon", 16f);
        assert tree.insert("Mario", 13f);
        assert tree.insert("Joel", 15f);
        assert tree.insert("Matthias", 12f);

        Object[][] sortedList = new Object[][]{
                {"Koka", 10f},
                {"Matthias", 12f},
                {"Mario", 13f},
                {"Joel", 15f},
                {"Simon", 16f}
        };

        Iterator<String> it = tree.asCollection().iterator();

        String elem;

        for (int i = 0; i < sortedList.length; i++) {
            elem = it.next();
            assert elem.equals("" + sortedList[i][0] + " - " + sortedList[i][1]);
        }
    }

    @Test
    void asCollectionLongTest2() {
        PlayerBinaryTree<String, Float> tree = new PlayerBinaryTree<>();

        Object[][] sortedList = new Object[1000][2];

        for (int i = 0; i < 1000; i++) {
            assert tree.insert(Integer.toString(i), (float) i);
            sortedList[i][0] = Integer.toString(i);
            sortedList[i][1] = (float) i;
        }

        Iterator<String> it = tree.asCollection().iterator();

        String elem;

        for (int i = 0; i < sortedList.length; i++) {
            elem = it.next();
            assert elem.equals("" + sortedList[i][0] + " - " + sortedList[i][1]);
        }
    }
}