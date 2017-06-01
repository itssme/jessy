package networking;

import logging.LoggingSingleton;
import main.ChessGameController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TreeMap;
import java.util.logging.Level;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-21
 * Project: jessy
 * Desc.:   The Server which handles the connections of the players.
 *          One player will start the Server and automatically connect
 *          to it. The other player will be able to connect over the ip
 *          of the player which started the Server.
 */
public class Server extends Thread {

    private static ServerSocket self;
    private static int player_connected = 0;
    public player player1;
    public player player2;
    private Thread  thread;
    private Thread player1_thread = null;
    private Thread player2_thread = null;

    /**
     * Binds the server ip
     *
     * @param port the port for the server
     * @throws IOException network error
     */
    public Server(int port) throws IOException {
        self = new ServerSocket(port);
        LoggingSingleton.getInstance().log(Level.INFO, "SERVER: init " + port);
    }

    /**
     * Starts the server as a thread
     */
    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * Closes all networkstreams including the ones of the player dummies
     */
    public void close() {
        player1.close();
        player2.close();

        try {
            self.close();
        } catch (IOException e) {
            LoggingSingleton.getInstance().severe("Could not close server" + e.getLocalizedMessage());
        }

        player1 = null;
        player2 = null;
    }

    /**
     * The server thread, which lets the players connect
     */
    @Override
    public void run() {
        LoggingSingleton.getInstance().log(Level.INFO, "SERVER: waiting for connection");

        player1 = new player(1);
        player2 = new player(2);

        synchronized (this) {
            player1_thread = new Thread(player1);
            player2_thread = new Thread(player2);

            player1_thread.start();
            player2_thread.start();

            notify();
        }
        LoggingSingleton.getInstance().log(Level.INFO, "SERVER STARTED");
    }

    /**
     *  Initializes the dummy players without starting them
     */
    public void startSilent() {
        LoggingSingleton.getInstance().log(Level.INFO, "SERVER: waiting for connection");

        player1 = new player(1);
        player2 = new player(2);

        player1_thread = new Thread(player1);
        player2_thread = new Thread(player2);

        LoggingSingleton.getInstance().log(Level.INFO, "SERVER STARTED");
    }

    /**
     * starts the player dummy threads
     */
    public void startPlayerThreads() {
        player1_thread.start();
        player2_thread.start();
    }

    /**
     * Checks if both players are connected
     *
     * @return <code>true</code> if both players are connected
     *         and returns <code>false</code> if only one or no player is connected
     */
    @Deprecated
    public boolean all_connected() {
        return (player_connected == 2);
    }

    public class player implements Runnable {

        private Socket player;
        private PrintWriter pw_player;
        private BufferedReader br_player;
        private int number;
        private Thread thread;

        /**
         * Starts the player dummy and listens for the client to connect
         *
         * @param number the number of the dummy
         */
        private player(int number) {
            this.number = number;

            LoggingSingleton.getInstance().log(Level.INFO, "IN SERVER " + "Thread " + this.number + " started");

            try {
                player = self.accept();
                pw_player = new PrintWriter(player.getOutputStream(), true);
                br_player = new BufferedReader(new InputStreamReader(player.getInputStream()));
                LoggingSingleton.getInstance().log(Level.INFO, "IN SERVER " + " player connected: " + number);
                Server.player_connected++;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        /**
         * Closes all networkstreams
         */
        public void close() {
            try {
                player.close();
                pw_player.close();
                br_player.close();
            } catch (Exception e) {
                LoggingSingleton.getInstance().severe("Failed to close network streams (player " + number + ")" + e.getLocalizedMessage());
            }
        }

        /**
         * Runs the thread which passes on the object between the players
         */
        @Override
        public void run() {
            while (true) {
                if (number == 1) {
                    if (player2 != null) {
                        player2.send(this.get());
                    } else {
                        this.close();
                    }
                } else {
                    if (player1 != null) {
                        player1.send(this.get());
                    } else {
                        this.close();
                    }
                }
            }
        }

        /**
         * Sends who starts first over the network
         *
         * @param startFirst if this string is <code>true</code> the other player will start normally
         *                   if this string is <code>false</code> the other player will not start
         *                   if this string is <code>true_</code> the other player will take the white side and start
         *                   if this string is <code>false_</code> the other player will not start and take
         *                                                                                    the black side
         */
        public void sendStart(String startFirst) {
            pw_player.println(startFirst);
        }

        /**
         * Sets the variable if traffic is getting encrypted.
         *
         * @return <code>true</code> if the chat is encrypted
         *         <code>false</code> if the chat is not encrypted
         */
        public boolean getEncrypt() {
            String encrypt = "";

            try {
                encrypt = br_player.readLine();
            } catch (IOException e) {
                LoggingSingleton.getInstance().severe("Critical error could not " +
                        "get the starting message from host: " + e.getMessage());
            }

            if (encrypt.equals("false")) {
                return false;
            }

            return true;
        }

        /**
         * Sends if the traffic will be encrypted
         *
         * @param encrypt <code>true</code> chat will be encrypted
         *                <code>false</code> chat won't be encrypted
         */
        public void sendEncrypt(boolean encrypt) {
            pw_player.println(encrypt + "");
        }

        /**
         * Gets an <code>String</code> and return it
         *
         * @return <code>String</code> which has been received over network
         */
        private String get() {
            try {
                return br_player.readLine();
            } catch (IOException e) {
                LoggingSingleton.getInstance().severe("Could not read network stream -> closing connection" + e.getLocalizedMessage());
                this.close();
            }

            return "";
        }

        /**
         * Sends a <code>String</code> ot the player
         *
         * @param send_str <code>String</code> to send
         */
        private void send(String send_str) {
            pw_player.println(send_str);
        }
    }
}