package gui;

import JobScheduler.JsonConfigSetUp;

import java.awt.EventQueue;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 *
 * Created by Emanuel Brici on 10/30/15.
 */
public class UserName extends JFrame {

	private JPasswordField passwordField;
	private JsonConfigSetUp jsonConfigSetUp;

	public UserName() {
		setTitle("Login");
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 200);
		this.setLayout(null);
		
		JLabel lblUserName = new JLabel("Username");
		lblUserName.setBounds(50, 25, 70, 15);
		this.add(lblUserName);
		
		final JTextArea textArea = new JTextArea();
		textArea.setBounds(125, 23, 215, 16);
		this.add(textArea);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(50, 50, 61, 15);
		this.add(lblPassword);
		
		JLabel lblLabRooms = new JLabel("Lab Rooms");
		lblLabRooms.setBounds(50, 75, 70, 15);
		this.add(lblLabRooms);
		
		JTextArea txtrIe = new JTextArea();
		txtrIe.setBackground(UIManager.getColor("Button.background"));
		txtrIe.setEditable(false);
		txtrIe.setText("i.e 323 325 225 223");
		txtrIe.setBounds(125, 95, 217, 15);
		this.add(txtrIe);
		
		final JTextArea textArea_2 = new JTextArea();
		textArea_2.setBounds(125, 75, 215, 16);
		this.add(textArea_2);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				String userName;
				userName = textArea.getText();
				String password;
				password = passwordField.getText();
				String roomNumbers = textArea_2.getText();
				jsonConfigSetUp = new JsonConfigSetUp(userName, password, roomNumbers.split(" "));
				ComputersToBeUsed labRooms = new ComputersToBeUsed(jsonConfigSetUp);
				labRooms.setVisible(true);
				dispose();
			}
		});
		btnOk.setBounds(300, 130, 115, 30);
		this.add(btnOk);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(121, 47, 222, 18);
		this.add(passwordField);
	}

}
