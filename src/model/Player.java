package model;

import database.ConnectionFactory;
import logging.LoggingSingleton;
import utils.ModelIterator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    18.05.17
 * Poject:  jessy
 * Package: model
 * Desc.:
 */
public class Player {

    private int UID;
    private String name;

    /**
     * The Player Constructor takes the UserID and the name as an Argument.
     *
     * @param UID  The User-ID
     * @param name The name of the player
     */
    public Player(int UID, String name) {
        this.UID = UID;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getUID() {
        return UID;
    }

    /**
     * Returns an ArrayList of type Player where all the players with the
     * specified names
     *
     * @param name The name of the player
     * @return An ArrayList containing the Player-Objects
     */
    public ModelIterator<Player> getPlayerByName(String name) {
        Connection conn = this.getConnection();
        ArrayList<Player> result = new ArrayList<>();
        try {
            PreparedStatement psmt = conn.prepareStatement(
                    "select * from player where name = ?;");
            psmt.setString(1, name);
            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                result.add(new Player(
                        rs.getInt("UID"),
                        rs.getString("name")));
            }
            conn.close();
        } catch (SQLException e) {
            LoggingSingleton.getInstance().severe(e.getLocalizedMessage());
        }
        return new ModelIterator<>(result);
    }

    /**
     * Returns an finished instance of a Connection to the SQLITE-DB
     *
     * @return The Connection-Object for the SQLITE-DB
     */
    private Connection getConnection() {
        return new ConnectionFactory(
                ConnectionFactory.SQLITE,
                "db/jessy.db").establishConnection();
    }
}
