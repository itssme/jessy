package controllers;

import networking.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.InvalidKeyException;

import static controllers.ConnectController.connection;


/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-04-04
 * Desc.:
 */
public class HostController implements ActionListener {

    public static Server server = null;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (server == null) {
            try {
                String password = JOptionPane.showInputDialog(
                        null,
                        "Do you want to encrypt your game with a password?",
                        "password");

                if (password.length() <= 4) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Weak or no password.\nInsecure connectoin!");
                }

                System.out.println("server starting");
                server = new Server(5060);
                server.start();
                System.out.println("server starting done");
                try {
                    ConnectController.connect("127.0.0.1", 5060, password); // get password
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


            while (! server.all_connected()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            server.starter();

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
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Connected: opponent starts");
            }

        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Server is already started");
        }
    }
}
