package model;

import database.Scorer;

import javax.swing.*;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-24
 * Project: jessy
 * Desc.:
 */
public class ScoreList<T> extends JList<T> {

    public ScoreList() {
        super();
        new Scorer(this).start();
    }

}
