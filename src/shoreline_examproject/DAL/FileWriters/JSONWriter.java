package shoreline_examproject.DAL.FileWriters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.converter.LocalDateTimeStringConverter;
import org.apache.poi.ss.usermodel.DateUtil;
import shoreline_examproject.BE.AttributeMap;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.DataRow;
import shoreline_examproject.Utility.EventLogger;

/**
 * Saves the given data to a JSON file
 *
 * @author Dominik
 */
public class JSONWriter extends IFileWriter {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private int filecount = 1;
    private Object lock = new Object();
    private String importPath;

    /**
     *
     * @param data
     */
    @Override
    public void saveData(AttributesCollection data) {
        synchronized (lock) {
            importPath = data.getImportPath();
            File output = new File(getNextFilePath("exporttest/ConvertedData_" + LocalDate.now() + "_" + System.currentTimeMillis() + ".json_temp"));
            output.getParentFile().mkdirs();
            try (JsonWriter jwriter = gson.newJsonWriter(new BufferedWriter(new FileWriter(output)))) {
                jwriter.beginArray();
                for (DataRow datarow : data.getAttributes()) {
                    jwriter.beginObject();
                    for (AttributeMap attribute : datarow.getData()) {
                        writeObject(jwriter, attribute);
                    }
                    jwriter.endObject();
                }
                jwriter.endArray();

            }
            catch (Exception ex) {
                EventLogger.log(EventLogger.Level.ERROR, "An exception has occured: " + ex.getMessage());
                System.out.println(ex);
            }

            try {//idekelll
                Path temp = Paths.get(output.getPath());
                if (data.getExportPath() != null) {
                    Path saveExportLocation = Paths.get(data.getExportPath());
                    Files.move(temp, saveExportLocation, StandardCopyOption.ATOMIC_MOVE);
                    //System.out.println(saveExportLocation);
                } else {
                    Path done = Paths.get(output.getPath().substring(0, output.getPath().lastIndexOf("_")));
                    Files.move(temp, done, StandardCopyOption.ATOMIC_MOVE);
                    System.out.println(done);
                }
                
                EventLogger.log(EventLogger.Level.SUCCESS, "JSON writing was successful.");
                System.out.println("Writing was succesful.");
            }
            catch (IOException ex) {
                Logger.getLogger(JSONWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void writeObject(JsonWriter jwriter, AttributeMap data) throws Exception {
        if (data.isIsTreeRoot()) {
            jwriter.name(data.getKey());
            jwriter.beginObject();
            for (AttributeMap v : data.getValues()) {
                writeObject(jwriter, v);
            }
            jwriter.endObject();
        } else {
            if ((data.getKey().endsWith("Date") || data.getKey().endsWith("Time")) && !data.getValue().isEmpty()) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                if (importPath.endsWith(".xlsx")) {
                    Date date = DateUtil.getJavaDate(Double.parseDouble(data.getValue()), TimeZone.getTimeZone("UTC"));
                    jwriter.name(data.getKey()).value(df.format(date));
                } else {
                    DateFormat pareseformat = new SimpleDateFormat("yyyy-MM-dd");
                    pareseformat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date date = pareseformat.parse(data.getValue());
                    jwriter.name(data.getKey()).value(df.format(date));
                }
            } else {
                jwriter.name(data.getKey()).value(data.getValue());
            }
        }
    }

    private String getFileName(String path) {
        return path.substring(path.lastIndexOf("\\"));
    }

    private String getNextFilePath(String path) {
        File output = new File(path);
        if (output.exists()) {
            getNextFilePath("exporttest/ConvertedData_" + LocalDate.now() + "_" + System.currentTimeMillis() + filecount + ".json_temp");
            filecount++;
        }
        return output.getPath();
    }
}
