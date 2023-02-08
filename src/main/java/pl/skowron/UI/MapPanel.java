package pl.skowron.UI;

import pl.skowron.logic.Field;
import pl.skowron.logic.FieldContent;
import pl.skowron.main.Master;
import pl.skowron.main.Player;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class MapPanel extends JPanel  {
    public static List<List<Field>> map;
    public static int scale = 10;
    public static double shade = 0.6;
    public boolean isMaster;

    public MapPanel(boolean isMaster) {
        this.setSize(600 + 1, 400 + 1);
        this.setBorder(new LineBorder(new Color(153, 135, 108)));
        this.isMaster = isMaster;
    }

    public void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);
            HashMap<Integer, List<Integer>> visibleFields = new HashMap<>();

            if (isMaster) {
                map = Master.map;
                Player.yCoord = -2;
            } else {
                map = Player.map;
                for (int i = -1; i <= 1; i++) {
                    visibleFields.put(Player.yCoord + i, List.of(Player.xCoord - 1, Player.xCoord, Player.xCoord + 1));
                }
            }

            for (int i = 0; i < map.size(); i++) {
                for (int j = 0; j < map.get(i).size(); j++) {
                    g.setColor(new Color((int)(shade * 104), (int)(shade * 149), (int)(shade * 46)));
                    if (isMaster || (i <= Player.yCoord + 1 && i >= Player.yCoord - 1 &&
                            visibleFields.get(i).contains(j))) g.setColor(new Color(104, 149, 46));
                    g.fillRect(j * scale, i * scale, scale, scale);
                    if (map.get(i).get(j).content == FieldContent.UNKNOWN) {
                        g.setColor(new Color(214, 190, 150));
                        g.fillRect(j * scale, i * scale, scale, scale);
                    } else if (map.get(i).get(j).content == FieldContent.OBSTACLE) {
                        g.setColor(new Color((int)(shade * 94), (int)(shade * 94), (int)(shade * 94)));
                        if (isMaster || (i <= Player.yCoord + 1 && i >= Player.yCoord - 1 &&
                                visibleFields.get(i).contains(j))) g.setColor(new Color(94, 94, 94));
                        g.fillRect(j * scale, i * scale, scale, scale);
                    } else if (map.get(i).get(j).content == FieldContent.TREASURE) {
                        g.setColor(new Color((int)(shade * 212), (int)(shade * 175), (int)(shade * 55)));
                        if (isMaster || (i <= Player.yCoord + 1 && i >= Player.yCoord - 1 &&
                                visibleFields.get(i).contains(j))) g.setColor(new Color(212, 175, 55));
                        g.fillOval(j * scale + 1, i * scale + 1, scale - 2, scale - 2);
                    } else if (!isMaster && map.get(i).get(j).content == FieldContent.PLAYER
                            && i <= Player.yCoord + 1 && i >= Player.yCoord - 1 && visibleFields.get(i).contains(j)) {
                        g.setColor(Color.BLACK);
                        g.fillRect(j * scale + 1, i * scale + 1, scale - 2, scale - 2);
                        g.setColor(Color.WHITE);
                        g.fillRect(j * scale + 2, i * scale + 2, scale - 4, scale - 4);
                    }
                }
            }
        } catch (Exception e) { }
    }
}
