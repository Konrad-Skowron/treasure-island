package pl.skowron.UI;

import pl.skowron.main.Master;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;

public class MasterFrame extends JFrame {
    public Container c;
    public MapPanel mp;
    public JPanel communicationPanel;
    public JButton scoreboardBtn;
    public Scoreboard s;
    public static HashMap<Integer, PlayerPing> players = new HashMap<>();

    public MasterFrame(Master m) {
        setTitle("Labolatorium 6 - Game Master");
        setBounds(300, 90, 925, 457);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        mp = new MapPanel(true);
        mp.setLocation(10, 10);
        mp.setLayout(null);
        c.add(mp);

        communicationPanel = new JPanel();
        communicationPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        ReceiverPanel receiver = new ReceiverPanel(m);
        receiver.setBorder(new LineBorder(new Color(0, 0, 0)));
        receiver.setBounds(35, 13, 220, 95);
        communicationPanel.add(receiver);

        s = new Scoreboard();
        scoreboardBtn = new JButton("Scoreboard");
        scoreboardBtn.addActionListener(arg0 -> Scoreboard.show(s));
        scoreboardBtn.setBounds(70, 220, 150, 50);
        communicationPanel.add(scoreboardBtn);

        communicationPanel.setSize(290, 400);
        communicationPanel.setLocation(615, 10);
        communicationPanel.setLayout(null);
        c.add(communicationPanel);
        setVisible(true);
    }

    public void addPlayerPing(int port, int xCoord, int yCoord) {
        PlayerPing ping = new PlayerPing();
        ping.setLocation(xCoord * MapPanel.scale, yCoord * MapPanel.scale);
        players.put(port, ping);
        mp.add(ping);
        mp.repaint();
    }

    public void movePlayerPing(int port, int xCoord, int yCoord) {
        players.get(port).setLocation(xCoord * MapPanel.scale, yCoord * MapPanel.scale);
        mp.repaint();
    }

    public void updateScoreboard() {
        s.update();
    }
}

class Scoreboard extends JPanel {
    public JTable scoreboard;
    public DefaultTableModel tableModel;

    public Scoreboard() {
        String[] columns = {"Rank", "Player port", "Score"};
        tableModel = new DefaultTableModel(columns, 0);
        scoreboard = new JTable(tableModel);
        JScrollPane sc = new JScrollPane(scoreboard);
        add(sc);
    }

    public void update() {
        tableModel.setRowCount(0);
        List<Integer> scores = new ArrayList<>(Master.ranking.values());
        scores.sort(Collections.reverseOrder());
        HashMap<Integer, Integer> rankingSwitched = new HashMap<>();
        for (int key : Master.ranking.keySet()) {
            rankingSwitched.put(Master.ranking.get(key), key);
        }
        for (int i = 0; i < scores.size(); i++) {
            tableModel.insertRow(tableModel.getRowCount(),
                    new Object[]{i + 1, rankingSwitched.get(scores.get(i)), scores.get(i)});
        }
    }
    public static void show(Scoreboard s) {
        JFrame f = new JFrame("Scoreboard");
        f.add(s);
        f.pack();
        f.setVisible(true);
        f.setResizable(false);
        s.update();
    }
}

