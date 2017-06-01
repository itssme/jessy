package networking;

import main.ChessGameController;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidKeyException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-05-19
 * Project: jessy
 * Desc.:
 */
class ConnectionTest {

    @Nullable
    public static Server startServer() {
        Server server = null;

        try {
            server = new Server(5060);
        } catch (IOException e) {
            return null;
        }

        return server;
    }

    @Nullable
    public static Connection connect(ChessGameController controller) {
        Connection connection = null;

        try {
            connection = new Connection("127.0.0.1", 5060, "sicheresPasswort", controller);
        } catch (IOException e) {
            return null;
        } catch (InvalidKeyException e) {
            return null;
        }

        return connection;
    }

    @Test
    void sendMoveTest0() {
        ChessGameController controller1 = new ChessGameController();
        ChessGameController controller2 = new ChessGameController();

        Server server = startServer();

        if (server == null) {
            assertTrue(false);
        }

        server.start();

        Connection connection1 = connect(controller1);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Connection connection2 = connect(controller2);

        if (connection1 == null || connection2 == null) {
            assertTrue(false);
        }

        connection2.sendStart(true);
        connection1.getStart();

        System.out.println("starting done");

        JSONObject send = new JSONObject();
        try {
            send.put("test1", "testing1");
            send.put("test2", "testing2");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        connection2.sendObject(send);

        System.out.println("sent object");

        JSONObject get = null;
        try {
            System.out.println("waiting for object");
            get = connection1.get_object();
            System.out.println("connection 1 got");
        } catch (Exception e) {
            assertTrue(false);
        }

        System.out.println(get.toString());
        System.out.println(send.toString());

        assertTrue(get.toString().equals(send.toString()));

        server.close();
    }

    @Test
    void startingTest() {
        ChessGameController controller1 = new ChessGameController();
        ChessGameController controller2 = new ChessGameController();

        Server server = startServer();

        if (server == null) {
            assertTrue(false);
        }

        server.start();

        Connection connection1 = connect(controller1);

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Connection connection2 = connect(controller2);

        if (connection1 == null || connection2 == null) {
            assertTrue(false);
        }

        connection2.sendStart(true);
        assertTrue(connection1.getStart().equals("true"));
    }
}