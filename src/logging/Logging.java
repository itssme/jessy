package logging;

import java.util.logging.*;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-21
 * Project: jessy
 * Desc.:
 */
public class Logging implements jdk.internal.instrumentation.Logger {

    private Logger logObj = Logger.getLogger("FileLogger");
    private static final Level MINLEVEL = Level.ALL;

    public Logger getLogObj() {
        return logObj;
    }

    protected static Logging initLogging(FileHandler fh, Formatter formatter) {
        Logging log = new Logging();
        fh.setFormatter(formatter);
        log.getLogObj().setLevel(MINLEVEL);
        log.getLogObj().addHandler(fh);
        log.getLogObj().setUseParentHandlers(false);
        log.getLogObj().log(Level.INFO,
                "Successfully created the Logging Object!");
        return log;
    }

    public void logToFile(Level lvl, String msg) {
        logObj.log(lvl, msg);
    }

    public void addFileHandle(FileHandler fh) {
        fh.setFormatter(new SimpleFormatter());
        logObj.addHandler(fh);
    }

    public Handler[] getFileHandlers() {
        return logObj.getHandlers();
    }

    public void cleanUp() {
        Handler[] handlers = getFileHandlers();
        for (Handler h :
                handlers) {
            h.flush();
            h.close();
        }
    }

    @Override
    public void error(String s) {
        logToFile(Level.SEVERE, s);
    }

    @Override
    public void warn(String s) {
        logToFile(Level.WARNING, s);
    }

    @Override
    public void info(String s) {
        logToFile(Level.INFO, s);
    }

    @Override
    public void debug(String s) {
        logToFile(Level.ALL, s);
    }

    @Override
    public void trace(String s) {
        logToFile(Level.ALL, s);
    }

    @Override
    public void error(String s, Throwable throwable) {
        logToFile(Level.SEVERE, s + ": " + throwable.getLocalizedMessage());
    }
}
