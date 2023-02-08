package pl.skowron.UI;

import pl.skowron.main.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PlayerFrame extends JFrame {

    public Container c;
    public PlayerPing ping;
    public MapPanel mp;
    public JPanel communicationPanel;
    public JLabel timeLeft;
    public JLabel score;
    public JLabel waitTime;

    public PlayerFrame(Player p) {
        setTitle("Labolatorium 6 - Player ");
        setBounds(300, 90, 925, 457);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        mp = new MapPanel(false);
        mp.setLocation(10, 10);
        mp.setLayout(null);
        c.add(mp);

        communicationPanel = new JPanel();
        communicationPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        ReceiverPanel receiver = new ReceiverPanel(p);
        receiver.setBorder(new LineBorder(new Color(0, 0, 0)));
        receiver.setBounds(35, 13, 220, 95);
        communicationPanel.add(receiver);

        SenderPanel sender = new SenderPanel(p);
        sender.setBorder(new LineBorder(new Color(0, 0, 0)));
        sender.setBounds(35, 132, 220, 129);
        communicationPanel.add(sender);

        communicationPanel.setSize(290, 400);
        communicationPanel.setLocation(615, 10);
        communicationPanel.setLayout(null);
        c.add(communicationPanel);

        score = new JLabel("Score: 0");
        score.setBounds(35, 280, 150, 35);
        communicationPanel.add(score);

        timeLeft = new JLabel("Time left: 900 s");
        timeLeft.setBounds(35, 310, 150, 35);
        communicationPanel.add(timeLeft);

        waitTime = new JLabel();
        waitTime.setBounds(35, 340, 150, 35);
        communicationPanel.add(waitTime);

        c.add(communicationPanel);
        setVisible(true);
    }
}
