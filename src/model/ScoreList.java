package model;

import database.Scorer;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import logging.LoggingSingleton;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

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


    public void fill(ResultSet resultSet) {
        LoggingSingleton.getInstance().info("Starting the Score-Fill");
        ArrayList<String> userList = new ArrayList<>();
        try {
            for (int i = 0; resultSet.next(); i++) {
                userList.add("" + resultSet.getInt("UID") + " - " +
                        resultSet.getString("name"));
            }
            userList.forEach(user -> {
                list.add((T) user);
            });
            target.setItems(list);
        } catch (SQLException e) {
            LoggingSingleton.getInstance().log(
                    Level.INFO,
                    e.getLocalizedMessage());
        }
    }
}
