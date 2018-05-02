package shoreline_examproject.DAL;

import shoreline_examproject.BE.Config;
import java.io.File;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.EventLog;

/**
 * Defines an interface that can be used by the BLL to save and load data.
 *
 * @author sebok
 */
public interface IDataAccess {

    public void saveLog(EventLog log);

    public void saveConfig(Config config);
    
    public void saveData(AttributesCollection data);

    public AttributesCollection loadFileData(String filePath);

    public AttributesCollection loadFileData(File file);
}
