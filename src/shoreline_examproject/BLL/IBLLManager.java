package shoreline_examproject.BLL;

import java.util.List;
import javafx.beans.property.BooleanProperty;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.Utility.EventLog;
import shoreline_examproject.BE.FolderInformation;

/**
 *
 * @author sebok
 */
public interface IBLLManager {

    public void saveLog(EventLog log);

    public void saveConfig(Config config);

    public List<Config> loadConfigs();

    public void saveToJSON(AttributesCollection data);

    public AttributesCollection loadFileData(String filePath);

    public void convertData();

    public ConversionTask createConversionTask(Config c, AttributesCollection ac);

    public void stopConversion(ConversionTask selectedItem);

    public void pauseConversion(ConversionTask selectedItem);

    public void registerFolder(FolderInformation fi) throws BLLException;
        
    public void updateFolderInformation(FolderInformation fi) throws BLLException;
    
    public void removeFolder(FolderInformation fi);

    public void startFolderWatch() throws BLLException;

    public BooleanProperty isWatching();

    public void removeConfig(Config selected);

}
