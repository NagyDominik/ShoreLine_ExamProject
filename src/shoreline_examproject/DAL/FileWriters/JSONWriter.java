package shoreline_examproject.DAL.FileWriters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline_examproject.BE.AttributeValueMap;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.Utility.EventLogger;

/**
 * Saves the given data to a JSON file
 *
 * @author sebok
 */
public class JSONWriter extends IFileWriter {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     *
     * @param data
     */
    @Override
    public void saveData(AttributesCollection data) {
        try (JsonWriter jwriter = gson.newJsonWriter(new BufferedWriter(new FileWriter(new File("output.json"))))) {
            jwriter.beginArray();
            for (AttributeValueMap asd : data.getAttributeValueMap()) {
                jwriter.beginObject();
                for (Map.Entry<String, String> selected : asd.getHashMap().entrySet()) {
                    jwriter.name(selected.getKey()).value(selected.getValue());
                }
                jwriter.endObject();
            }
            jwriter.endArray();
            
            EventLogger.log(EventLogger.Level.SUCCESS, "JSON writing was successful.");
            System.out.println("Writing was succesfully");
        }
        catch (IOException ex) {
            EventLogger.log(EventLogger.Level.ERROR, "Error during JSON file writing");
        }

    }

}
