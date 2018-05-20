package shoreline_examproject.BE;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;
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
    private final Object pauseLock = new Object();
    private Boolean isPaused = false;
    private Boolean isCanceled = false;
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
        convertedData = new AttributesCollection();
        int count = inputData.getNumberOfDataEntries();
        double progressPercentage = 0;
        int progress = 0;

        for (DataRow dataRow : inputData.getData()) {
            DataRow convertedRow = new DataRow();
            for (AttributeMap attributeMap : dataRow.getData()) {
                if (isCanceled) {
                    this.cancelled();
                    convertedData = null;
                    return;
                }
                if (isPaused) {
                    synchronized (pauseLock) {
                        try {
                            System.out.println("PAUSED");
                            pauseLock.wait();
                        }
                        catch (InterruptedException e) {
                        }
                    }
                }
                if (usedConfig.containsKey(attributeMap.getKey())) {
                        convertedRow.addData(convertMap(attributeMap));
                }
            }
            progress++;
            progressPercentage = (double) progress / count * 100;
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
    
    public void stop() {
        isCanceled = true;
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
        synchronized (pauseLock) {
            pauseLock.notify();
        }
    }

    public Boolean isPaused() {
        return isPaused;
    }

    public Boolean getIsCanceled() {
        return isCanceled;
    }
    
    /**
     * Convert the provided attribute map using the configuration.
     *
     * @param attributeMap The map that will be converted.
     * @return A converted AttributeMap.
     * @throws IllegalAccessException If a provided key to the configuration is
     * invalid.
     * @throws NoSuchFieldException If an AttributeMap invalidly presumed to be
     * a tree root.
     */
    private AttributeMap convertMap(AttributeMap attributeMap) throws IllegalAccessException, NoSuchFieldException {
        AttributeMap convertedMap = new AttributeMap();        
        if (!attributeMap.isIsTreeRoot()) {
            oldKey = attributeMap.getKey();
            value = attributeMap.getValue();

            newKey = usedConfig.getNewKey(oldKey);

                /*if (usedConfig.isPlanning(oldKey)) {
                convertedMap.setIsTreeRoot(true);
                convertedMap.setKey(newKey);
                convertedMap.addValue(value);
            }*/
            convertedMap.setKey(newKey);
            convertedMap.addValue(value);
        } else {
            for (AttributeMap value1 : attributeMap.getValues()) {
                AttributeMap nestedMap = convertMap(value1);
                convertedMap.addValue(nestedMap);
            }
        }
        return convertedMap;
    }

}
