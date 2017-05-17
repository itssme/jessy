package database;


import logging.LoggingSingleton;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-04-07
 * Project: jessy
 * Desc.:
 */
public class ConnectionFactory {

    public static final int SQLITE = 0;

    private final int DATABASE_SYSTEM;
    private String connectionString;

    public ConnectionFactory(int dbSys, String connectionString) {
        this.connectionString = connectionString;
        this.DATABASE_SYSTEM = dbSys;
    }

    public Connection establishConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            LoggingSingleton.getInstance().log(
                    Level.SEVERE,
                    e.getLocalizedMessage());
            int res = JOptionPane.showConfirmDialog(null,
                    "We couldn't initialize the Database-Driver, " +
                            "closing the application now",
                    "An error Occured",
                    JOptionPane.OK_OPTION,
                    JOptionPane.ERROR_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                System.exit(1);
            }
        }
        Connection conn = null;
        if (this.DATABASE_SYSTEM == SQLITE) {
            try {
                conn = DriverManager.
                        getConnection("jdbc:sqlite:" + connectionString);
            } catch (SQLException e) {
                LoggingSingleton.getInstance().log(
                        Level.SEVERE,
                        "An SQLException occured");
                LoggingSingleton.getInstance().log(
                        Level.SEVERE,
                        e.getLocalizedMessage());
            }
        }
        return conn;
    }

}
