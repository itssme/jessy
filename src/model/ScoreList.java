package model;

import database.Scorer;
import logging.Logging;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
public class ScoreList<T> extends JList<T> {

    public ScoreList() {
        super();
        Scorer sc = new Scorer(this);
        sc.start();
    }

    public void fill(ResultSet resultSet) {
        ArrayList<String> userList = new ArrayList<>();
        try {
            for (int i = 0; resultSet.next(); i++) {
                userList.add("" + resultSet.getInt("UID") + " - " + resultSet.getString("name"));
            }
            this.setListData((T[]) userList.toArray());

        } catch (SQLException e) {
            Logging.logToFile(Level.INFO, e.getLocalizedMessage());
        }
    }
}
