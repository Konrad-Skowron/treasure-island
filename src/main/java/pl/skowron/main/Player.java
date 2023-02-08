/**
 * @author Konrad Skorwon
 * gradlew build
 * gradlew jar
 * java -cp lab06_pop.jar pl.skowron.main.Player
 */
package pl.skowron.main;

import org.json.JSONObject;
import pl.skowron.UI.MapPanel;
import pl.skowron.UI.PlayerFrame;
import pl.skowron.UI.PlayerPing;
import pl.skowron.logic.Field;
import pl.skowron.logic.FieldContent;
import pl.skowron.tools.GenerateMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Player extends User implements Runnable {

    public static Player p;
    public static int xCoord;
    public static int yCoord;
    public static int score;
    public static int masterPort;
    public static long endTime;
    public static PlayerFrame pf;
    public List<List<Integer>> tempUnvisited = new ArrayList<>();

    public Player() {
        score = 0;
        host = "localhost";
        map = new ArrayList<>();
        for (int i = 0; i < GenerateMap.rows; i++) {
            map.add(new ArrayList<>());
            for (int j = 0; j < GenerateMap.columns; j++) {
                map.get(i).add(new Field());
            }
        }
    }

    public void see() {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (xCoord + i < 0 || xCoord + i > 59 || yCoord + j < 0 || yCoord + j > 39) continue;
                JSONObject request = new JSONObject()
                        .put("order", "showField")
                        .put("port", port)
                        .put("x", xCoord + i)
                        .put("y", yCoord + j);
                send(request, host, masterPort);

                JSONObject response = receive();
                Field f = new Field(FieldContent.valueOf(response.getString("content")),
                        response.getInt("waitTime"), map.get(yCoord + j).get(xCoord + i).isVisited);
                map.get(request.getInt("y")).set(request.getInt("x"), f);
            }
        }
    }

    public void move() {
        map.get(yCoord).get(xCoord).isVisited = true;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (xCoord + i < 0 || xCoord + i > 59 || yCoord + j < 0 || yCoord + j > 39) continue;
                if (map.get(yCoord + j).get(xCoord + i).content == FieldContent.TREASURE) {
                    take(xCoord + i, yCoord + j);
                    return;
                }
            }
        }
        int tempX = xCoord;
        int tempY = yCoord;
        int direction = new Random().nextInt(8);
        switch (direction) {
            case 0:
                yCoord -= 1;
                break;
            case 1:
                xCoord += 1;
                break;
            case 2:
                yCoord += 1;
                break;
            case 3:
                xCoord -= 1;
                break;
            case 4:
                yCoord -= 1;
                xCoord -= 1;
                break;
            case 5:
                yCoord -= 1;
                xCoord += 1;
                break;
            case 6:
                yCoord += 1;
                xCoord -= 1;
                break;
            case 7:
                yCoord += 1;
                xCoord += 1;
                break;
        }
        if (xCoord < 0 || xCoord > 59 || yCoord < 0 || yCoord > 39 ||
                map.get(yCoord).get(xCoord).content != FieldContent.EMPTY ||
                map.get(yCoord).get(xCoord).isVisited) {
            xCoord = tempX;
            yCoord = tempY;
            boolean isEverythingVisited = true;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (xCoord + i < 0 || xCoord + i > 59 || yCoord + j < 0 || yCoord + j > 39 ||
                            map.get(yCoord + j).get(xCoord + i).content != FieldContent.EMPTY) continue;
                    if (!map.get(yCoord + j).get(xCoord + i).isVisited) {
                        isEverythingVisited = false;
                        break;
                    }
                }
            }
            if (isEverythingVisited) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (xCoord + i < 0 || xCoord + i > 59 || yCoord + j < 0 || yCoord + j > 39 ||
                                map.get(yCoord + j).get(xCoord + i).content != FieldContent.EMPTY) continue;
                        map.get(yCoord + j).get(xCoord + i).isVisited = false;
                        tempUnvisited.add(new ArrayList<>(Arrays.asList(xCoord + i, yCoord + j)));
                    }
                }
            }
            move();
            return;
        } else if (!tempUnvisited.isEmpty()) {
            for (List<Integer> coords : tempUnvisited) {
                map.get(coords.get(1)).get(coords.get(0)).isVisited = true;
            }
            tempUnvisited.clear();
        }

        JSONObject request = new JSONObject()
                .put("order", "move")
                .put("port", port)
                .put("x", xCoord)
                .put("y", yCoord)
                .put("xPrev", tempX)
                .put("yPrev", tempY);
        send(request, host, masterPort);

        JSONObject response = receive();
        if (!response.getString("result").equals("success")) {
            xCoord = tempX;
            yCoord = tempY;
            move();
        }
    }

    public void take(int xCoord, int yCoord) {
        pf.waitTime.setText("Unpacking: " + map.get(yCoord).get(xCoord).waitTime / 1000 + " s");
        Master.wait(map.get(yCoord).get(xCoord).waitTime);
        pf.waitTime.setText("");
        map.get(yCoord).get(xCoord).content = FieldContent.EMPTY;
        map.get(yCoord).get(xCoord).isVisited = true;
        score += 10;
        pf.score.setText("Score: " + Player.score);
        JSONObject request = new JSONObject()
                .put("order", "take")
                .put("port", port)
                .put("score", score)
                .put("x", xCoord)
                .put("y", yCoord);
        send(request, host, masterPort);
    }

    public void addPlayer() {
        JSONObject request = new JSONObject()
                .put("order", "addPlayer")
                .put("port", port);
        send(request, host, masterPort);
        JSONObject response = receive();
        xCoord = response.getInt("x");
        yCoord = response.getInt("y");
    }

    public void start() {
        p.addPlayer();
        pf.ping = new PlayerPing();
        pf.ping.setLocation(xCoord * MapPanel.scale, yCoord * MapPanel.scale);
        pf.mp.add(pf.ping);
        p.see();
        Thread thread = new Thread(p);
        thread.start();
    }

    public static void main(String[] args) {
        p = new Player();
        pf = new PlayerFrame(p);
    }

    @Override
    public void run() {
        while (System.currentTimeMillis() < endTime) {
            Master.wait(500);
            p.move();
            p.see();
            pf.ping.setLocation(xCoord * MapPanel.scale, yCoord * MapPanel.scale);
            pf.timeLeft.setText("Time left: " + (endTime - System.currentTimeMillis()) / 1000 + " s");
            pf.mp.repaint();
        }
    }
}
