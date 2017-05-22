package model;

import database.ConnectionFactory;
import logging.LoggingSingleton;
import utils.ModelIterator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    18.05.17
 * Poject:  jessy
 * Package: model
 * Desc.:
 */
public class Game {

    private int GID;
    private int UID;
    private int score;
    public final AtomicInteger counter = new AtomicInteger(this.getCounter());

    public Game(int GID, int UID, int score) {
        this.GID = GID;
        this.UID = UID;
        this.score = score;
    }

    public int getGID() {
        return GID;
    }

    public int getUID() {
        return UID;
    }

    public int getScore() {
        return score;
    }

    /**
     * GetGameByPlayer returns an ArrayList of type Game which holds all the
     * Games for this Player;
     *
     * @param p The Player which should be found for the games
     * @return An ArrayList of type Game with all the games.
     */
    public ModelIterator<Game> getGameByPlayer(Player p) {
        Connection conn = this.getConnection();
        ArrayList<Game> result = new ArrayList<>();
        try {
            PreparedStatement psmt =
                    conn.prepareStatement("select * from game where UID = ?;");
            psmt.setInt(1, p.getUID());
            ResultSet res = psmt.executeQuery();
            while (res.next()) {
                result.add(new Game(
                        res.getInt("GID"),
                        res.getInt("UID"),
                        res.getInt("score")));
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

    /**
     * GetCounter returns the GID from the gameTable for insert.
     *
     * @return The maximum of GID or 0
     */
    private int getCounter() {
        Connection conn = this.getConnection();
        try {
            PreparedStatement psmt = conn.prepareStatement(
                    "selelct max(GID) from game;");
            ResultSet res = psmt.executeQuery();
            res.next();
            conn.close();
            return res.getInt(1);
        } catch (SQLException e) {
            LoggingSingleton.getInstance().severe(e.getLocalizedMessage());
        }
        return 0;
    }
}
