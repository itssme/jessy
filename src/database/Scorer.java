package database;

import model.ScoreList;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-24
 * Project: jessy
 * Desc.:
 */
public class Scorer extends Thread implements Runnable {

    private ScoreList targetList;
    private final String dbFile = "scores.db";

    public Scorer(ScoreList target) {
        this.targetList = target;
    }

    @Override
    public void run() {
        super.run();

    }
}