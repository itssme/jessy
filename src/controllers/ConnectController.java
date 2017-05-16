package controllers;

import logging.LoggingSingleton;
import networking.Connection;
import networking.Encrypter;
import org.json.JSONException;
import org.json.JSONObject;
import sun.net.util.IPAddressUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-04-04
 * Project: jessy
 * Desc.:
 */
public class ConnectController implements ActionListener {

    public static Connection connection = null;
    public static boolean startFirst;

    @Override
    public void actionPerformed(ActionEvent e) {

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

        if (! IPAddressUtil.isIPv4LiteralAddress(ipAddress)) {
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

        while (! connection.gotDisconnect) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        connection = null;
    }
}
