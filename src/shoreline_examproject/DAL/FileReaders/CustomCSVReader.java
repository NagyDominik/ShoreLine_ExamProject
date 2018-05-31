package shoreline_examproject.DAL.FileReaders;

import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import shoreline_examproject.BE.AttributeMap;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.DataRow;
import shoreline_examproject.Utility.EventLogger;

/**
 * Reads data from an CSV file
 *
 * @author Dominik
 */
public class CustomCSVReader extends CustomFileReader {

    private AttributesCollection loadedData = new AttributesCollection();
    private List<String> attributes = new ArrayList();
    private int rowCount = 1;
    private int repeatcount = 2;
    private String repeatingAttribute;

    @Override
    public AttributesCollection getData(File file) {
        loadedData.setImportPath(file.getPath());
        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] row;
            while ((row = reader.readNext()) != null) {
                DataRow datarow = new DataRow();
                if (rowCount == 1) {
                    for (String string : row) {
                        checkIfExists(string);
                        attributes.add(repeatingAttribute);
                    }
                } else {
                    for (int i = 0; i < row.length; i++) {
                        AttributeMap temp = new AttributeMap(attributes.get(i), false);
                        temp.addValue(row[i]);
                        datarow.addData(temp);
                    }
                    loadedData.addAttributeMap(datarow);
                }
                rowCount++;
            }
        }
        catch (IOException ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
        }
        return loadedData;
    }

    private void checkIfExists(String attribute) {
        if (repeatcount == 2) {
            repeatingAttribute = attribute;
        }
        if (attributes.contains(attribute)) {
            attribute = repeatingAttribute + "(" + repeatcount + ")";
            repeatcount++;
            checkIfExists(attribute);
        } else {
            repeatingAttribute = attribute;
        }
        repeatcount = 2;
    }

}
