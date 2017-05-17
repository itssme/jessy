package controllers;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import logging.LoggingSingleton;
import networking.Connection;

import javax.swing.*;
import java.util.logging.Level;


/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-04-03
 * Project: jessy
 * Desc.:
 */
public class SendBTNController {


    public static void fireClick(TextField textField, TextArea chat) {
        try {
            String msg = textField.getText();
            textField.setText("");
            printToChat("You", msg, chat);
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

    public static void printToChat(String player, String msg, TextArea chat) {
        String txt = chat.getText();
        txt = String.format("%s %n" + "%s: %s", txt, player, msg);
        chat.setText(txt);
    }


}