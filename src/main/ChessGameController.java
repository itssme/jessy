package main;

import board.Move;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import logging.ChessSaver;
import logging.LoggingSingleton;
import model.BoardModel;
import model.Player;
import model.ScoreList;
import networking.Connection;
import networking.Server;
import org.json.JSONException;
import org.json.JSONObject;
import sound.Sound;
import sun.net.util.IPAddressUtil;
import utils.Utilities;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.util.ResourceBundle;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    17.05.17
 * Poject:  jessy_jfx
 * Package: main
 * Desc.:   The GameController for the User Interface.
 */
public class ChessGameController implements Initializable {

    private static Server server = null;
    private boolean canPlay = false;
    public static Connection connection = null;
    private static String startFirst;
    private BoardModel model = new BoardModel(8, 8);
    private static ChessGameController reference;
    private boolean creatingConnection = false;

    @FXML
    private TextArea chat;
    @FXML
    private TextField chatTextBox;
    @FXML
    private SwingNode chessBoard;
    @FXML
    private ListView<String> targetList;

    public SwingNode getChessBoard() {
        return chessBoard;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SwingUtilities.invokeLater(() -> chessBoard.setContent(model));
        reference = this;
        new ScoreList<Player>(FXCollections.observableArrayList(), targetList);

    }

    /**
     * GetGameController returns the class-Reference of this particular
     * Controller for clean-ups.
     *
     * @return The GameController
     */
    public static ChessGameController getGameController() {
        return reference;
    }

    /**
     * Starts the <code>Server</code> and the <code>Connection</code>
     *
     */
    public synchronized void hostGame() {
        new Thread(() -> {
            if (server == null && ! creatingConnection) {
                creatingConnection = true;

                try {
                    String password = JOptionPane.showInputDialog(
                            null,
                            "Do you want to encrypt your game with a password?",
                            "password");
                    if (password == null) {
                        creatingConnection = false;
                        return;
                    } else if (password.length() <= 4) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Weak or no password.\nInsecure connection!");
                    }

                    LoggingSingleton.getInstance().info("server starting");
                    server = new Server(5060);
                    server.start();
                    LoggingSingleton.getInstance().info("server starting done");

                    try {

                        connect("127.0.0.1", 5060, password);
                    } catch (InvalidKeyException e1) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Invalid password");
                        creatingConnection = false;
                        return;
                    }
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Could not start server\n" + e1.toString());
                    creatingConnection = false;
                    return;
                }

                JOptionPane.showMessageDialog(
                        null,
                        "Started the game");

                synchronized (server) {
                    printToChat("Server", "Started game, waiting for players to connect.");
                    try {
                        server.wait();
                    } catch (InterruptedException e1) {
                        LoggingSingleton.getInstance().severe(
                                "Waiting for player connection failed -> " +
                                        "starting game now!" +
                                        e1.getLocalizedMessage());
                    }
                }

                if (Main.CHESSGAMEBOARD.getSideToMove().equals(Side.BLACK)) {
                    startFirst = "false";
                } else {
                    startFirst = "true";
                }

                if (startFirst.equals("true")) {
                    connection.sendStart(false);
                } else {
                    connection.sendStart(true);
                }

                connection.getEncrypt();

                if (System.getProperty("os.name").equalsIgnoreCase("linux")) {
                    connection.sendEncrypt(true);
                } else {
                    connection.sendEncrypt(false);
                }

                connection.start_thread();
                Connection.sendGameState(Main.CHESSGAMEBOARD.getFEN(true));

                if (startFirst.equals("true")) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Connected: you start");
                    printToChat("Server", "You start first");
                } else if (startFirst.equals("false")){
                    JOptionPane.showMessageDialog(
                            null,
                            "Connected: opponent starts");
                    Utilities.switchPlayer();
                    printToChat("Server", "opponent starts first");
                }

                creatingConnection = false;

            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Server is already started");
            }
        }).start();
    }


    /**
     * Starts the <code>Connection</code> if a button is pressed
     *
     */
    public synchronized void connectToGame() {
        new Thread(() -> {
            if (connection == null && ! creatingConnection) {
                creatingConnection = true;

                String ipAddress = "";
                String password  = "";

                try {

                    ipAddress = JOptionPane.showInputDialog(
                            null,
                            "Type in the ip:",
                            "192.168.1.100");

                    if (ipAddress == null) {
                        creatingConnection = false;
                        return;
                    }

                    password = JOptionPane.showInputDialog(
                            null,
                            "Do you want to encrypt your game with a password?",
                            "password");

                    if (password.length() <= 4) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Weak or no password.\nInsecure connection!");
                    }
                } catch (Exception e) {
                    LoggingSingleton.getInstance().severe(e.getMessage());
                    creatingConnection = false;
                    return;
                }

                try {
                    if (! connect(ipAddress, 5060, password)) {
                        throw new IOException();
                    }

                } catch (InvalidKeyException e1) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Invalid password");
                    creatingConnection = false;
                    return;
                } catch (IOException e) {
                    LoggingSingleton.getInstance().severe("Failed to connect " + e.getMessage());
                    creatingConnection = false;
                    return;
                }

                if (connection == null) {
                    LoggingSingleton.getInstance().severe("Could not create connection");
                    creatingConnection = false;
                    return;
                }

                startFirst = connection.getStart();

                if (System.getProperty("os.name").equalsIgnoreCase("linux")) {
                    connection.sendEncrypt(true);
                } else {
                    connection.sendEncrypt(false);
                }

                connection.getEncrypt();

                connection.start_thread();

                if (startFirst.equals("true")) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Connected: you start");
                    printToChat("Server", "You start first");
                    Utilities.switchPlayer();
                } else if (startFirst.equals("false")){
                    JOptionPane.showMessageDialog(
                            null,
                            "Connected: opponent starts");
                    printToChat("Server", "opponent starts first");
                    Utilities.switchPlayer();
                } else if (startFirst.equals("true_")) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Connected: you start");
                    printToChat("Server", "You start first");
                } else if (startFirst.equals("false_")) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Connected: you start");
                    printToChat("Server", "You start first");
                    Utilities.switchPlayer();
                }

                creatingConnection = false;
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Your are already connected to a game");
            }
        }).start();
    }

    /**
     * Connects to a server
     *
     * @param ipAddress the ip address of the server
     * @param port      the port the server listens on
     * @param password  the password for the connection
     * @return <code>true</code> if the connection was a success
     * and <code>false</code> if the connection failed
     * @throws InvalidKeyException the password is not valid
     */
    private boolean connect(String ipAddress, int port, String password)
            throws InvalidKeyException {

        if (!IPAddressUtil.isIPv4LiteralAddress(ipAddress)) {
            JOptionPane.showMessageDialog(
                    null,
                    ipAddress + " is not a valid ip");
        } else {
            try {
                connection = new Connection(ipAddress, port, password, this);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(
                        null,
                        "Could not connect");
                return false;
            }
        }
        if (!Utilities.canPlay()) Utilities.switchPlayer();
        canPlay = true;
        return true;
    }

    /**
     * Disconnects form the server and closes all networkstreams
     */
    public void disconnect() {
        printToChat("Server", "player disconnected -> stopping game");

        if (connection != null) {
            JSONObject disconnectObj = new JSONObject();

            try {
                disconnectObj.put("disconnect", "true");
            } catch (JSONException e) {
                LoggingSingleton.getInstance().severe("Failed to create disconnect object (not disconnecting)" + e.getLocalizedMessage());
                return;
            }

            Connection.sendObject(disconnectObj);
            closeServer();

            connection.reset();
            connection = null;
        }
    }

    /**
     * Closes the connections of the server and stops it
     */
    private static void closeServer() {
        if (server != null) {
            server.close();
            server = null;
        }
    }

    /**
     * Writes the text with the user, who sent the text to the ChatBox.
     *
     * @param user The user who sent the text. 'You' if it was sent by the local
     *             user
     * @param msg  The message sent.
     */
    public void printToChat(String user, String msg) {
        chat.appendText("\n" + user + ": " + msg);
        chat.selectAll();
        chat.deselect();
    }

    /**
     * The keyTyped event which is the keyboard-handler for the Text-Area
     *
     * @param keyEvent The key-input for the Text-Area
     */
    @FXML
    public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getCode().compareTo(KeyCode.ENTER) == 0) {
            String msg = chatTextBox.getText();
            if (!msg.equals("")) {
                Connection.send_chat_msg(msg);
                printToChat("You", msg);
                chatTextBox.setText("");
            }
        }
    }

    /**
     * The actionHandler for the 'send' Button
     */
    public void sendBTNClicked() {
        String msg = chatTextBox.getText();
        if (!msg.equals("")) {
            Connection.send_chat_msg(msg);
            printToChat("You", msg);
            chatTextBox.setText("");
        }
    }

    /**
     * Disconnects from the opponent and closes the application
     */
    public void quitGame() {
        this.disconnect();
        System.exit(0);
    }

    /**
     * The mouseClick for the JTable. Just calls the mouseClick event of the
     * model.
     */
    public void mouseClick() {
        try {
            model.mouseClick();
        } catch (IllegalArgumentException e) {
            LoggingSingleton.getInstance().info("Mouse click error " + e.getMessage());
        }
    }

    /**
     * Executes a move for the Board
     *
     * @param mv The move to execute
     */
    public void executeMove(com.github.bhlangonijr.chesslib.move.Move mv) {
        if (canPlay) {
            model.setSelected(Piece.NONE);
            model.setSelectedStartSquare(Square.NONE);
            model.setMadeMove(Move.getMoveFromLib(mv));
            Main.CHESSGAMEBOARD.doMove(mv);
            BoardModel.refresh();
            new Sound().playSound("chessMove1.wav");
            Utilities.switchPlayer();
        }
    }

    /**
     * Saves the Game and quits
     * @return Returns the result of writeToXML of ChessSaver
     */
    public boolean saveGame() {
        boolean writeRes = ChessSaver.getInstance().
                saveGame(Main.CHESSGAMEBOARD.getFEN(true)).
                writeToXML("jessy.sav.xml");
        this.quitGame();
        return writeRes;
    }

    /**
     * Loads a Game
     */
    public void loadGame() {
        ChessSaver.getInstance().loadGame();
    }
}
