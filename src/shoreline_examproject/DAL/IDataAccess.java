package shoreline_examproject.DAL;

import shoreline_examproject.BLL.Config;
import shoreline_examproject.Utility.EventLog;

/**
 * Defines an interface that can be used by the BLL to save and load data.
 * @author sebok
 */
public interface IDataAccess
{
    public void saveLog(EventLog log);
    
    public void saveConfig(Config config);
}
