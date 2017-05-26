package model;

import database.Scorer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import logging.LoggingSingleton;

import java.util.Collection;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-24
 * Project: jessy
 * Desc.:   A ScoreList which is displayed at the right of the Application
 */
public class ScoreList<T> {

    /**
     * The list containing all the Players
     */
    private ObservableList<T> list;
    /**
     * The actual list
     */
    private ListView<T> target;

    /**
     * A constructor taking an ObservableList and the targetList as a parameter
     *
     * @param observableList The list of Items for the listView
     * @param targetList     The TargetList which should be updated
     */
    public ScoreList(ObservableList<T> observableList, ListView targetList) {
        this.target = targetList;
        list = observableList;
        Scorer sc = new Scorer(this);
        sc.start();
    }


    /**
     * Fills the Targetlist with the specified Collection
     * @param collection The Collection which should be set as the content of
     *                   the list
     */
    public void fill(Collection<T> collection) {
        LoggingSingleton.getInstance().info("Starting the Score-Fill");
        target.setItems(FXCollections.observableArrayList(collection));

    }
}
