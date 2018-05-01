package shoreline_examproject.BE;

import java.util.concurrent.Callable;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

/**
 * This task represents a conversion to the given file format.
 * @author Bence
 */
public class ConversionTask implements Callable<AttributesCollection>{

    private final ReadOnlyDoubleWrapper progress = new ReadOnlyDoubleWrapper();
    private Config usedConfig; // The config that will be used to map the input values to the output values.
    private AttributesCollection inputData; 
    private AttributesCollection convertedData;
    double count;  // The total number of data rows to convert, used to calculate the progress of the task.

    public ConversionTask(Config usedConfig, AttributesCollection inputData, AttributesCollection convertedData)
    {
        this.usedConfig = usedConfig;
        this.inputData = inputData;
        this.convertedData = convertedData;
    }
    
    @Override
    public AttributesCollection call() throws Exception
    {
        try
        {
            convert();
        }
        catch (Exception ex)
        {
            //TODO: log here
            throw ex;
        }
        
        return convertedData;
    }
    
    
    public ReadOnlyDoubleProperty progressProperty()
    {
        return this.progress.getReadOnlyProperty();
    }
    
    public final double getProgress()
    {
        return progressProperty().get();
    }
    
    /**
     * Use the data stored inside the provided configuration to convert the input data row-by row.
     */
    private void convert()
    {
        convertedData = new AttributesCollection();
        count = inputData.getNumberOfDataEntries();
        double prog = 0;
        
        for (AttributeValueMap attributeValueMap : inputData.getAttributeValueMap()) {  // Iterate through all the attribute value maps (an AttributeValueMap equals a row of data in the .xlsx file).
            AttributeValueMap newAttributeValueMap = new AttributeValueMap();
            for (String attribute : attributeValueMap.getAttributes()) { // Iterate through the attribute-value pairs
                if (usedConfig.containsKey(attribute)) {    // If an attribute is found in the config, it has a corresponding, newer name.
                    newAttributeValueMap.addKeyValuePair(usedConfig.getValue(attribute), attributeValueMap.getValueBasedOnAttribute(attribute)); // Add the new name and the corresponding value to the new attlibute-value map.
                }
            }
            prog++;
            progress.set(count/prog);
            convertedData.addAttributeMap(newAttributeValueMap);
        }
    }
}
