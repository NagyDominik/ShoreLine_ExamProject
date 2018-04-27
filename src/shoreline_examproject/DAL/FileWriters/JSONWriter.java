package shoreline_examproject.DAL.FileWriters;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.time.Instant;
import java.util.Date;

/**
 * Saves the given data to a JSON file
 *
 * @author sebok
 */
public class JSONWriter extends FileWriter {

    public void saveData(String data) {
        Gson gson = new Gson();
        
        String siteName = "";
        String assetSerialNumber = "";
        String type = "ZCS5";
        JsonObject jsonObj = new JsonObject();
        JsonArray jsonArr = new JsonArray();
        JsonObject planning = new JsonObject();
        Date date = Date.from(Instant.now());
        planning.add("Date1", gson.toJsonTree(date));
        planning.add("Date2", gson.toJsonTree(date));
        planning.add("Date3", gson.toJsonTree(date));
        jsonObj.add("type", gson.toJsonTree(type));
        jsonObj.add("planning", planning);
        System.out.println(jsonObj.toString());
    }

}
