package controllers;

import logging.LoggingSingleton;
import model.ChatModel;
import networking.Connection;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;


/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-04-03
 * Project: jessy
 * Desc.:
 */
public class SendBTNController implements ActionListener {

    private static ChatModel chat;
    private JTextField textField;

    public SendBTNController(ChatModel chat, JTextField textField) {
        this.chat = chat;
        this.textField = textField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String msg = textField.getText();
            textField.setText("");
            printToChat("You", msg);
            Connection.send_chat_msg(msg);
            LoggingSingleton.getInstance().logToFile(
                    Level.INFO,
                    "Send message: " + msg);
        } catch (NullPointerException e2) {
            LoggingSingleton.getInstance().logToFile(
                    Level.WARNING,
                    "Connection is not set up, could not send message");
            JOptionPane.showMessageDialog(
                    null,
                    "You are not connected to another player");
        }
    }

    public static void printToChat(String player, String msg) {
        String txt = chat.getText();
        txt = String.format("%s %n" + "%s: %s", txt, player, msg);
        chat.setText(txt);
    }
}