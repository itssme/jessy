package view;

import logging.Logging;
import model.BoardModel;
import model.ScoreList;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-20
 * Project: jessy
 * Desc.:
 */
public class ChessGame extends JFrame {

    public static void play() {
        ChessGame game = new ChessGame();
        game.setVisible(true);
    }

    private ChessGame() {
        super("jessy");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            Logging.logToFile(Level.WARNING,
                    "A Class Not Fount exception occurred!");
        } catch (InstantiationException e) {
            Logging.logToFile(Level.WARNING,
                    "An Error occurred while instantiating the Class!");
        } catch (IllegalAccessException e) {
            Logging.logToFile(Level.WARNING,
                    "An IllegalAccessException occurred!");
        } catch (UnsupportedLookAndFeelException e) {
            Logging.logToFile(Level.WARNING,
                    "An unsupported Look and Feel exception occurred!");
        }
        Logging.logToFile(Level.INFO, "Generating GUI");
        this.setMinimumSize(new Dimension(1280, 720));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        this.add(this.createBoard(gbc), gbc);
        this.add(this.createScoreList(gbc), gbc);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private BoardModel createBoard(GridBagConstraints gbc) {
        Logging.logToFile(Level.INFO, "Creating the Gameboard");
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        return new BoardModel(8, 8);
    }

    private ScoreList<String> createScoreList(GridBagConstraints gbc) {
        Logging.logToFile(Level.INFO, "Creating The Target-Score-List");
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        return new ScoreList<>();
    }

}
