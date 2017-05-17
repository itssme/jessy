package main;

import com.github.bhlangonijr.chesslib.Board;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    public void start(Stage stage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("chessGame.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setTitle("jessy");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        Board board = new Board();
        try {
            Class.forName("logging.LoggingSingleton");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(board.toString());
        launch(args);
    }

}
