package JobScheduler;

import gui.MainWindow;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Emanuel Brici on 10/24/15.
 * Created by Brayden Roth-White
 */

public class Executor {

    public ConcurrentHashMap<String, Node> activeNodes;
    public List<Node> idleNodes;
    public List<String> scripts;
    public MainWindow mainWindow;
    public String directory;

    public Executor(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.activeNodes = new ConcurrentHashMap<>();
        this.idleNodes = new ArrayList<>();
        this.directory = "Jobs";
    }

    /**
     * Main thread that shells out each script to each host.
     * @throws IOException
     * @throws InterruptedException
     */
    @SuppressWarnings("unchecked")
    public void execute() throws IOException, InterruptedException {
        // Grabs nodes from the configuration file and appends them to idleNodes
        List<Node> nodes = ConfigurationUtil.getNodeConfig(new File("Configuration.json"));
        idleNodes.addAll(nodes);

        logHeader();

        // Initialize first list: idle nodes
        DefaultListModel<String> idleNodesList = (DefaultListModel) mainWindow.idleHostNodes.getModel();
        idleNodesList.removeAllElements(); // Start with a clean list

        for (String s : getIdleHostNames()) { // Add host names
            idleNodesList.addElement(s);
        }

        // Runs SetUp
        Thread t = new Thread(new SetUp(nodes.get(0), "ls ~/"+directory+"/"));
        t.start();
        t.join();
        scripts = getScripts(); // Run getScripts

        // Initialize next list: scripts
        DefaultListModel<String> jobList = (DefaultListModel<String>) mainWindow.uncompletedJobs.getModel();
        jobList.removeAllElements(); // Start with a a clean list

        for (String s : scripts) { //Add scripts to jobList
            jobList.addElement(s);
        }

        while (true) { // Main loop that handles creating each thread
            String script = scripts.get(0); // Get the first script
            script = "python ~/"+directory+"/"+script; // Concatenated pathname to the script
            Node n = getNodeToDoWork(idleNodes);
            if(n != null) { // Check for an idle node
                new Thread(new Job(mainWindow, n, script, this)).start(); // New Thread and new Job with command and node
                scripts.remove(script.replace("python ~/"+directory+"/", "")); // Passed in the script remove it from the list
                jobList.removeElement(script.replace("python ~/"+directory+"/", ""));
            }
            Thread.sleep(25); // Wait a bit for new Job to append active and idle nodes
            if (scripts.size() == 0) { // Out of scripts then break
                break;
            }
        }
        System.out.println("Once the hosts return to idle, the program has finished.");
    }

    /**
     * Adds a header for a new logging session
     * @throws IOException
     */
    private void logHeader() throws IOException {
        PrintWriter out = new PrintWriter(new FileWriter("Log.txt", true));
        for(int i = 0; i<60; i++) {
            out.print("#");
        }
        out.print("\n");
        out.print("### Log file built on date: "+ new Date() +" ###\n");
        for(int i = 0; i<60; i++) {
            out.print("#");
        }
        out.print("\n");
        out.close();
    }

    /**
     * Creating a list of the literal string host
     * names rather than a node reference
     * @return ArrayList
     */
    public ArrayList<String> getIdleHostNames() {
        if (idleNodes.size() == 0) {
            return new ArrayList<>();
        }
        int temp = idleNodes.size();
        int i = 0;
        ArrayList<String> hostNameHolder = new ArrayList<>();
        while (temp > 0) {
            hostNameHolder.add(idleNodes.get(i).getHostName());
            temp--;
            i++;
        }
        return hostNameHolder;
    }


    /**
     * Grabs the first node in the List and returns
     * it to be used for a script.
     * @param nodes List<Node>
     * @return Node
     */
    private Node getNodeToDoWork(List<Node> nodes) {
        if (nodes.isEmpty()) return null;
        else return nodes.get(0);
    }

    /**
     * Reads through the text file with scripts listed and writes them
     * to a List for later use.
     * @return List
     * @throws IOException
     */
    private List<String> getScripts() throws IOException {
        List<String> scripts = new ArrayList<>(); // gets all files
        BufferedReader in;
        FileReader fr;

        fr = new FileReader("Jobs.txt");
        in = new BufferedReader(fr);
        String str;

        while ((str = in.readLine()) != null) {
            scripts.add(str);
        }

        in.close();
        fr.close();

        return scripts;
    }
}