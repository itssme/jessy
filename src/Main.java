import database.Scorer;
import logging.Logging;
import view.Chessgame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.FileHandler;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:
 */
public class Main {

    public static void main(String[] args) {
        FileHandler fh;
        try {
            fh = new FileHandler("jessy.log");
            Logging.initLogging(fh);
            Chessgame.main(args);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't create the log-File!");
        }
    }
}
