/**
 * @author Konrad Skorwon
 * gradlew build
 * gradlew jar
 * java -cp lab06_pop.jar pl.skowron.main.Master
 */
package pl.skowron.main;

import org.json.JSONObject;
import pl.skowron.UI.MasterFrame;
import pl.skowron.logic.FieldContent;
import pl.skowron.tools.DataController;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Master extends User {
    public static int playerCount = 0;
    public static HashMap<Integer, Integer> ranking = new HashMap<>();
    public ConcurrentLinkedQueue<JSONObject> requests = new ConcurrentLinkedQueue<>();
    public MasterFrame mf;

    public Master() {
        host = "localhost";
        map = DataController.loadMap();
        mf = new MasterFrame(this);
    }

    public void listen() {
        Thread t = new Thread(() -> {
            while (true) {
                requests.add(receive());
            }
        });
        t.start();
    }
    public void reply() {
        Thread t = new Thread(() -> {
            while (true) {
                if (requests.size() == 0) continue;
                JSONObject r = requests.poll();
                if (r.getString("order").equals("addPlayer")) {
                    playerCount++;
                    int xCoord, yCoord;
                    do {
                        xCoord = new Random().nextInt(60 - 1);
                        yCoord = new Random().nextInt(40 - 1);
                    } while (map.get(yCoord).get(xCoord).content != FieldContent.EMPTY);
                    mf.addPlayerPing(r.getInt("port"), xCoord, yCoord);
                    JSONObject response = new JSONObject()
                            .put("playerCount", playerCount)
                            .put("x", xCoord)
                            .put("y", yCoord);
                    ranking.put(r.getInt("port"), 0);
                    send(response, host, r.getInt("port"));
                } else if (r.getString("order").equals("showField")) {
                    JSONObject response = new JSONObject()
                            .put("content", map.get(r.getInt("y")).get(r.getInt("x")).content.toString())
                            .put("waitTime", map.get(r.getInt("y")).get(r.getInt("x")).waitTime);
                    send(response, host, r.getInt("port"));
                } else if (r.getString("order").equals("move")) {
                    JSONObject response;
                    if (map.get(r.getInt("y")).get(r.getInt("x")).content == FieldContent.PLAYER) {
                         response = new JSONObject().put("result", "invalid");
                    } else {
                        map.get(r.getInt("y")).get(r.getInt("x")).content = FieldContent.PLAYER;
                        map.get(r.getInt("yPrev")).get(r.getInt("xPrev")).content = FieldContent.EMPTY;
                        mf.movePlayerPing(r.getInt("port"), r.getInt("x"), r.getInt("y"));
                        response = new JSONObject().put("result", "success");
                    }
                    send(response, host, r.getInt("port"));
                } else if (r.getString("order").equals("take")) {
                    map.get(r.getInt("y")).get(r.getInt("x")).content = FieldContent.EMPTY;
                    ranking.put(r.getInt("port"), r.getInt("score"));
                    mf.updateScoreboard();
                    mf.mp.repaint();
                }
            }
        });
        t.start();
    }

    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Master m = new Master();
    }
}
