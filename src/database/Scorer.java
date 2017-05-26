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
 * Desc.:   The Scorer takes care of setting up the tables in the database
 *          It will also, as soon as everything is set up, start to fill the
 *          ScoreList on the right of the Program
 */
public class Scorer extends Thread implements Runnable {

    /**
     * The targetList, where all the Users will be displayed
     */
    private ScoreList targetList;
    /**
     * The connectionString for the database. This is declared public static and
     * final, so it will be easily accessible from other classes.
     */
    public static final String DB = "db/jessy.db";
    /**
     * The Username of the player which will be used. This will be collected
     * from the Dialog at the start of the Program
     */
    public static String USERNAME;
    /**
     * The Opponent is the Name of the enemy. It will be collected at the
     * beginning of the game, as soon as a player connects to the host.
     */
    public static String OPPONENT;

    /**
     * A constructor which will take the targetList - the ScoreList at the right
     * of the program - as a parameter for future references.
     *
     * @param target The reference to the ScoreList.
     */
    public Scorer(ScoreList target) {
        this.targetList = target;
    }

    /**
     * The implemented run method from Runnable to enable Threaded retrieval
     * of the user-Scores and names
     */
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

    /**
     * This method sets up the tables in the database and returns true, if no
     * SQL Errors occured
     * @param conn The connection to the Database.
     * @return True, if no errors occured.
     */
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
