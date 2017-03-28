package view;

import logging.Logging;
import model.BoardModel;
import model.ScoreList;

import javax.swing.*;
import java.util.logging.Level;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-28
 * Project: jessy
 * Desc.:
 */
public class Chessgame {
    private JPanel mainPanel;
    private BoardModel board;
    private ScoreList scoreList;
    private JTextField chatTextInput;
    private JButton sendBTN;
    private JLabel chat;
    private JPanel boardContainer;

    private void createUIComponents() {
        this.board = new BoardModel(8, 8);
    }

    public static void main(String[] args) {
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
        JFrame frame = new JFrame("Jessy");
        Chessgame game = new Chessgame();
        game.createUIComponents();
        frame.setContentPane(game.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void printToChat(String player, String msg) {
        String txt = chat.getText();
        txt = String.format("%s %n" + "%s: %s", txt, player, msg);
        chat.setText(txt);
    }
}
