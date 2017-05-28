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
 * Desc.:   A factory which will Initialize the Database-driver and take care
 *          of returning a suitable Connection-Object to work with
 */
public class ConnectionFactory {

    /**
     * A final integer variable to represent the SQLite database.
     * This is mostly for calling the Factory
     */
    public static final int SQLITE = 0;

    /**
     * This will store, which Database System will be used.
     * This will be the values as defined in the declarations above
     */
    private final int DATABASE_SYSTEM;
    /**
     * The connectionString, to which location the Database-driver will connect
     * to
     */
    private String connectionString;

    /**
     * This Constructor will initialize the Database-driver and takes care of
     * setting up the database
     *
     * @param dbSys            The Database-System for this factory.
     * @param connectionString The connectionString, to which file or database
     *                         the driver will connect.
     */
    public ConnectionFactory(int dbSys, String connectionString) {
        this.connectionString = connectionString;
        this.DATABASE_SYSTEM = dbSys;
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
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.ERROR_MESSAGE);
            if (res == JOptionPane.OK_OPTION) {
                System.exit(1);
            }
        }
    }

    /**
     * Establishes a Connection to the Database and returns the resulting
     * Connection-Object.
     * @return The Connection-Object for this connection.
     */
    public Connection establishConnection() {
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
