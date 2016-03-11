package gui;

import JobScheduler.JsonConfigSetUp;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 *
 * Created by Emanuel Brici on 10/30/15.
 */
public class ComputersToBeUsed extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public ComputersToBeUsed(final JsonConfigSetUp jsonConfigSetUp) {
		setTitle("Lab Machines");
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 525, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		int index = 25;
		for (String roomNumber: jsonConfigSetUp.getLabRooms()) {
			JLabel lblRoom = new JLabel("Computers in Room " + roomNumber);
			lblRoom.setBounds(50, index, 175, 16);

			JTextArea textArea = new JTextArea();
			textArea.setBounds(225, index, 250, 16);
			textArea.setName(roomNumber);
			contentPane.add(textArea);
			index = index+25;

			contentPane.add(lblRoom);
		}
		
		JTextArea txtrIe = new JTextArea();
		txtrIe.setBackground(UIManager.getColor("Button.background"));
		txtrIe.setEditable(false);
		txtrIe.setText("i.e 1-3 5 7 9-15");
		txtrIe.setBounds(50, 136, 105, 15);
		contentPane.add(txtrIe);
		
		JButton btnConfigure = new JButton("Configure");
		btnConfigure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(Component c : contentPane.getComponents()) {
					if( c instanceof JTextArea && ((JTextArea) c).isEditable()){
						JTextArea jTextArea = (JTextArea) c;
						jsonConfigSetUp.setHosts(jTextArea.getName(), jTextArea.getText());
						// System.out.println(jTextArea.getName() + " : " + jTextArea.getText());
					}
				}

				try {
					jsonConfigSetUp.writeConfig();
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
				}

				MainWindow mainWindow = new MainWindow();
				mainWindow.setVisible(true);
				dispose();
			}
		});
		btnConfigure.setBounds(370, 130, 115, 30);
		contentPane.add(btnConfigure);
	}
}
