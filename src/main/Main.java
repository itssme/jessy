package main;

import com.github.bhlangonijr.chesslib.Board;
import database.Scorer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logging.ChessSaver;
import logging.LoggingSingleton;
import model.Player;
import networking.Server;
import utils.Utilities;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    17.05.17
 * Poject:  jessy_jfx
 * Package: main
 * Desc.:   The main Class for the Program. It startes the UI, loads the FXML
 *          and Initializes the Logging.
 */
public class Main extends Application {

    /**
     * The Chess-Board for this round. It is public, static and static because 
     * it has to be always accessible and may never be overridden.
     */
    public static final Board CHESSGAMEBOARD = new Board();

    /**
     * The start-point of the GUI. This class has to be implemented as
     * specified by <strong>javafx.Application</strong>.
     *
     * @param stage The stage, in which we can play.
     */
    public void start(Stage stage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("chessGame.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert root != null;
        Scene scene = new Scene(root);
        stage.setTitle("jessy - " + Scorer.USERNAME);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * The stop method which will be called at the end of the JavaFX Lifecycle
     *
     * @throws Exception In case of an Exception in super.stop()
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        try {
            ChessGameController.getGameController().disconnect();
        } catch (Exception e) {
            LoggingSingleton.getInstance().warning("Failed to disconnect (probably not connected " + e.getMessage());
        }
        System.exit(0);
    }


    @Override
    public void init() throws Exception {
        super.init();
        /*
          Adds the default PlayerSwitchObserver to the listeners
         */
        Utilities.addPlayerSwitchListener(canPlay -> {
            if (CHESSGAMEBOARD.isMated()) {
                if (!canPlay) {
                    JOptionPane.showMessageDialog(null, "You won!");
                    new Player(
                            Scorer.USERNAME,
                            (50 / CHESSGAMEBOARD.getHalfMoveCounter()) + 10
                    ).savePlayer();
                    new Player(
                            Scorer.OPPONENT,
                            (50 / CHESSGAMEBOARD.getHalfMoveCounter()) - 10
                    ).savePlayer();
                } else {
                    JOptionPane.showMessageDialog(null, "You lost!");
                    new Player(
                            Scorer.OPPONENT,
                            (50 / CHESSGAMEBOARD.getHalfMoveCounter()) + 10
                    ).savePlayer();
                    new Player(
                            Scorer.USERNAME,
                            (50 / CHESSGAMEBOARD.getHalfMoveCounter()) - 10
                    ).savePlayer();
                }
            }
            ChessGameController.getGameController().
                    getChessBoard().setDisable(!canPlay);
        });

        Utilities.addPlayerSwitchListener(canPlay -> {
            if (CHESSGAMEBOARD.isMated()) {
                ChessGameController.getGameController().printToChat(
                        "Server",
                        "A player is Mated!"
                );
            }
            if (CHESSGAMEBOARD.isDraw()) {
                ChessGameController.getGameController().printToChat(
                        "Server",
                        "A Draw happened"
                );
                JOptionPane.showMessageDialog(null,
                        "A draw happened");
                new Player(
                        Scorer.USERNAME,
                        25
                ).savePlayer();
                new Player(
                        Scorer.OPPONENT,
                        25
                ).savePlayer();
                ChessGameController.getGameController().disconnect();

            }
            if (CHESSGAMEBOARD.isKingAttacked()) {
                ChessGameController.getGameController().printToChat(
                        "Server",
                        "A king is under attack"
                );
            }
            if (CHESSGAMEBOARD.isStaleMate()) {
                ChessGameController.getGameController().printToChat(
                        "Server",
                        "A Stale is mate"
                );
            }
        });
    }

    /**
     * The main-entry point of the Program. It initializes some Classes and
     * asks the Player for his name.
     *
     * @param args The command-line arguments
     */
    public static void main(String[] args) {
        File db = new File("db");
        File log = new File("log");
        db.mkdir();
        log.mkdir();
        try {
            Class.forName("logging.LoggingSingleton");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (args.length != 0 && (args[0].equals("--server-only") || args[0].equals("-so"))) {
            System.out.println("[!] starting in server only mode!");
            LoggingSingleton.getInstance().info("server only mode (only server will be started)");

            Server server = null;

            try {
                server = new Server(5060);
            } catch (IOException e) {
                LoggingSingleton.getInstance().severe("Failed to start server " + e.getMessage());
                System.err.print("[!] Error starting server. Make sure port 5060 is not blocked or used");
                System.out.println("\n[!] exit with error");
                return;
            }

            System.out.println("[!] waiting for connections");
            server.startSilent();
            System.out.println("[!] all players connected game is starting");

            server.player1.sendStart("true_");
            server.player2.sendStart("false_");

            System.out.println("send start");

            boolean encryptionPl1 = server.player1.getEncrypt();
            boolean encryptionPl2 = server.player2.getEncrypt();

            System.out.println("got encryption");
            System.out.println(encryptionPl1);
            System.out.println(encryptionPl2);

            if (encryptionPl1 && encryptionPl2) {
                server.player1.sendEncrypt(true);
                server.player2.sendEncrypt(true);
            } else {
                server.player1.sendEncrypt(false);
                server.player2.sendEncrypt(false);
            }

            System.out.println("send encryption");

            server.startPlayerThreads();

            while (server.all_connected()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("[!] one player disconnected or game is over -> stopping server");
            server.close();
            System.out.println("[!] done");

        } else {

            String userName = JOptionPane.showInputDialog("What's your username?");
            if (userName == null || userName.equals("")) {
                JOptionPane.showMessageDialog(null,
                        "Your username can't be empty!", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            } else {
                Scorer.USERNAME = userName;
            }
            ChessSaver.getInstance().init(CHESSGAMEBOARD.getFEN());

            launch(args);
        }
    }
}
