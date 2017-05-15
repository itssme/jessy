package view;

import controllers.ConnectController;
import controllers.HostController;
import controllers.SendBTNController;
import database.Scorer;
import logging.LoggingSingleton;
import model.BoardModel;
import model.ScoreList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-28
 * Project: jessy
 * Desc.:
 */
public class Chessgame extends WindowAdapter {

    private JPanel mainPanel;
    private BoardModel board;
    private ScoreList scoreList;
    private JTextField chatTextInput;
    private JButton sendBTN;
    private model.ChatModel chat;
    private JPanel boardContainer;
    private JMenuBar menuBar;
    private JPanel chatContainer;
    private JPanel chatBox;

    public Chessgame() {

        board.addMouseListener(board);
        sendBTN.addActionListener(new SendBTNController(chat, chatTextInput));
        sendBTN.setDefaultCapable(true);
        chatTextInput.addKeyListener(new SendBTNController(chat, chatTextInput));
    }

    private void createUIComponents() {
        this.board = new BoardModel(8, 8);

        this.menuBar = new JMenuBar();
        this.menuBar.add(this.genPlayMenu());

    }

    private JMenu genPlayMenu() {
        JMenu c = new JMenu("Play");

        JMenuItem connect = new JMenuItem("Connect to Game");
        connect.addActionListener(new ConnectController());

        JMenuItem host = new JMenuItem("Host a Game");
        host.addActionListener(new HostController());

        c.add(connect);
        c.add(host);

        return c;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        LoggingSingleton.getInstance().logToFile(
                Level.INFO,
                "Starting the logging Cleanup.");
        LoggingSingleton.getInstance().cleanUp();
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            LoggingSingleton.getInstance().logToFile(Level.WARNING,
                    "A Class Not Fount exception occurred!");
        } catch (InstantiationException e) {
            LoggingSingleton.getInstance().logToFile(Level.WARNING,
                    "An Error occurred while instantiating the Class!");
        } catch (IllegalAccessException e) {
            LoggingSingleton.getInstance().logToFile(Level.WARNING,
                    "An IllegalAccessException occurred!");
        } catch (UnsupportedLookAndFeelException e) {
            LoggingSingleton.getInstance().logToFile(Level.WARNING,
                    "An unsupported Look and Feel exception occurred!");
        }
        String username = JOptionPane.
                showInputDialog("Please type in your Player-Name");
        if (username == null) System.exit(0);
        Scorer.USERNAME = username;
        JFrame frame = new JFrame("Jessy");
        Chessgame game = new Chessgame();
        game.board.refresh();
        frame.setMinimumSize(new Dimension(1290, 740));
        frame.setContentPane(game.mainPanel);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(game);
        frame.pack();
        frame.setVisible(true);
    }
}
