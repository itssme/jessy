import view.Chessgame;

/**
 * Name:    Königsreiter Simon
 * Class:   3CHIF
 * Date:    2017-03-17
 * Project: jessy
 * Desc.:
 */
public class Main {

    public static void main(String[] args) {
        try {
            Class.forName("logging.LoggingSingleton");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Chessgame.main(args);
    }
}
