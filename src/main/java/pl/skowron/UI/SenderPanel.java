package pl.skowron.UI;

import pl.skowron.main.Player;
import pl.skowron.main.User;

import javax.swing.*;

public class SenderPanel extends JPanel {
	private JTextField txtHost;
	private JTextField txtPort;

	public SenderPanel(Player p) {
		setLayout(null);

		JToggleButton btnSend = new JToggleButton("Send");
		btnSend.addActionListener(arg0 -> {
			User.host = txtHost.getText();
			Player.masterPort = Integer.parseInt(txtPort.getText());
			Player.endTime = System.currentTimeMillis() + 1000 * 60 * 15;
			p.start();
		});
		btnSend.setBounds(121, 86, 78, 25);
		add(btnSend);
		
		txtHost = new JTextField();
		txtHost.setBounds(51, 13, 149, 22);
		add(txtHost);
		
		txtPort = new JTextField();
		txtPort.setBounds(51, 50, 78, 22);
		add(txtPort);
		
		JLabel lblHost = new JLabel("host:");
		lblHost.setBounds(12, 16, 35, 16);
		add(lblHost);
		
		JLabel lblPort = new JLabel("port:");
		lblPort.setBounds(12, 50, 35, 16);
		add(lblPort);
	}
}
