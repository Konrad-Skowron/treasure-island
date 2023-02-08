package pl.skowron.main;

import org.json.JSONObject;
import pl.skowron.logic.Field;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public abstract class User {
    public ServerSocket ss;
    public static int port;
    public static String host;
    public static List<List<Field>> map;
    public void send(JSONObject request, String host, int port) {
        try {
            Socket s = new Socket(host, port);
            PrintWriter pw = new PrintWriter(s.getOutputStream());
            pw.println(request);
            pw.flush();
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public JSONObject receive() {
        JSONObject response = null;
        try {
            Socket s = ss.accept();
            InputStreamReader in = new InputStreamReader(s.getInputStream());
            BufferedReader bf = new BufferedReader(in);
            response = new JSONObject(bf.readLine());
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
