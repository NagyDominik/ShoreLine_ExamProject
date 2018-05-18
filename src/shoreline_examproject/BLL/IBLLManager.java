package shoreline_examproject.BLL;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.BE.EventLog;
import shoreline_examproject.BE.FolderInformation;

/**
 *
 * @author sebok
 */
public interface IBLLManager
{
 
    public void saveLog(EventLog log);
    
    public void saveConfig(Config config);
    
    public void saveToJSON(AttributesCollection data);

    public AttributesCollection loadFileData(String filePath);
    
    public void convertData();
    
    public ConversionTask createConversionTask(Config c, AttributesCollection ac);

    public void stopConversion(ConversionTask selectedItem);

    public void pauseConversion(ConversionTask selectedItem);
    
    public void assignFolder(FolderInformation fi) throws BLLException;
    
    public void changeMonitoring();
    
    public BooleanProperty isMonitoring();

    public void removeFolder(FolderInformation selected) throws BLLException;
}
