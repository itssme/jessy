package main;

import controllers.ConnectController;
import controllers.SendBTNController;
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

    public void sendBTNClicked(ActionEvent actionEvent) {
        SendBTNController.fireClick(chatTextBox, chat);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                chessBoard.setContent(new BoardModel(8, 8));
            }
        });
    }

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
                    ConnectController.connect("127.0.0.1", 5060, password);
                } catch (InvalidKeyException e1) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Invalid password");
                    return;
                }
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(
                        null,
                        "Could not start server");
                return;
            }

            JOptionPane.showMessageDialog(
                    null,
                    "Started the game");

            synchronized (server) {
                try {
                    server.wait();
                } catch (InterruptedException e1) {
                    LoggingSingleton.getInstance().error("Waiting for player connection failed -> starting game now!", e1);
                }
            }

            try {
                ConnectController.startFirst = connection.start();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            connection.start_thread();

            if (ConnectController.startFirst) {
                JOptionPane.showMessageDialog(
                        null,
                        "Connected: you start");
                //Chessgame.playerFactionWhite = true;
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Connected: opponent starts");
                //  Chessgame.playerFactionWhite = false;
            }

        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Server is already started");
        }
    }

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

    public static boolean connect(String ipAddress, int port, String password) throws InvalidKeyException {

        if (!IPAddressUtil.isIPv4LiteralAddress(ipAddress)) {
            JOptionPane.showMessageDialog(
                    null,
                    ipAddress + " is not a valid ip");
        } else {
            try {
                connection = new Connection(ipAddress, port, password);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(
                        null,
                        "Could not connect");
                return false;
            }
        }

        return true;
    }

    public static void disconnect() {
        JSONObject disconnectObj = new JSONObject();

        try {
            disconnectObj.put("disconnect", "true");
        } catch (JSONException e) {
            LoggingSingleton.getInstance().error("Failed to create disconnect object (not disconnecting)", e);
            return;
        }

        Connection.send_object(disconnectObj);

        while (!connection.gotDisconnect) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        connection = null;
    }

    public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            SendBTNController.fireClick(chatTextBox, chat);
        }
    }
}