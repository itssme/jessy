package main;

import com.github.bhlangonijr.chesslib.Board;
import database.Scorer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    17.05.17
 * Poject:  jessy_jfx
 * Package: main
 * Desc.:
 */
public class Main extends Application {

    /**
     * The Chess-Board for this round. It is public and static because it has
     * to be always accessible and may never be overridden.
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
        Scene scene = new Scene(root);
        stage.setTitle("jessy - " + Scorer.USERNAME);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * The main-entry point of the Program. It initializes some Classes and
     * asks the Player for his name.
     * @param args The command-line arguments
     */
    public static void main(String[] args) {
        try {
            Class.forName("logging.LoggingSingleton");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String userName = JOptionPane.showInputDialog("What's your username?");
        if (userName == null || userName.equals("")) {
            JOptionPane.showMessageDialog(null,
                    "Your username can't be empty!", "Warning",
                    JOptionPane.CANCEL_OPTION);
            System.exit(0);
        } else {
            Scorer.USERNAME = userName;
        }
        launch(args);
    }

}
