package utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    19.05.17
 * Poject:  jessy
 * Package: utils
 * Desc.:   An Iterator over all the Models
 */
public class ModelIterator<T> implements Iterator<T> {

    /**
     * The underlying list for the Iterator
     */
    private ArrayList<T> list;

    /**
     * The Constructor which takes a collection of Items to start the Iterator
     * with
     *
     * @param c The collection which should be used to initialize the Iterator
     */
    public ModelIterator(Collection<T> c) {
        list = new ArrayList<>(c);
    }

    /**
     * An empty base Iterator
     */
    public ModelIterator() {
        list = new ArrayList<>();
    }

    @Override
    public boolean hasNext() {
        return !list.isEmpty();
    }

    @Override
    public T next() {
        T ret = list.get(0);
        this.remove();
        return ret;
    }

    @Override
    public void remove() {
        list.remove(0);
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        list.forEach(action);
    }

    /**
     * Adds an element to the Iterator
     * @param elem The element to add
     * @return True, if it was successfully added
     */
    public boolean addElement(T elem) {
        return list.add(elem);
    }
}
