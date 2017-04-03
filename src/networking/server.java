package networking;

import logging.Logging;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

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
public class server {

    private static ServerSocket self;
    private static int player_connected = 0;
    private player player1;
    private player player2;

    public server(int port) throws IOException {
        self = new ServerSocket(port);
        Logging.logToFile(Level.INFO, "SERVER CONNECTION: Waiting for a connection on " + port);

        player1 = new player(1);
        player2 = new player(2);

        while (! this.all_connected()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Thread player1_thread = new Thread(player1);
        Thread player2_thread = new Thread(player2);

        player1_thread.start();
        player2_thread.start();

        Logging.logToFile(Level.INFO, "SERVER STARTED");
    }

    public void starter() {
        player1.send_str("start");
        player2.send_str("dont start");
    }

    public boolean all_connected() {
        return (player_connected == 2);
    }

    private class player implements Runnable {

        private Socket player;
        private PrintWriter pw_player;
        private BufferedReader br_player;
        private int number;
        private Thread thread;

        private player(int number) {
            this.number = number;

            Logging.logToFile(Level.INFO, "IN SERVER " + "Thread " + this.number + " started");

            try {
                player = server.self.accept();
                pw_player = new PrintWriter(player.getOutputStream(), true);
                br_player = new BufferedReader(new InputStreamReader(player.getInputStream()));
                Logging.logToFile(Level.INFO, "IN SERVER " + " player connected: " + number);
                server.player_connected++;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void start () {
            Logging.logToFile(Level.INFO, "IN SERVER " + "Starting " +  number );
            if (thread == null) {
                thread = new Thread (this, Integer.toString(number));
                thread.start ();
            }
        }


        @Override
        public void run() {
            System.out.println("started " + number);
            while (true) { // TODO: stop by a static variable if the game is over
                if (number == 1) {
                    player2.send(this.get_object());
                } else {
                    player1.send(this.get_object());
                }
            }
        }

        private JSONObject get_object() {

            try {
                JSONObject got_obj = new JSONObject(br_player.readLine());
                Logging.logToFile(Level.INFO, "IN SERVER " + number + " got in server" + got_obj.toString());
                return got_obj;

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        private void send(JSONObject send_obj) {
            pw_player.println(send_obj);
            Logging.logToFile(Level.INFO, "IN SERVER " + number + " sent " + send_obj.toString());
        }

        private void send_str(String str) {
            pw_player.println(str);
        }
    }
}