package shoreline_examproject.DAL.FileWriters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline_examproject.BE.AttributeMap;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.DataRow;
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
            for (DataRow datarow : data.getAttributes()) {
                jwriter.beginObject();
                for (AttributeMap attribute : datarow.getData()) {
                    writeObject(jwriter, attribute);
                }
                jwriter.endObject();
            }
            jwriter.endArray();
            
            EventLogger.log(EventLogger.Level.SUCCESS, "JSON writing was successful.");
            System.out.println("Writing was succesfully");
        }
        catch (Exception ex) {
            EventLogger.log(EventLogger.Level.ERROR, "Error during JSON file writing");
        }

    }
    
    private void writeObject(JsonWriter jwriter, AttributeMap data) throws IOException, NoSuchFieldException {
        jwriter.beginObject();
        if (data.isIsTreeRoot()) {
            jwriter.name(data.getKey());
            for (AttributeMap value : data.getValues()) {
                writeObject(jwriter, value);
            }
        } else {
            jwriter.name(data.getKey()).value(data.getValue());
        }
        jwriter.endObject();
    }
    
}
    
