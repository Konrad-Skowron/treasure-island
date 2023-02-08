package pl.skowron.UI;

import javax.swing.*;
import java.awt.*;

public class PlayerPing extends JPanel {

    public PlayerPing() {
        this.setSize(MapPanel.scale, MapPanel.scale);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setBackground(Color.WHITE);
    }
}
