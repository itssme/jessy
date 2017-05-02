package networking;

import board.Move;
import board.Position;
import logging.Logging;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;

import static controllers.SendBTNController.printToChat;
import static database.Scorer.USERNAME;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-21
 * Project: jessy
 * Desc.:   One connection which is sending and receiving json objects (and strings)
 */
public class Connection implements Runnable {

    private Socket       self;
    private BufferedReader br;
    private boolean   sending = false;
    private static Move      last_obj;
    private static PrintWriter     pw;
    private Thread this_thread;

    public Connection(String connect_to_ip, int port) throws IOException {
        self = new Socket(connect_to_ip, port);
        br = new BufferedReader(new InputStreamReader(self.getInputStream()));
        pw = new PrintWriter(self.getOutputStream(), true);
    }

    public void start_thread() {
        if (this_thread == null) {
            this_thread = new Thread(this);
            this_thread.start();
        }
    }


    @Override
    public void run() {
        while (true) {
            try {

                Logging.logToFile(Level.INFO, "Connection thread is running");

                JSONObject obj = get_object();

                if (obj != null) {
                    if (obj.has("chat")) {

                        String msg = obj.getString("chat");
                        String player = obj.getString("player");
                        printToChat(player, msg);

                        Logging.logToFile(Level.INFO, "GOT CHAT: " + player + " " + msg);

                    } else if (obj.has("resend")) {
                        send_move(last_obj);

                    } else if (obj.has("from x")){
                        Logging.logToFile(Level.INFO, "GOT " + "got position object");

                        Position from = new Position(obj.getInt("from x"), obj.getInt("from y"));
                        Position to = new Position(obj.getInt("to x"), obj.getInt("to y"));

                        Move move = new Move(from, to);

                        Logging.logToFile(Level.INFO, "GOT: " + move.toJsonObject().toString());
                        // TODO: validate (move)
                    } else {
                        Logging.logToFile(Level.WARNING, "GOT: not a valid object " + obj.toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void send_move(Move move) throws IOException {
        JSONObject send_object = move.toJsonObject();
        last_obj = move;
        send_object(send_object);
    }

    public static void send_chat_msg(String msg) throws IOException {
        JSONObject send_object = new JSONObject();

        try {
            send_object.put("chat", msg);
            send_object.put("player", USERNAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        send_object(send_object);
    }

    public static void send_object(JSONObject obj) {
        Logging.logToFile(Level.INFO, "Sent object: " + obj.toString());
        pw.println(obj);
    }

    private JSONObject get_object() throws IOException, JSONException {
        String got_obj = br.readLine();
        return new JSONObject(got_obj);
    }

    public boolean start() throws IOException {
        /*
            This function determines which of the
            two clients starts first.
            (This will be the client which connected
             first to the Server. In most cases this will
             be the client starting the Server)
         */

        return br.readLine().equals("start");
    }
}