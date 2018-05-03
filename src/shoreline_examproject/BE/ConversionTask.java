package shoreline_examproject.BE;

import java.util.concurrent.Callable;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import shoreline_examproject.Utility.EventLogger;

/**
 * This task represents a conversion to the given file format.
 *
 * @author Bence
 */
public class ConversionTask implements Callable<AttributesCollection> {

    private final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper(0.5);
    private Config usedConfig; // The config that will be used to map the input values to the output values.
    private AttributesCollection inputData;
    private AttributesCollection convertedData;
    double count;  // The total number of data rows to convert, used to calculate the progress of the task.

    public ConversionTask(Config usedConfig, AttributesCollection inputData)
    {
        this.usedConfig = usedConfig;
        this.inputData = inputData;
    }

    public ConversionTask()
    {
    }

    @Override
    public AttributesCollection call() throws Exception {
        try {
            convert();
        }
        catch (Exception ex) {
            EventLogger.log(EventLogger.Level.ERROR, String.format("Exception: %s", ex.getMessage()));
            throw ex;
        }

        return convertedData;
    }

    public ReadOnlyDoubleProperty progressProperty() {
        return this.progress.getReadOnlyProperty();
    }

    public final double getProgress() {
        return progressProperty().get();
    }

    /**
     * Use the data stored inside the provided configuration to convert the
     * input data row-by row.
     */
    private void convert() throws NoSuchFieldException {
        convertedData = new AttributesCollection();
        count = inputData.getNumberOfDataEntries();
        
        double prog = 0;

        for (DataRow dataRow : inputData.getData()) {
            DataRow converted = new DataRow();
            for (AttributeMap attributeMap : dataRow.getData()) {
                converted.addData(convertMap(attributeMap));
            }
            prog++;
            progress.set(prog/count); // Count progress by counting the total number of rows converted.
        }
    }

    /**
     * Recursively convert a data row.
     * @param attributeMap
     * @return
     * @throws NoSuchFieldException 
     */
    private AttributeMap convertMap(AttributeMap attributeMap) throws NoSuchFieldException
    {
        AttributeMap convertedAttributeMap = null;
        if (!attributeMap.isIsTreeRoot()) {
            convertedAttributeMap = new AttributeMap(usedConfig.getValue(attributeMap.getKey()), false);
            convertedAttributeMap.setValue(attributeMap.getValue());
        }
        else {
            convertedAttributeMap = new AttributeMap(usedConfig.getValue(attributeMap.getKey()), true);
            for (AttributeMap value : attributeMap.getValues()) {
                convertedAttributeMap.addValue(convertMap(value));
            }
        } 
        
        return convertedAttributeMap;
    }

    public void setInput(AttributesCollection input)
    {
        this.inputData = input;
    }

    public void setConfig(Config config)
    {
        this.usedConfig = config;
    }
    
    public String getConfigName()
    {
        return this.usedConfig.getName();
    }
}
