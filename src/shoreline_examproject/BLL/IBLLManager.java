package shoreline_examproject.BLL;

import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.BE.EventLog;

/**
 *
 * @author sebok
 */
public interface IBLLManager
{
 
    public void saveLog(EventLog log);
    
    public void saveConfig(Config config);
    

    public AttributesCollection loadFileData(String filePath);
    
    public void convertData();
    
    public ConversionTask createConversionTask(Config c, AttributesCollection ac);
    
}
