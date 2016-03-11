package JobScheduler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.forgerock.json.JsonValue;

/**
 *
 * Created by Emanuel Brici on 10/30/15.
 */
@SuppressWarnings("unused")
public class JsonConfigSetUp {

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String[] getLabRooms() {
        return labRooms;
    }

    public void setLabRooms(String[] labRooms) {
        this.labRooms = labRooms;
    }

    public Set<String> getHosts() {
        return hosts;
    }

    public void setHosts(Set<String> hosts) {
        this.hosts = hosts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String userName;
    private String[] labRooms;
    private Set<String> hosts = new HashSet<>();
    private String password;

    public JsonConfigSetUp(String userName, String password, String[] labRooms) {
        this.userName = userName;
        this.password = password;
        this.labRooms = labRooms;
    }

    /**
     * Concatenates host name to be in the following order
     * "ecs" + room-number + "-" + comp-number + ".labs.encs"
     *
     * @param computerIds computer numbers to use in the lab.
     */
    public void setHosts(String labRoom, String computerIds) {
        String[] strings = computerIds.split(" ");
        int i = 0;
        for (String str : strings) {
            if(str.contains("-")) {
                String[] temp = str.split("-");
                int x = Integer.parseInt(temp[0]);
                int y = Integer.parseInt(temp[1]);
                for(; x <= y; x++) {
                    String startOfHosts = "ecs" + labRoom + "-";
                    String FinalHostName = startOfHosts + x + ".labs.encs";
                    hosts.add(FinalHostName);
                }
            } else {
                int x = Integer.parseInt(str);
                String startOfHosts = "ecs" + labRoom + "-";
                String FinalHostName = startOfHosts + x + ".labs.encs";
                hosts.add(FinalHostName);
            }
            i++;
        }
    }

    /**
     * Creates the configuration .json file with host names, username, and password
     *
     * @return JsonValue
     */
    private JsonValue configToJson() {
        JsonValue object = new JsonValue(new LinkedHashMap<String, Object>());
        object.put("username", userName);
        object.put("password", password);

        List<Map<String, Object>> list = new ArrayList<>();

        for (String host : hosts) {
            Map<String, Object> h = new HashMap<>();
            h.put("host", host);
            h.put("port", 22);
            list.add(h);
        }
        object.put("nodes", list);
        return object;
    }

    /**
     * Writes the .json file with the information provided from user
     *
     * @throws JsonProcessingException
     */
    public void writeConfig() throws JsonProcessingException {
        JsonValue config = configToJson();
        try {
            FileWriter file = new FileWriter("Configuration.json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(config.getObject());
            file.write(json);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}





