package model;

import database.Scorer;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import logging.LoggingSingleton;
import utils.ModelIterator;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-24
 * Project: jessy
 * Desc.:
 */
public class ScoreList<T> {

    private ObservableList<T> list;
    private ListView<T> target;

    public ScoreList(ObservableList<T> observableList, ListView targetList) {
        this.target = targetList;
        list = observableList;
        Scorer sc = new Scorer(this);
        sc.start();
    }


    public void fill(ModelIterator<T> targetFill) {
        LoggingSingleton.getInstance().info("Starting the Score-Fill");
        target.setItems(targetFill.asObservableList());

    }
}
