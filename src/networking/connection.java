package networking;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Name:    Joel Klimont
 * Class:   3CHIF
 * Date:    2017-03-21
 * Project: jessy
 * Desc.:   One connection which is sending and receiving json objects (and strings)
 */
public class connection {

    private Socket         self;
    private BufferedReader br;
    private PrintWriter    pw;

    public connection(String connect_to_ip, int port) throws IOException {
        self = new Socket(InetAddress.getLocalHost(), port); // TODO: connect to other ip rather than local
        br = new BufferedReader(new InputStreamReader(self.getInputStream()));
        pw = new PrintWriter(self.getOutputStream(), true);
    }

    public void send(JSONObject send_object) throws IOException {
        pw.println("sending object");

        if (br.readLine().equals("OK")) {
            // TODO: check for errors and implement resending

            String json_object = send_object.toString();
            pw.println(json_object);

        } else {
            throw new IOException();
        }
    }

    public JSONObject get_object() throws IOException, JSONException {
        if (br.readLine().equals("sending object")) {
            pw.println("OK");
            JSONObject got_object = new JSONObject(br.readLine());
            pw.println("GET OK");
            return got_object;
        }
        return null;
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