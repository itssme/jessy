package utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
 * Desc.:
 */
public class ModelIterator<T> implements Iterator<T> {

    private ArrayList<T> list;

    public ModelIterator(Collection<T> c) {
        list = new ArrayList<T>(c);
    }

    public ModelIterator() {
        list = new ArrayList<T>();
    }

    @Override
    public boolean hasNext() {
        return list.isEmpty();
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
     * Returns this Iterator as an ObservableList
     *
     * @return An ObservableList containing all the Elements
     */
    public ObservableList<T> asObservableList() {
        return FXCollections.observableArrayList(list);
    }

    public boolean addElement(T elem) {
        return list.add(elem);
    }
}
