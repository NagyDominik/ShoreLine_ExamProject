package shoreline_examproject.DAL.FileWriters;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.sun.org.apache.bcel.internal.generic.AALOAD;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline_examproject.BE.AttributeValueMap;
import shoreline_examproject.BE.AttributesCollection;

/**
 * Saves the given data to a JSON file
 *
 * @author sebok
 */
public class JSONWriter extends IFileWriter {

    private Gson gson = new Gson();

    public void saveData(AttributesCollection data) {
        data.getAttributeValueMap();
        /*for each attribute valuemappon megy hozzáadja az objecthez, filewriter private function 
                getattriubte value length lista hoszáif 
                object 
                belső bele és kiírja
                buffer writer            */

 /*String siteName = "";
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
        jsonObj.add("sitename",gson.toJsonTree(siteName));
        
        System.out.println(jsonObj.toString());*/
        FileWriter writer;
        try {
            writer = new FileWriter(new File("output.json"));
            JsonWriter jsonWriter = new JsonWriter(new BufferedWriter(writer));
            jsonWriter.beginObject();
            for (AttributeValueMap asd : data.getAttributeValueMap()) {
                JsonObject output = new JsonObject();
                    for (Map.Entry<String, String> selected : asd.getHashMap().entrySet()) {
                        output.add(selected.getKey(), gson.toJsonTree(selected.getValue()));
                    }
            jsonWriter.endObject();
                System.out.println("write was succesfully");
        }
        } catch (IOException ex) {
            Logger.getLogger(JSONWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
