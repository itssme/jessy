package model;

import database.ConnectionFactory;
import database.Scorer;
import logging.LoggingSingleton;
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
 * Desc.:   The programmatic representation of a Player as in the database
 */
public class Player {

    /**
     * The name of the Player
     */
    private String name;
    /**
     * The score of the player
     */
    private float score;

    public String getName() {
        return name;
    }

    public float getScore() {
        return score;
    }

    /**
     * A simple constructor to get Access to all the class-methods
     */
    public Player() {
    }

    /**
     * The Player Constructor takes the UserID, the name and the score
     * as an Argument.
     *
     * @param name  The name of the player
     * @param score The score of the game
     */
    public Player(String name, float score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Returns an Iterator with all Players in the database
     *
     * @return The Iterator with all Players
     */
    public ModelIterator<Player> getAllPlayers() {
        ModelIterator<Player> ret = new ModelIterator<>();

        try (Connection conn =
                     new ConnectionFactory(ConnectionFactory.SQLITE,
                             Scorer.DB).establishConnection()) {

            try (PreparedStatement psmt =
                         conn.prepareStatement("select * from game;");
                 ResultSet res = psmt.executeQuery();) {

                while (res.next()) {
                    ret.addElement(new Player(
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

    /**
     * Saves a player and the Score to the database
     * @return True, if it was saved successful
     */
    public boolean savePlayer() {
        try (Connection conn = new ConnectionFactory(
                ConnectionFactory.SQLITE,
                Scorer.DB
        ).establishConnection()) {

            try (PreparedStatement psmt = conn.prepareStatement(
                    "insert into game values(?, ?);"
            )) {

                psmt.setString(1, this.getName());
                psmt.setFloat(2, this.getScore());

                return psmt.execute();
            }

        } catch (SQLException e) {
            LoggingSingleton.getInstance().severe(e.getLocalizedMessage());
        }
        return false;
    }

    /**
     * An inherited method to represent the Player
     *
     * @return The String representing the Player
     */
    @Override
    public String toString() {
        return this.getName() + " - " + this.getScore();
    }
}
