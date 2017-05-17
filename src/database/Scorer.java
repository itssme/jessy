package database;

import logging.LoggingSingleton;
import model.ScoreList;

import java.sql.*;
import java.util.logging.Level;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-24
 * Project: jessy
 * Desc.:
 */
public class Scorer extends Thread implements Runnable {

    public static int MOVES = 0;

    public static void incrementMoves() {
        MOVES += 1;
    }

    public static int getMOVES() {
        return MOVES;
    }

    private final int ENV_DEV = 0;
    private final int ENV_REL = 1;

    private final int DEVENV = ENV_DEV;

    private ScoreList targetList;
    private final String DBFILE = "jessy.db";
    public static String USERNAME;

    public Scorer(ScoreList target) {
        this.targetList = target;
    }

    @Override
    public void run() {
        super.run();
        Connection conn = null;
        if (this.DEVENV == this.ENV_REL) {
            conn = new ConnectionFactory(ConnectionFactory.SQLITE,
                    "db/" + DBFILE).
                    establishConnection();
        } else if (this.DEVENV == this.ENV_DEV) {
            // Connects to an in-memory Database which makes it easier to get
            // rid of the Database.
            conn = new ConnectionFactory(ConnectionFactory.SQLITE,
                    ":memory:").
                    establishConnection();
        }
        if (conn != null) {
            this.setupDB(conn);
            if (this.DEVENV == ENV_DEV) {
                this.insertTestData(conn, null);
            }
            targetList.fill(this.readUserDB(conn));
        } else {
            LoggingSingleton.getInstance().logToFile(
                    Level.WARNING,
                    "Couldn't initialize Connection");
        }
    }


    private boolean insertTestData(
            Connection conn,
            String[] exampleData) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "insert into player(name) values(?);")) {
            String[] names;
            names = (exampleData != null) ? exampleData :
                    new String[]{"John", "Hans", "Harald"};
            for (String name :
                    names) {
                stmt.setString(1, name);
                stmt.execute();
            }
            return true;
        } catch (SQLException e) {
            LoggingSingleton.getInstance().logToFile(
                    Level.INFO,
                    e.getLocalizedMessage());
        }
        return false;
    }

    private boolean setupDB(Connection conn) {
        Statement stmt;
        try {
            stmt = conn.createStatement();
            String TABLE_STRING = "" +
                    "create table if not exists player(" +
                    "UID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name text not null unique" +
                    ");" +
                    "" +
                    "create table if not exists game(" +
                    "GID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "UID references player" +
                    "score integer not null default 30" +
                    ");";
            stmt.execute(TABLE_STRING);
            stmt.execute(
                    "insert into player(name) values('" + USERNAME + "');");
            stmt.close();
        } catch (SQLException e) {
            LoggingSingleton.getInstance().logToFile(
                    Level.SEVERE,
                    e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    private ResultSet readUserDB(Connection conn) {
        PreparedStatement stmt;
        try {
            stmt = conn.prepareStatement("select * from player;");
            return stmt.executeQuery();
        } catch (SQLException e) {
            LoggingSingleton.getInstance().logToFile(
                    Level.SEVERE,
                    e.getLocalizedMessage());
        }
        return null;
    }
}
