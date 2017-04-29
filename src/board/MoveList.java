package board;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    21.04.17
 * Package: board
 * Desc.:
 */
public class MoveList<E> extends ArrayList<E> {

    @Override
    public boolean add(E e) {
        if (e == null) {
            return false;
        } else {
            return super.add(e);
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.contains(null)) {
            return false;
        } else {
            return super.addAll(c);
        }
    }
}
