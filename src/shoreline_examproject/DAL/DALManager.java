package shoreline_examproject.DAL;

import shoreline_examproject.DAL.FileReaders.FileReader;
import shoreline_examproject.BE.Config;
import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.DAL.FileReaders.FileReaderFactory;
import shoreline_examproject.BE.EventLog;
import shoreline_examproject.DAL.DataBase.DBLogManager;
import shoreline_examproject.DAL.FileWriters.IFileWriter;
import shoreline_examproject.DAL.FileWriters.JSONWriter;

/**
 * Provides access to file saving and loading.
 *
 * @author sebok
 */
public class DALManager implements IDataAccess {

    private IFileWriter writer = new JSONWriter();
    private FileReader reader;
    private DBLogManager dBlog;

    @Override
    public void saveLog(EventLog log) {
        try {
            dBlog.saveLog(log);
        } catch (DALException ex) {
            Logger.getLogger(DALManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void saveConfig(Config config) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AttributesCollection loadFileData(String filePath) {
        File file = new File(filePath);
        return loadFileData(file);
    }

    @Override
    public AttributesCollection loadFileData(File file) {

        reader = FileReaderFactory.CreateFileReader(file.getPath());
        return reader.getData(file);
    }

    @Override
    public void saveData(AttributesCollection data) {
        writer.saveData(data);
    }
}
