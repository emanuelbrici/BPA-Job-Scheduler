package JobScheduler;

import org.forgerock.http.util.Json;
import org.forgerock.json.JsonValue;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Emanuel Brici on 10/17/2015.
 * Appended by Brayden Roth-White on 10/17/2015.
 */

public class ConfigurationUtil {

    /**
     * Creates nodes with username password and hostname.
     *
     * @param f File
     * @return List
     */
    public static List<Node> getNodeConfig(File f) {
        try {
            List<Node> nodes = new ArrayList<>();
            JsonValue config = new JsonValue(Json.readJson(
                    new FileReader(f)));
            String username = config.get("username").asString();
            String password = config.get("password").asString();
            for (Map node : config.get("nodes").asList(Map.class)) {
                nodes.add(new Node((String) node.get("host"), (Integer) node.get("port"), username, password));
            }
            return nodes;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
