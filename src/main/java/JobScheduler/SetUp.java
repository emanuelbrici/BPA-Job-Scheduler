package JobScheduler;

import com.jcraft.jsch.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * Created by Emanuel Brici on 10/24/15.
 * Created by Brayden Roth-White on 10/24/15
 */
public class SetUp implements Runnable {

    private Node node;
    private String command;
    String buffer;

    public SetUp(Node node, String command) {
        this.node = node;
        this.command = command;
    }

    /**
     * Runnable method for threads created as object SetUp.
     * Connects the session and disconnects the session here
     */
    public void run() {
        Session session = null;

        try {
            JSch jsch = new JSch();

            session = jsch.getSession(node.getUserName(), node.getHostName(), node.getPort());
            session.setPassword(node.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connected, Retrieving Files...");

            getTasks(session);

        } catch (Exception e) {
            System.out.println(this.command + " failed on " + this.node.getHostName());
            e.printStackTrace();
        } finally {
            if (session != null && session.isConnected())
                session.disconnect();
        }

    }

    /**
     * Connects to a session with ENCS network and stores output in text files for later use.
     * When unable to complete the command it will exit out as the user has most likely tried to
     * use a host machine not booted to linux.
     * Connects the channel and disconnects the channel here
     *
     * Will create a list of scripts in a specified directory.
     */
    private void getTasks(Session session) throws IOException, InterruptedException {
        Channel channel = null;

        try {
            channel = session.openChannel("exec"); // Channel setup to be executable
            ((ChannelExec) channel).setCommand(command); // Command to be used is given

            channel.setInputStream(null); // Set input stream
            ((ChannelExec) channel).setErrStream(System.err); // Set err stream

            InputStream in = channel.getInputStream();

            channel.connect();
            byte[] tmp = new byte[1024];

            while (true) {
                while (in.available() > 0) { // While input stream is available read the output from the command
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    buffer = new String(tmp, 0, i);
                    PrintWriter out = new PrintWriter("Jobs.txt");
                    out.print(buffer); // Write the output to a text file for the machine to later read
                    out.close();
                }
                if (channel.isClosed()) {
                    if (in.available() > 0) continue;
                    break;
                }
                Thread.sleep(250);
            }

        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            if(channel != null && channel.isConnected())
                channel.disconnect();
        }
    }
}

