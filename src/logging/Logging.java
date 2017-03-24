package logging;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-21
 * Project: jessy
 * Desc.:
 */
public class Logging {

    private static Logger logObj = Logger.getLogger("FileLogger");
    private static final Level minLogLevel = Level.ALL;

    public static void initLogging(FileHandler fh) {
        fh.setFormatter(new SimpleFormatter());
        logObj.addHandler(fh);
        logObj.setUseParentHandlers(false);
        logObj.log(Level.INFO, "Successfully created the Logging Object!");
    }

    public static void logToFile(Level lvl, String msg) {
        filterLog(lvl, msg);
    }

    private static void filterLog(Level lvl, String msg) {
        if (lvl.intValue() >= minLogLevel.intValue()) {
            logObj.log(lvl, msg);
        }
    }

}
