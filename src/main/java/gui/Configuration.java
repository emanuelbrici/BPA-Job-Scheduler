package gui;

import java.awt.EventQueue;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 *
 * Created by Emanuel Brici on 10/30/15.
 */
public class Configuration {

	private JFrame frame;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Configuration window = new Configuration();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Configuration() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setTitle("Configuration File");
		frame.setBounds(100, 100, 420, 175);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Yes");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainWindow mainWindow = new MainWindow();
				mainWindow.setVisible(true);
			}
		});
		btnNewButton.setBounds(150, 90, 115, 30);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("No");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				UserName userName = new UserName();
				userName.setVisible(true);
			}
		});
		btnNewButton_1.setBounds(260, 90, 115, 30);
		frame.getContentPane().add(btnNewButton_1);
		
		JLabel lblConfigurationFile = new JLabel("Configuration file");
		lblConfigurationFile.setBounds(30, 25, 123, 21);
		frame.getContentPane().add(lblConfigurationFile);
		
		JTextArea txtrWouldYouLike = new JTextArea();
		txtrWouldYouLike.setBackground(UIManager.getColor("Button.background"));
		txtrWouldYouLike.setWrapStyleWord(true);
		txtrWouldYouLike.setLineWrap(true);
		txtrWouldYouLike.setEditable(false);
		txtrWouldYouLike.setText("Would you like to use a Configuration File that has already been made?");
		txtrWouldYouLike.setBounds(50, 50, 350, 40);
		frame.getContentPane().add(txtrWouldYouLike);
	}
}
