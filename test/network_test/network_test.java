package network_test;

import board.Move;
import board.Position;
import networking.Connection;
import networking.Server;

import java.io.IOException;

/**
 * THIS IS A TEST
 */
public class network_test {

    public static void main(String[] args) throws IOException {
        Open_server serv = new Open_server();
        Open_connection1 con1 = new Open_connection1();
        Open_connection2 con2 = new Open_connection2();

        serv.start();
        con1.start();
        con2.start();

    }
}

class Open_server implements Runnable{

    private Thread t;

    Open_server() throws IOException {
    }


    @Override
    public void run() {
        try {
            Server cool_server = new Server(5060);
            cool_server.start();

            while (! cool_server.all_connected()) {
                Thread.sleep(500);
                System.out.println("not connected");
            }
            System.out.println("go!!!");
            cool_server.starter();
            System.out.println("running");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}

class Open_connection1 implements Runnable {

    private Thread t;

    Open_connection1() throws IOException {
    }

    @Override
    public void run() {
        /*try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        try {
            Connection con = new Connection("127.0.0.1", 5060);

            boolean start = con.start();

            Thread con_thread = new Thread(con);
            con_thread.start();

            if (start) {
                System.out.println("[!] connection one will start");

                Move move = new Move(new Position(10,20), new Position(20, 40));

                con.send_move(move);
                System.out.println("[!] one sent object");

            } else {
                System.out.println("[!] connection one won't start first");
                System.out.println("thread is waiting");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}

class Open_connection2 implements Runnable{

    private Thread t;

    Open_connection2() throws IOException {
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Connection con = new Connection("127.0.0.1", 5060);

            boolean start = con.start();

            Thread con_thread = new Thread(con);
            con_thread.start();

            if (start) {
                System.out.println("[!] connection two will start");

                Move move = new Move(new Position(10,20), new Position(20, 40));

                con.send_move(move);
                System.out.println("[!] two sent object");

            } else {
                System.out.println("[!] connection two won't start first");
                System.out.println("thread is waiting");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}