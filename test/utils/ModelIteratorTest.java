package utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    27.05.17
 * Poject:  jessy
 * Package: utils
 * Desc.:   A test for the ModelIterator
 */
class ModelIteratorTest {
    @Test
    void hasNext() {
        ModelIterator<Integer> intIter = new ModelIterator<>();
        assert intIter.hasNext() == false;
        intIter.addElement(1);
        intIter.addElement(2);
        intIter.addElement(3);
        intIter.addElement(4);
        intIter.addElement(5);
        intIter.addElement(6);
        intIter.addElement(7);
        int counter = 0;
        while (intIter.hasNext()) {
            counter += 1;
            intIter.next();
        }
        assert counter == 7;
        assert intIter.hasNext() == false;
    }

    @Test
    void next() {
        ModelIterator<Integer> intIter = new ModelIterator<>();
        intIter.addElement(1);
        intIter.addElement(3);
        intIter.addElement(5);
        assert intIter.next() == 1;
        intIter.addElement(3);
        assert intIter.next() == 3;
        assert intIter.next() == 5;
        assert intIter.next() == 3;
    }

    @Test
    void remove() {
        ModelIterator<Integer> intIter = new ModelIterator<>();
        intIter.addElement(3);
        intIter.addElement(5);
        intIter.remove();
        int counter = 0;
        while (intIter.hasNext()) {
            counter += 1;
            intIter.next();
        }
        assert counter == 1;
    }

    @Test
    void forEachRemaining() {
        ModelIterator<Integer> integerModelIterator = new ModelIterator<>();
        integerModelIterator.addElement(5);
        integerModelIterator.addElement(31);
        integerModelIterator.addElement(5);
        integerModelIterator.addElement(30);
        integerModelIterator.addElement(5);
        integerModelIterator.addElement(30);

        for (int i = integerModelIterator.next();
             integerModelIterator.hasNext() && i != 30;
             i = integerModelIterator.next()) {
            System.out.println(i);
        }

        ArrayList arrayList = new ArrayList();
        integerModelIterator.forEachRemaining(integer -> {
            arrayList.add(integer);
        });
        assert arrayList.size() == 2;
    }

    @Test
    void addElement() {
        ModelIterator<Integer> intIter = new ModelIterator<>();
        assert intIter.addElement(3);
        assert intIter.addElement(3);
        assert intIter.addElement(3);
        assert intIter.addElement(3);
        assert intIter.addElement(3);

        int counter = 0;
        while (intIter.hasNext()) {
            counter += 1;
            intIter.next();
        }
        assert counter == 5;
    }

}