package pl.skowron.UI;

import pl.skowron.main.Master;
import pl.skowron.main.Player;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;

public class ReceiverPanel extends JPanel {

	private JTextField txtPort;
	public ReceiverPanel(Player p) {
		setLayout(null);

		txtPort = new JTextField();
		txtPort.setBounds(51, 13, 62, 22);
		add(txtPort);

		JLabel lblPort = new JLabel("port:");
		lblPort.setBounds(12, 16, 35, 16);
		add(lblPort);

		JToggleButton btnListen = new JToggleButton("Listen");
		btnListen.addActionListener(arg0 -> {
			try {
				Player.port = Integer.parseInt(txtPort.getText());
				p.ss = new ServerSocket(Player.port);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		btnListen.setBounds(121, 52, 78, 25);
		add(btnListen);
	}

	public ReceiverPanel(Master m) {
		setLayout(null);

		txtPort = new JTextField();
		txtPort.setBounds(51, 13, 62, 22);
		add(txtPort);

		JLabel lblPort = new JLabel("port:");
		lblPort.setBounds(12, 16, 35, 16);
		add(lblPort);

		JToggleButton btnListen = new JToggleButton("Listen");
		btnListen.addActionListener(arg0 -> {
			try {
				Master.port = Integer.parseInt(txtPort.getText());
				m.ss = new ServerSocket(Master.port);
				m.listen();
				m.reply();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
		btnListen.setBounds(121, 52, 78, 25);
		add(btnListen);
	}
}
