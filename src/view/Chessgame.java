package view;

import controllers.SendBTNController;
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
    private model.ChatModel chat;
    private JPanel boardContainer;
    private JToolBar menuBar;
    private JPanel chatContainer;
    private JPanel chatBox;

    public Chessgame() {

        board.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JTable target = (JTable)evt.getSource();
                int row = target.getSelectedRow();
                int col = target.getSelectedColumn();

                System.out.println(row + ":" + col);

            }
        });
        sendBTN.addActionListener(new SendBTNController(chat, chatTextInput));
    }

    private void createUIComponents() {
        this.board = new BoardModel(8, 8);

        this.menuBar = new JToolBar();

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
}
