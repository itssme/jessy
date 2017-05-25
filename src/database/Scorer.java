package database;

import logging.LoggingSingleton;
import model.ScoreList;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
            //targetList.fill(this.readUserDB(conn));
        } else {
            LoggingSingleton.getInstance().log(
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
            LoggingSingleton.getInstance().log(
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
                    "create table if not exists game(" +
                    "UID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name text not null," +
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
