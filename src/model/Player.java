package model;

import database.ConnectionFactory;
import utils.ModelIterator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    private float score;

    /**
     * A simple constructor to get Access to all the class-methods
     */
    public Player() {
    }

    /**
     * The Player Constructor takes the UserID, the name and the score
     * as an Argument.
     *
     * @param UID   The User-ID
     * @param name  The name of the player
     * @param score The score of the game
     */
    public Player(int UID, String name, float score) {
        this.UID = UID;
        this.name = name;
        this.score = score;
    }

    public ModelIterator<Player> getAllPlayers() {
        ModelIterator<Player> ret = new ModelIterator<>();

        try (Connection conn =
                     new ConnectionFactory(ConnectionFactory.SQLITE,
                             "db/jessy.db").establishConnection()) {

            try (PreparedStatement psmt =
                         conn.prepareStatement("select * from game;");
                 ResultSet res = psmt.executeQuery();) {

                while (res.next()) {
                    ret.addElement(new Player(
                            res.getInt("UID"),
                            res.getString("name"),
                            res.getFloat("score"))
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public int getMaxUID() {
        try (Connection conn = new ConnectionFactory(
                ConnectionFactory.SQLITE,
                "db/jessy.db").establishConnection()) {

            try (PreparedStatement psmt = conn.prepareStatement(
                    "select max(UID) from game;");
                 ResultSet res = psmt.executeQuery()) {

                res.next();
                return res.getInt(1);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
