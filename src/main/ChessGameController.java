package main;

import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import logging.LoggingSingleton;
import model.BoardModel;
import networking.Connection;
import networking.Server;
import org.json.JSONException;
import org.json.JSONObject;
import sun.net.util.IPAddressUtil;

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
 * Desc.:
 */
public class ChessGameController implements Initializable {

    private static Server server = null;
    public static Connection connection = null;
    public static boolean startFirst;

    @FXML
    public TextArea chat;
    @FXML
    public TextField chatTextBox;
    @FXML
    private SwingNode chessBoard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                chessBoard.setContent(new BoardModel(8, 8));
            }
        });
    }

    /**
     * Starts the <code>Server</code> and the <code>Connection</code>
     *
     * @param actionEvent the button event
     */
    public void hostGame(ActionEvent actionEvent) {
        if (server == null) {
            try {
                String password = JOptionPane.showInputDialog(
                        null,
                        "Do you want to encrypt your game with a password?",
                        "password");

                if (password.length() <= 4) {
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
                    return;
                }
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(
                        null,
                        "Could not start server\n" + e1.toString());
                return;
            }

            JOptionPane.showMessageDialog(
                    null,
                    "Started the game");

            synchronized (server) {
                try {
                    server.wait();
                } catch (InterruptedException e1) {
                    LoggingSingleton.getInstance().severe(
                            "Waiting for player connection failed -> " +
                                    "starting game now!" +
                                    e1.getLocalizedMessage());
                }
            }

            try {
                startFirst = connection.start();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            connection.start_thread();

            if (startFirst) {
                JOptionPane.showMessageDialog(
                        null,
                        "Connected: you start");
                //Chessgame.playerFactionWhite = true;
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Connected: opponent starts");
                //Chessgame.playerFactionWhite = false;
            }

        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Server is already started");
        }
    }

    /**
     * Starts the <code>Connection</code> if a button if pressed
     *
     * @param actionEvent the button event
     */
    public void connectToGame(ActionEvent actionEvent) {
        if (connection == null) {

            String ipAddress = JOptionPane.showInputDialog(
                    null,
                    "Type in the ip:",
                    "192.168.1.100");

            String password = JOptionPane.showInputDialog(
                    null,
                    "Do you want to encrypt your game with a password?",
                    "password");

            if (password.length() <= 4) {
                JOptionPane.showMessageDialog(
                        null,
                        "Weak or no password.\nInsecure connection!");
            }

            try {
                connect(ipAddress, 5060, password);
            } catch (InvalidKeyException e1) {
                JOptionPane.showMessageDialog(
                        null,
                        "Invalid password");
                return;
            }

            try {
                startFirst = connection.start();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            connection.start_thread();

            if (startFirst) {
                JOptionPane.showMessageDialog(
                        null,
                        "Connected: you start");
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Connected: opponent starts");
            }

        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Your are already connected to a game");
        }
    }

    /**
     * Connects to a server
     *
     * @param ipAddress the ip address of the server
     * @param port the port the server listens on
     * @param password the password for the connection
     * @return <code>true</code> if the connection was a success
     *         and <code>false</code> if the connection failed
     * @throws InvalidKeyException the password is not valid
     */
    public boolean connect(String ipAddress, int port, String password)
            throws InvalidKeyException {

        if (! IPAddressUtil.isIPv4LiteralAddress(ipAddress)) {
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

        return true;
    }

    /**
     * Disconnects form the server and closes all networkstreams
     */
    public void disconnect() {
        printToChat("Server", "other player disconnected -> stopping game");

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
    public static void closeServer() {
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
        String txt = String.format("%s %n" + "%s: %s", chat.getText(), user, msg);
        chat.setText(txt);
        chatTextBox.setText("");
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
            Connection.send_chat_msg(msg);
            printToChat("You", msg);
        }
    }

    /**
     * The actionHandler for the 'send' Button
     *
     * @param actionEvent The Button-click
     */
    public void sendBTNClicked(ActionEvent actionEvent) {
        String msg = chatTextBox.getText();
        Connection.send_chat_msg(msg);
        printToChat("You", msg);
    }
}
