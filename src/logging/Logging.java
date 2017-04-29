package logging;

import java.util.logging.*;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-21
 * Project: jessy
 * Desc.:
 */
public class Logging {

    private static Logger logObj = Logger.getLogger("FileLogger");
    private static final Level MINLEVEL = Level.ALL;

    public static void initLogging(FileHandler fh) {
        fh.setFormatter(new SimpleFormatter());
        logObj.setLevel(MINLEVEL);
        logObj.addHandler(fh);
        logObj.setUseParentHandlers(false);
        logObj.log(Level.INFO, "Successfully created the Logging Object!");
    }

    public static void logToFile(Level lvl, String msg) {
        logObj.log(lvl, msg);
    }

    public static void addFileHandle(FileHandler fh) {
        fh.setFormatter(new SimpleFormatter());
        logObj.addHandler(fh);
    }

    public static Handler[] getFileHandlers() {
        return logObj.getHandlers();
    }

    public static void cleanUp() {
        Handler[] handlers = getFileHandlers();
        for (Handler h :
                handlers) {
            h.flush();
            h.close();
        }
    }
}
