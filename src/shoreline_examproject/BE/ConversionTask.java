package shoreline_examproject.BE;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import shoreline_examproject.Utility.EventLogger;

/**
 * This task represents a conversion to the given file format.
 *
 * @author Bence
 */
public class ConversionTask extends Task implements Callable<AttributesCollection> {

    private Config usedConfig; // The config that will be used to map the input values to the output values.
    private AttributesCollection inputData;
    private AttributesCollection convertedData;
    private LocalDateTime startTime;
    private Object pauseLock = new Object();
    private String oldKey = "", newKey = "", value = "";

    public ConversionTask(Config usedConfig, AttributesCollection inputData) {
        this.usedConfig = usedConfig;
        this.inputData = inputData;
        this.startTime = LocalDateTime.now();
        this.updateProgress(0, 100);
    }

    public ConversionTask() {
        this.startTime = LocalDateTime.now();
        this.updateProgress(0, 100);
    }

    @Override
    public AttributesCollection call() throws Exception {
        try {
            convert();
            return convertedData;
        }
        catch (Exception ex) {
            EventLogger.log(EventLogger.Level.ERROR, String.format("Exception: %s", ex.getMessage()));
            throw ex;
        }
    }

    /**
     * Use the data stored inside the provided configuration to convert the
     * input data row-by row.
     */
    private void convert() throws NoSuchFieldException, InterruptedException, IllegalAccessException {
//        int c = 10000000;
//        for (int i = 0; i < c; i++) {
//            if (!Thread.currentThread().isInterrupted()) {
//                synchronized (pauseLock) {
//                    updateProgress((double) i / c, 1.0);
//                }
//            }
//        }
//        System.out.println("done");

        convertedData = new AttributesCollection();
        int count = inputData.getNumberOfDataEntries();
        double progressPercentage = 0;

        System.out.println(count);
        int progress = 0;
        for (DataRow dataRow
                : inputData.getData()) {
            DataRow convertedRow = new DataRow();
            for (AttributeMap attributeMap : dataRow.getData()) {
                convertedRow.addData(convertMap(attributeMap));
            }
            progress++;
            progressPercentage = (double) progress / count * 100;
            System.out.println(progressPercentage);
            updateProgress(progressPercentage, count);
            convertedData.addAttributeMap(convertedRow);
        }
    }

    public void setInput(AttributesCollection input) {
        this.inputData = input;
    }

    public void setConfig(Config config) {
        this.usedConfig = config;
    }

    public String getConfigName() {
        return this.usedConfig.getName();
    }

    public AttributesCollection getInputData() {
        return inputData;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeAsString() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
        return startTime.format(format);
    }

    public void pause() {

    }

    public void resume() {

    }

    private AttributeMap convertMap(AttributeMap attributeMap) throws IllegalAccessException, NoSuchFieldException {
        AttributeMap convertedMap = new AttributeMap();
        if (usedConfig.containsKey(attributeMap.getKey())) {
            if (!attributeMap.isIsTreeRoot()) {
                oldKey = attributeMap.getKey();
                value = attributeMap.getValue();

                newKey = usedConfig.getNewKey(oldKey);

                convertedMap.setKey(newKey);
                convertedMap.addValue(value);
            } else {
                for (AttributeMap value1 : attributeMap.getValues()) {
                    AttributeMap nestedMap = convertMap(value1);
                    convertedMap.addValue(nestedMap);
                }
            }
        }

        return convertedMap;
    }
}
