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
 * Desc.:
 */
public class Utilities {

    private static boolean canPlay = true;
    private static ArrayList<PlayerSwitchObserver> playerSwitchObservers =
            new ArrayList<>();

    private static void addPlayerSwitchListener(PlayerSwitchObserver observer) {
        playerSwitchObservers.add(observer);
    }

    public static boolean canPlay() {
        return canPlay;
    }

    public static void switchPlayer() {
        canPlay = !canPlay;
        playerSwitchObservers.forEach(observer -> {
            observer.onPlayerSwitch(canPlay);
        });
    }

    static {

        addPlayerSwitchListener(new PlayerSwitchObserver() {
            @Override
            public void onPlayerSwitch(boolean canPlay) {
                if (CHESSGAMEBOARD.isMated()) {
                    if (!canPlay) {
                        JOptionPane.showMessageDialog(null, "You won!");
                        new Player(
                                Scorer.USERNAME,
                                (50 / Main.CHESSGAMEBOARD.getMoveCounter()) + 10
                        ).savePlayer();
                        new Player(
                                Scorer.OPONENT,
                                (50 / Main.CHESSGAMEBOARD.getMoveCounter()) - 10
                        ).savePlayer();
                    } else {
                        JOptionPane.showMessageDialog(null, "You lost!");
                        new Player(
                                Scorer.OPONENT,
                                (50 / Main.CHESSGAMEBOARD.getMoveCounter()) + 10
                        ).savePlayer();
                        new Player(
                                Scorer.USERNAME,
                                (50 / Main.CHESSGAMEBOARD.getMoveCounter()) - 10
                        ).savePlayer();
                    }
                }
                ChessGameController.getGameController().
                        getChessBoard().setDisable(!canPlay);
            }
        });
    }

}
