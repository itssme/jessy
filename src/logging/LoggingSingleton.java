package logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.XMLFormatter;

/**
 * Author:  Königsreiter Simon
 * Class:   3CHIF
 * Date:    05.05.17
 * Poject:  jessy
 * Package: logging
 * Desc.:   The LoggingSingleton is a Class around the Logger class. It's only
 *          purpose is to get the reference to the Logger to enable logging
 *          for this project
 */
public class LoggingSingleton {
    /**
     * The logger which will be used for logging the Events
     */
    private static Logger logger;

    /**
     * A static block to initialize the logger, set the Formatter and set some
     * Variables
     */
    static {
        logger = Logger.getLogger("fileLogger");
        FileHandler fh = null;
        try {
            fh = new FileHandler("log/jessy.log.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        fh.setFormatter(new XMLFormatter());

        logger.addHandler(fh);
        logger.setUseParentHandlers(false);
    }

    /**
     * Returns the reference to the logger
     *
     * @return The Logger Object
     */
    public static Logger getInstance() {
        return logger;
    }

    /**
     * A private Constructor so nobody can create an Object of this class.
     */
    private LoggingSingleton() {
    }
}
