package utils;

import database.Scorer;
import main.ChessGameController;
import main.Main;
import model.Player;

import javax.swing.*;
import java.util.ArrayList;

import static main.Main.CHESSGAMEBOARD;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    19.05.17
 * Poject:  jessy
 * Package: utils
 * Desc.:   A simple Utility class which handles setting of player-switches and
 *          the corresponding listeners.
 */
public class Utilities {

    /**
     * A simple boolean variable to indicate if a player can play
     */
    private static boolean canPlay = true;
    /**
     * An ArrayList containing all the playerSwitchListeners
     */
    private static ArrayList<PlayerSwitchObserver> playerSwitchObservers =
            new ArrayList<>();

    /**
     * Adds a new Listener to the listener-Array.
     *
     * @param observer The PlayerSwitchObserver which will handle a playerSwitch
     */
    private static void addPlayerSwitchListener(PlayerSwitchObserver observer) {
        playerSwitchObservers.add(observer);
    }

    /**
     * A boolean operation which indicates whether a player can play or not
     * @return True, if the player can play, false otherwise
     */
    public static boolean canPlay() {
        return canPlay;
    }

    /**
     * A simple method which changes canPlay and executes all the listeners
     */
    public static void switchPlayer() {
        canPlay = !canPlay;
        playerSwitchObservers.forEach(observer -> {
            observer.onPlayerSwitch(canPlay);
        });
    }

    static {

        /**
         * Adds the default PlayerSwitchObserver to the listeners
         */
        addPlayerSwitchListener(new PlayerSwitchObserver() {
            @Override
            public void onPlayerSwitch(boolean canPlay) {
                if (CHESSGAMEBOARD.isMated()) {
                    if (!canPlay) {
                        JOptionPane.showMessageDialog(null, "You won!");
                        new Player(
                                Scorer.USERNAME,
                                (50 / Main.CHESSGAMEBOARD.getHalfMoveCounter()) + 10
                        ).savePlayer();
                        new Player(
                                Scorer.OPPONENT,
                                (50 / Main.CHESSGAMEBOARD.getHalfMoveCounter()) - 10
                        ).savePlayer();
                    } else {
                        JOptionPane.showMessageDialog(null, "You lost!");
                        new Player(
                                Scorer.OPPONENT,
                                (50 / Main.CHESSGAMEBOARD.getHalfMoveCounter()) + 10
                        ).savePlayer();
                        new Player(
                                Scorer.USERNAME,
                                (50 / Main.CHESSGAMEBOARD.getHalfMoveCounter()) - 10
                        ).savePlayer();
                    }
                }
                ChessGameController.getGameController().
                        getChessBoard().setDisable(!canPlay);
            }
        });
    }

}
