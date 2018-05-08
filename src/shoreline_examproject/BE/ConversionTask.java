package shoreline_examproject.BE;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;
import javafx.beans.property.ReadOnlyDoubleWrapper;
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
    double count;  // The total number of data rows to convert, used to calculate the progress of the task.
    private LocalDateTime startTime;


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
        }
        catch (Exception ex) {
            EventLogger.log(EventLogger.Level.ERROR, String.format("Exception: %s", ex.getMessage()));
            throw ex;
        }

        return convertedData;
    }


    /**
     * Use the data stored inside the provided configuration to convert the
     * input data row-by row.
     */
    private void convert() throws NoSuchFieldException, InterruptedException {
        int c = 10000000;
        for (int i = 0; i < c; i++) {
            updateProgress((double)i/c, 1.0);
        }
        System.out.println("done");
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
}
