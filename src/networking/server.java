package networking;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-21
 * Project: jessy
 * Desc.:   The server which handels the connections of the players.
 *          One player will start the server and automatically connect
 *          to it. The other player will be able to connect over the ip
 *          of the player which started the server.
 */

// TODO: check if the move is valid

public class server implements Runnable {

    protected static ServerSocket self;
    protected static int player_connected = 0;
    private player player1;
    private player player2;

    public server(int port) throws IOException {
        self = new ServerSocket(port);
        System.err.println("[!] SERVER CONNECTION: Waiting for a connection on " + port);

        player1 = new player(1);
        player2 = new player(2);
        System.out.println("init players");

        while (! this.all_connected()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            player2.send(player1.get_object());
            player1.send(player2.get_object());
        }
    }

    public void starter() { // TODO: rewrite this
        player1.send_str("start");
        player2.send_str("dont start");
    }

    public boolean all_connected() {
        return (player_connected == 2);
    }

}

class player {

    private Socket player;
    private PrintWriter pw_player;
    private BufferedReader br_player;
    public  int number;

    public player(int number) {
        this.number = number;

        System.out.println("Thread " + this.number + " started");

        try {
            player = server.self.accept();
            pw_player = new PrintWriter(player.getOutputStream(), true);
            br_player = new BufferedReader(new InputStreamReader(player.getInputStream()));
            System.out.println("[!] player connected: " + number);
            server.player_connected++;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public JSONObject get_object() {
        try {
            String got_str = br_player.readLine();

            switch (got_str) {
                case "sending object":
                    pw_player.println("OK");
                    try {
                        JSONObject got_obj = new JSONObject(br_player.readLine());
                        return got_obj;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case "start":
                    System.err.println("[!] PLAYER LISTEN: unexpected 'start'"); // TODO: implement resending
                    break;
                case "OK":
                    System.err.println("[!] PLAYER LISTEN: unexpected 'OK'");    // TODO: implement resending
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void send(JSONObject send_obj) {
        pw_player.println("sending object");

        try {
            String got_str = br_player.readLine();
            if (got_str.equals("OK")) {
                pw_player.println(send_obj);
                got_str = br_player.readLine();

                if (got_str.equals("GET OK")) {
                    System.out.println("[!] SUCCESS SENDING OBJ");
                } else {
                    System.err.println("[!] SERVER: client didn't got object + " + got_str); // TODO: try again
                }

            } else {
                System.err.println("[!] SERVER: could not send object");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send_str(String str) {
        pw_player.println(str);
    }
}
