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
 * Desc.:
 */
public class LoggingSingleton {
    private static Logger logger;

    static {
        logger = Logger.getLogger("fileLogger");
        FileHandler fh = null;
        try {
            fh = new FileHandler("jessy.log.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        fh.setFormatter(new XMLFormatter());

        logger.addHandler(fh);
        logger.setUseParentHandlers(false);
    }

    public static Logger getInstance() {
        return logger;
    }

    private LoggingSingleton() {
    }
}
