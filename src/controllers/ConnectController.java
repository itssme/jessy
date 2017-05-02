package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import networking.Connection;
import sun.net.util.IPAddressUtil;

import javax.swing.*;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-04-04
 * Project: jessy
 * Desc.:
 */
public class ConnectController implements ActionListener {

    public static Connection connection = null;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (connection == null) {
            String ipAddress = JOptionPane.showInputDialog(null, "Type in the ip:", "192.168.1.100");
            connect(ipAddress, 5060);
        } else {
            JOptionPane.showMessageDialog(null, "Your are already connected to a game");
        }
    }

    public static boolean connect(String ipAddress, int port) {
        if (! IPAddressUtil.isIPv4LiteralAddress(ipAddress)) {
            JOptionPane.showMessageDialog(null, ipAddress + " is not a valid ip");
        } else {
            try {
                connection = new Connection(ipAddress, port);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "Could not connect");
                return false;
            }
        }

        return true;
    }
}
