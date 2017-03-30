package networking;

import board.Move;
import board.Position;
import org.json.JSONException;
import org.json.JSONObject;
import view.Chessgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static database.Scorer.USERNAME;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-21
 * Project: jessy
 * Desc.:   One connection which is sending and receiving json objects (and strings)
 */
public class connection implements Runnable { // TODO: remove all prints and replace with logging

    private Socket       self;
    private BufferedReader br;
    private PrintWriter    pw;
    private Chessgame    game;
    private Move     last_obj;
    private boolean   sending = false;

    public int number;

    public connection(String connect_to_ip, int port, Chessgame game, int test_number) throws IOException {
        self = new Socket(connect_to_ip, port);
        br = new BufferedReader(new InputStreamReader(self.getInputStream()));
        pw = new PrintWriter(self.getOutputStream(), true);
        this.game = game;
        number = test_number;
    }

    @Override
    public void run() {
        while (true) {
            try {

                JSONObject obj = get_object();

                if (obj != null) {
                    if (obj.has("chat")) {
                        String msg = obj.getString("chat");
                        String player = obj.getString("player");
                        game.printToChat(player, msg);

                        System.out.println(System.currentTimeMillis() + " -> " + "FROM " + number + " -> " + player + " " + msg);

                    } else if (obj.has("resend")) {
                        send_move(last_obj);

                    } else {
                        System.out.println(System.currentTimeMillis() + " -> " + "FROM " + number + " -> " + "got position object");

                        Position from = new Position(obj.getInt("from x"), obj.getInt("from y"));
                        Position to = new Position(obj.getInt("to x"), obj.getInt("to y"));

                        Move move = new Move(from, to);

                        System.out.println(System.currentTimeMillis() + " -> " + "FROM " + number + " -> " + move.toJsonObject().toString());
                        // TODO: validate (move)
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void send_move(Move move) throws IOException {
        System.out.println(System.currentTimeMillis() + " -> " + "FROM " + number + " -> " + "in move sending: " + sending);
        JSONObject send_object = move.toJsonObject();

        last_obj = move;

        String json_object = send_object.toString();
        pw.println(json_object);
    }

    public void send_chat_msg(String msg) throws IOException {
        JSONObject send_object = new JSONObject();

        try {
            send_object.put("chat", msg);
            send_object.put("player", USERNAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String json_object = send_object.toString();
        pw.println(json_object);
    }

    private JSONObject get_object() throws IOException, JSONException {
        System.out.println(System.currentTimeMillis() + " -> " + "FROM " + number + " -> " + "get_obj");

        System.out.println(System.currentTimeMillis() + " -> " + "FROM " + number + " -> " + "listening for object");
        String wasd = br.readLine();
        System.out.println(System.currentTimeMillis() + " -> " + "FROM " + number + " -> " + "should be json " + wasd);

        return new JSONObject(wasd);
    }

    public boolean start() throws IOException {
        /*
            This function determines which of the
            two clients starts first.
            (This will be the client which connected
             first to the server. In most cases this will
             be the client starting the server)
         */
        return br.readLine().equals("start");
    }
}