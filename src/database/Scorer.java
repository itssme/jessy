package database;

import logging.LoggingSingleton;
import model.Player;
import model.ScoreList;
import utils.PlayerBinaryTree;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-24
 * Project: jessy
 * Desc.:
 */
public class Scorer extends Thread implements Runnable {


    private ScoreList targetList;
    public static final String DB = "db/jessy.db";
    public static String USERNAME;
    public static String OPONENT;

    public Scorer(ScoreList target) {
        this.targetList = target;
    }

    @Override
    public void run() {
        super.run();
        Connection conn;
        conn = new ConnectionFactory(ConnectionFactory.SQLITE,
                DB).
                establishConnection();
        if (conn != null) {
            this.setupDB(conn);
            targetList.fill(
                    new PlayerBinaryTree<String, Float>(
                            new Player().getAllPlayers()).asCollection());
        } else {
            LoggingSingleton.getInstance().log(
                    Level.WARNING,
                    "Couldn't initialize Connection");
        }
    }


    private boolean setupDB(Connection conn) {
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String TABLE_STRING = "" +
                    "create table if not exists game(" +
                    "name varchar(255) not null," +
                    "score float not null" +
                    ");";
            stmt.execute(TABLE_STRING);
            stmt.close();
        } catch (SQLException e) {
            LoggingSingleton.getInstance().log(
                    Level.SEVERE,
                    e.getLocalizedMessage()
            );
            return false;
        }
        return true;
    }
}
