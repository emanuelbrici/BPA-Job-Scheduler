package JobScheduler;

import com.jcraft.jsch.*;
import gui.MainWindow;
import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by Emamuel Brici on 10/17/15.
 * Appended by Brayden Roth White on 10/19/15
 */
public class Job implements Runnable {

    private Node node;
    private String command;
    private String buffer;
    private boolean failed;
    private boolean failedHost;
    private boolean complete;

    private Executor executor;
    private MainWindow mainWindow;

    public Job(MainWindow mainWindow, Node nodeToDoTheWork, String command, Executor executor) {
        this.node = nodeToDoTheWork;
        this.command = command;
        this.executor = executor;
        this.mainWindow = mainWindow;
        this.buffer = "";

        this.failed = false;
        this.failedHost = false;
        this.complete = false;
    }


    /**
     * Runnable method for threads created as object Job.
     * Connects to a session with ENCS network and prints output to console.
     * When unable to complete the command, if it's an issue with the host it will try again with a new host
     * if it's an issue with the command it will not retry the failed command.
     * See method "task" for more detail.
     */
    @SuppressWarnings("unchecked")
    public void run() {
        executor.activeNodes.put(this.command, this.node);

        DefaultListModel<String> runningJobsList = (DefaultListModel<String>) mainWindow.runningJobs.getModel();
        runningJobsList.addElement("Host: " + this.node.getHostName() + "    Running: " +
                this.command.replace("python ~/"+executor.directory+"/", ""));

        DefaultListModel<String> activeNodesList = (DefaultListModel<String>) mainWindow.activeHostNodes.getModel();
        activeNodesList.addElement(this.node.getHostName());

        DefaultListModel<String> idleNodesList = (DefaultListModel<String>) mainWindow.idleHostNodes.getModel();
        idleNodesList.removeElement(this.node.getHostName());

        DefaultListModel<String> scriptList = (DefaultListModel<String>) mainWindow.uncompletedJobs.getModel();

        Session session = null;
        try {
            JSch jsch = new JSch();

            session = jsch.getSession(node.getUserName(), node.getHostName(), node.getPort());
            session.setPassword(node.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection With Host " + node.getHostName() + "...");
            executor.idleNodes.remove(this.node);

            session.connect();

            task(session);

        } catch (JSchException e) {
            executor.idleNodes.remove(this.node);
            executor.activeNodes.remove(this.command, this.node);
            System.out.println("\n\u001B[31m[FAILED]\u001B[0m" + this.node.getHostName() + " not valid");
            failedHost = true;
            e.printStackTrace();
        } catch (IOException | InterruptedException e) {
            executor.idleNodes.remove(this.node);
            executor.activeNodes.remove(this.command, this.node);
            System.out.println("\n\u001B[31m[FAILED]\u001B[0m" + this.node.getHostName() + " not valid");
            e.printStackTrace();
        } finally {
            if(session != null && session.isConnected()) {
                session.disconnect();
            }

            if (complete) {
                executor.activeNodes.remove(this.command, this.node);
                executor.idleNodes.add(this.node);
                idleNodesList.addElement(this.node.getHostName());
                activeNodesList.removeElement(this.node.getHostName());

                DefaultListModel<String> completedList = (DefaultListModel<String>) mainWindow.completedJobs.getModel();
                completedList.addElement(this.command.replace("python ~/"+executor.directory+"/", "") +
                        "  Completion Time: " + new Date());
            } else if (failed) {
                executor.activeNodes.remove(this.command, this.node);
                executor.idleNodes.add(this.node);
                idleNodesList.addElement(this.node.getHostName());
                activeNodesList.removeElement(this.node.getHostName());

                scriptList.addElement("[FAILED] "+this.command.replace("python ~/"+executor.directory+"/",""));
            } else if (failedHost) {
                executor.scripts.add(this.command.replace("python ~/"+executor.directory+"/", ""));
                executor.activeNodes.remove(this.command, this.node);
                idleNodesList.addElement("[INVALID] " + this.node.getHostName());
                scriptList.addElement(this.command.replace("python ~/"+executor.directory+"/", ""));
                activeNodesList.removeElement(this.node.getHostName());
            }

            runningJobsList.removeElement("Host: "+ this.node.getHostName() + "    Running: " +
                    this.command.replace("python ~/"+executor.directory+"/",""));
        }
    }

    private void task(Session session) throws IOException, InterruptedException {
        Channel channel = null;
        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();

            channel.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    buffer += new String(tmp, 0, i);
                }
                PrintWriter out = new PrintWriter(new FileWriter("Log.txt", true));
                out.print(buffer); // Write the output to a text file for the machine to later read
                out.close();
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    if (channel.getExitStatus() != 0) {
                        System.out.println("\n\u001B[31m[FAILED]\u001B[0m exit-status: "
                                + channel.getExitStatus() + " " + this.command +
                                " failed on " + this.node.getHostName());
                        failed = true;
                    } else {
                        System.out.println("\u001B[32m[SUCCESS]\u001B[0m exit-status: " + channel.getExitStatus()
                                + " " + this.command + " successful with " + this.node.getHostName());
                        complete = true;
                    }
                    break;
                }
                Thread.sleep(250);
            }
        } catch (JSchException e) {

            e.printStackTrace();
        } finally {
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
        }
    }
}
