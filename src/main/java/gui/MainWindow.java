package gui;

import JobScheduler.Executor;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 *
 * Created by Emanuel Brici on 10/30/15.
 */
public class MainWindow extends JFrame {

	private Executor executor;

    // ** Smaller Inner windows of the MainWindows ** //

	// Idle hosts
    public final static String IDLE_WINDOW_NAME = "Idle Nodes";
    public JList idleHostNodes; // idleNodes

	// Active hosts
	public final static String ACTIVE_WINDOW_NAME = "Active Nodes";
	public JList activeHostNodes; // activeNodes

	//Jobs to run
	public final static String UNCOMPLETED_JOBS_WINDOW_NAME = "UncompletedJobs";
	public JList uncompletedJobs; // jobs needed to run

	//Jobs running
	public final static String RUNNING_JOBS_WINDOW_NAME = "Running Jobs";
	public JList runningJobs; // running jobs

	//Completed Jobs
	public final static String COMPLETED_JOBS_WINDOW_NAME = "Jobs completed";
	public JList completedJobs; // completed jobs


    /**
	 * Create the frame.
	 */
    @SuppressWarnings("unchecked")
	public MainWindow() {
		this.executor = new Executor(this);
		setTitle("Job Scheduler");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 0, 1000, 750);
		this.setLayout(null);


		//Jobs to be done window
		uncompletedJobs = new JList();
		uncompletedJobs.setModel(new DefaultListModel());
		uncompletedJobs.setName(UNCOMPLETED_JOBS_WINDOW_NAME);
		uncompletedJobs.setBounds(50, 50, 450, 200);
		this.add(uncompletedJobs);

		//Jobs that are running window
		runningJobs = new JList();
		runningJobs.setModel(new DefaultListModel());
		runningJobs.setName(RUNNING_JOBS_WINDOW_NAME);
		runningJobs.setBounds(50, 275, 450, 200);
		this.add(runningJobs);

		//Jobs that are completed window
		completedJobs = new JList();
		completedJobs.setModel(new DefaultListModel());
		completedJobs.setName(COMPLETED_JOBS_WINDOW_NAME);
		completedJobs.setBounds(50, 500, 450, 200);
		this.add(completedJobs);

		//Active Window
		activeHostNodes = new JList();
		activeHostNodes.setModel(new DefaultListModel());
		activeHostNodes.setName(ACTIVE_WINDOW_NAME);
		activeHostNodes.setBounds(550, 50, 400, 275);
		this.add(activeHostNodes);

		//Idle Window
		idleHostNodes = new JList();
		idleHostNodes.setModel(new DefaultListModel());
		idleHostNodes.setName(IDLE_WINDOW_NAME);
		idleHostNodes.setBounds(550, 350, 400, 275);
		this.add(idleHostNodes);

		JButton btnRun = new JButton("Run");
		btnRun.setBounds(738, 660, 100, 30);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread runThread = new Thread() {
					public void run() {
						try {
							executor.execute();
						} catch (IOException | InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				runThread.start();
			}
		});
		this.add(btnRun);

		
		JButton btnQuit = new JButton("Quit");
		btnQuit.setBounds(850, 660, 100, 30);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		this.add(btnQuit);
		
		JLabel lblJobsToRun = new JLabel("Incomplete Jobs");
		lblJobsToRun.setBounds(50, 30, 120, 15);
		this.add(lblJobsToRun);
		
		JLabel lblNewLabel = new JLabel("Jobs Running");
		lblNewLabel.setBounds(50, 255, 95, 15);
		this.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Jobs Completed");
		lblNewLabel_1.setBounds(50, 480, 110, 15);
		this.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Active Hosts");
		lblNewLabel_2.setBounds(550, 30, 100, 15);
		this.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Idle Hosts");
		lblNewLabel_3.setBounds(550, 330, 100, 15);
		this.add(lblNewLabel_3);
		
		JButton btnBack = new JButton("Configure");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserName userName = new UserName();
				userName.setVisible(true);
				dispose();
				
			}
		});
		btnBack.setBounds(626, 660, 100, 30);
		this.add(btnBack);
	}
}
