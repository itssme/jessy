package logging;

import java.io.IOException;
import java.util.logging.FileHandler;
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
    private static Logging logObj;

    static {
        try {
            logObj = new Logging().initLogging(
                    new FileHandler("jessy.log.xml"),
                    new XMLFormatter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logging getInstance() {
        return logObj;
    }

    private LoggingSingleton() {
    }
}
