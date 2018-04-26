package shoreline_examproject.DAL;

import shoreline_examproject.DAL.FileReaders.FileReader;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import java.io.File;
import shoreline_examproject.Utility.EventLog;

/**
 * Provides access to file saving and loading.
 * @author sebok
 */
public class DALManager implements IDataAccess
{

    private FileReader reader;
    
    @Override
    public void saveLog(EventLog log)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveConfig(Config config)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AttributesCollection loadFileData(String filePath)
    {
        File file = new File(filePath);
        return loadFileData(file);
    }

    @Override
    public AttributesCollection loadFileData(File file)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
