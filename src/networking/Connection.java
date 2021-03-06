package networking;

import board.Move;
import board.Position;
import com.github.bhlangonijr.chesslib.Side;
import database.Scorer;
import logging.ChessSaver;
import logging.LoggingSingleton;
import main.ChessGameController;
import main.Main;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.util.logging.Level;

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
    private static Move last_obj;
    private static PrintWriter pw;
    private Thread this_thread;
    private static Encrypter encrypter;
    private ChessGameController controller;
    private static boolean enableEncryprtion = true;

    /**
     * Opens the connection to the server
     *
     * @param connect_to_ip ip of the server
     * @param port          ports of the server
     * @param password      password for the connection
     * @param controller    The reference to the GameController
     * @throws IOException         network severe
     * @throws InvalidKeyException invalid password
     */
    public Connection(String connect_to_ip, int port, String password, ChessGameController controller) throws IOException, InvalidKeyException {
        self = new Socket(connect_to_ip, port);
        br = new BufferedReader(new InputStreamReader(self.getInputStream()));
        pw = new PrintWriter(self.getOutputStream(), true);
        encrypter = new Encrypter(password, controller);
        this.controller = controller;

        if (System.getProperty("os.name").equalsIgnoreCase("linux")) {
            enableEncryprtion = true;
        } else {
            enableEncryprtion = false;
        }

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
                        controller.printToChat(player, msg);

                        LoggingSingleton.getInstance().log(Level.INFO, "GOT CHAT: " + player + " " + msg);

                    } else if (obj.has("resend")) {
                        send_move(last_obj);

                    } else if (obj.has("from x")) {
                        LoggingSingleton.getInstance().log(Level.INFO, "GOT " + "got position object");

                        Position from = new Position(obj.getInt("from x"), obj.getInt("from y"));
                        Position to = new Position(obj.getInt("to x"), obj.getInt("to y"));

                        Move move = new Move(from, to);

                        LoggingSingleton.getInstance().log(Level.INFO, "GOT: " + move.toJsonObject().toString());

                        ChessGameController.getGameController().executeMove(Move.convertMoveToLib(move));

                    } else if (obj.has("gameState")) {
                        ChessSaver.getInstance().loadGameFromFEN(obj.getString("gameState"));
                        Scorer.OPPONENT = obj.getString("username");

                        if (!Main.CHESSGAMEBOARD.getSideToMove().equals(Side.WHITE)) {
                            Utilities.switchPlayer();
                        }

                    } else if (obj.has("disconnect")) {
                        if (obj.getString("disconnect").equals("true")) {
                            LoggingSingleton.getInstance().info("got disconnect object");
                            controller.disconnect();
                        }

                        return;
                    } else {
                        LoggingSingleton.getInstance().log(Level.WARNING, "GOT: not a valid object " + obj.toString());
                    }
                }
            } catch (IOException e) {
                LoggingSingleton.getInstance().severe("Failed to send json object" + e.getLocalizedMessage());
            } catch (JSONException e) {
                LoggingSingleton.getInstance().severe("Failed to create json object" + e.getLocalizedMessage());
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
        JSONObject sendObject = new JSONObject();

        try {
            sendObject.put("chat", msg);
            sendObject.put("player", USERNAME);
            sendObject.put("part", "false");
        } catch (JSONException e) {
            LoggingSingleton.getInstance().severe("Failed to create chat object " + e.getMessage());
            return;
        }

        sendObject(sendObject);
    }

    /**
     * Send the status of the current game to the other player
     *
     * @param gameState the current game state
     */
    public static void sendGameState(String gameState) {
        JSONObject sendObject = new JSONObject();

        try {
            sendObject.put("gameState", gameState);
            sendObject.put("username", USERNAME);
        } catch (JSONException e) {
            LoggingSingleton.getInstance().severe("Failed to create gameState object " + e.getMessage());
            return;
        }

        sendObject(sendObject);

    }

    /**
     * Gets an <code>JSONObject</code> and sends it
     * over the newtork to the server
     *
     * @param obj object which will be sent to the server
     */
    public static void sendObject(JSONObject obj) {
        LoggingSingleton.getInstance().log(Level.INFO, "Sent object: " + obj.toString());
        try {
            if (enableEncryprtion) {
                pw.println(encrypter.encrypt(obj.toString()));
            } else {
                pw.println(obj.toString());
            }

        } catch (Exception e) {
            LoggingSingleton.getInstance().info("Couldn't send the object");
        }
    }

    /**
     * Listens for <code>String</code>, converts them into <code>JSONObjects</code>
     * and then returns it
     *
     * @return returns a <code>JSONObject</code> or null if the received <code>String</code> is not valid
     * @throws IOException   network severe
     * @throws JSONException received <code>String</code> is not a valid <code>JSONObject</code>
     */
    public JSONObject get_object() throws IOException, JSONException {
        String got_obj = br.readLine();
        try {
            if (enableEncryprtion) {
                return new JSONObject(encrypter.decrypt(got_obj));
            } else {
                return new JSONObject(got_obj);
            }

        } catch (Exception e) {
            LoggingSingleton.getInstance().severe("Failed to decrypt object " + e.getMessage());
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
            LoggingSingleton.getInstance().severe("Failed to close network streams" + e.getLocalizedMessage());
        }
    }

    /**
     * Get start message from host
     *
     * @return <code>true</code> means that the player is starting
     *         <code>false</code> means that the host is starting
     */
    public String getStart() {
        String startFist = "";

        try {
            startFist = br.readLine();
        } catch (IOException e) {
            LoggingSingleton.getInstance().severe("Critical error could not get the starting message from host: " + e.getMessage());
        }

        return startFist;
    }

    /**
     * Sends who starts first over the network
     *
     * (this is the replacement for the old starting
     *  method which let server determine who starts)
     *
     * @param startFirst <code>true</code> if the client starts first
     */
    public void sendStart(boolean startFirst) {
        pw.println(startFirst + "");
    }

    /**
     *  Sets the variable if traffic is getting encrypted.
     */
    public void getEncrypt() {
        String encrypt = "";

        try {
            encrypt = br.readLine();
        } catch (IOException e) {
            LoggingSingleton.getInstance().severe("Critical error could not get the starting message from host: " + e.getMessage());
        }

        if (encrypt.equals("false")) {
            enableEncryprtion = false;
        }
    }

    /**
     * Sends if the traffic will be encrypted
     *
     * @param encrypt <code>true</code> chat will be encrypted
     *                <code>false</code> chat won't be encrypted
     */
    public void sendEncrypt(boolean encrypt) {
        pw.println(encrypt + "");
    }

}