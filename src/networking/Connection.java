package networking;

import board.Move;
import board.Position;
import controllers.ConnectController;
import logging.Logging;
import logging.LoggingSingleton;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.logging.Level;

import static controllers.SendBTNController.printToChat;
import static database.Scorer.USERNAME;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-21
 * Project: jessy
 * Desc.:   One connection which is sending and receiving json objects
 */
public class Connection implements Runnable {

    private Socket self;
    private BufferedReader br;
    private boolean sending = false;
    private static Move last_obj;
    private static PrintWriter pw;
    private Thread this_thread;
    private static Encrypter encrypter;

    /**
     * Opens the connection to the server
     *
     * @param connect_to_ip ip of the server
     * @param port ports of the server
     * @param password password for the connection
     * @throws IOException network error
     * @throws InvalidKeyException invalid password
     */
    public Connection(String connect_to_ip, int port, String password) throws IOException, InvalidKeyException {
        self = new Socket(connect_to_ip, port);
        br = new BufferedReader(new InputStreamReader(self.getInputStream()));
        pw = new PrintWriter(self.getOutputStream(), true);
        encrypter = new Encrypter(password);
    }

    /**
     * Starts this thread
     */
    public void start_thread() {
        if (this_thread == null) {
            this_thread = new Thread(this);
            this_thread.start();
        }
    }

    /**
     * Calls <code>getObject()</code> and parses the object
     */
    @Override
    public void run() {
        while (true) {
            try {
                JSONObject obj = get_object();

                if (obj != null) {
                    if (obj.has("chat")) {

                        String msg = obj.getString("chat");
                        String player = obj.getString("player");
                        //printToChat(player, msg);

                        LoggingSingleton.getInstance().logToFile(Level.INFO, "GOT CHAT: " + player + " " + msg);

                    } else if (obj.has("resend")) {
                        send_move(last_obj);

                    } else if (obj.has("from x")) {
                        LoggingSingleton.getInstance().logToFile(Level.INFO, "GOT " + "got position object");

                        Position from = new Position(obj.getInt("from x"), obj.getInt("from y"));
                        Position to = new Position(obj.getInt("to x"), obj.getInt("to y"));

                        Move move = new Move(from, to);

                        LoggingSingleton.getInstance().logToFile(Level.INFO, "GOT: " + move.toJsonObject().toString());

                        // TODO: validate (move)

                    } else if (obj.has("disconnect")) {
                        if (obj.getString("disconnect").equals("true")) {
                            LoggingSingleton.getInstance().info("got disconnect object");
                            ConnectController.disconnect();
                        }

                        this_thread.stop();
                        return;
                    } else {
                        LoggingSingleton.getInstance().logToFile(Level.WARNING, "GOT: not a valid object " + obj.toString());
                    }
                }
            } catch (IOException e) {
                LoggingSingleton.getInstance().error("Failed to send json object", e);
            } catch (JSONException e) {
                LoggingSingleton.getInstance().error("Failed to create json object", e);
            }
        }
    }


    /**
     * Converts a <code>Move</code> Object to a <code>JSONObject</code>
     * and then passes it onto <code>sendObject()</code>.
     *
     * @param move an object that represents a move of a chessfigure
     */
    public static void send_move(Move move) {
        JSONObject sendObject = move.toJsonObject();
        last_obj = move;
        sendObject(sendObject);
    }

    /**
     * Converts a <code>String</code> o a <code>JSONObject</code>
     * and then passes it onto <code>sendObject()</code>.
     *
     * @param msg a chat message
     */
    public static void send_chat_msg(String msg) {
        JSONObject send_object = new JSONObject();

        try {
            send_object.put("chat", msg);
            send_object.put("player", USERNAME);
            send_object.put("part", "false");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendObject(send_object);
    }

    /**
     * Gets an <code>JSONObject</code> and sends it
     * over the newtork to the server
     *
     * @param obj object which will be sent to the server
     */
    public static void sendObject(JSONObject obj) {
        LoggingSingleton.getInstance().logToFile(Level.INFO, "Sent object: " + obj.toString());
        try {
            pw.println(encrypter.encrypt(obj.toString()));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Listens for <code>String</code>, converts them into <code>JSONObjects</code>
     * and then returns it
     *
     * @return returns a <code>JSONObject</code> or null if the received <code>String</code> is not valid
     * @throws IOException network error
     * @throws JSONException received <code>String</code> is not a valid <code>JSONObject</code>
     */
    private JSONObject get_object() throws IOException, JSONException {
        String got_obj = br.readLine();
        try {
            return new JSONObject(encrypter.decrypt(got_obj));
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Closes all network streams
     */
    public void reset() {
        try {
            self.close();
            br.close();
            pw.close();
        } catch (Exception e) {
            LoggingSingleton.getInstance().error("Failed to close network streams", e);
        }
    }

    /**
     * Waits for the start from the server
     *
     * @return if this player starts <code>true</code> else <code>false</code>
     * @throws IOException network error
     */
    public boolean start() throws IOException {
        /*
            This function determines which of the
            two clients starts first.
            (This will be the client which connected
             first to the Server. In most cases this will
             be the client starting the Server)
         */

        //printToChat("Server", "Game has started");
        return br.readLine().equals("start");
    }
}