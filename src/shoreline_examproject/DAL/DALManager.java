package shoreline_examproject.DAL;

import shoreline_examproject.DAL.FileReaders.FileReader;
import shoreline_examproject.BE.Config;
import java.io.File;
import java.util.List;

import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.DAL.FileReaders.FileReaderFactory;
import shoreline_examproject.Utility.EventLog;
import shoreline_examproject.DAL.DataBase.DBConfigManager;
import shoreline_examproject.DAL.DataBase.DBLogManager;
import shoreline_examproject.DAL.FileWriters.IFileWriter;
import shoreline_examproject.DAL.FileWriters.JSONWriter;
import shoreline_examproject.Utility.EventLogger;

/**
 * Provides access to file saving and loading.
 *
 * @author sebok
 */
public class DALManager implements IDataAccess {

    private IFileWriter writer = new JSONWriter();
    private FileReader reader;
    private DBLogManager DBLog = new DBLogManager();
    private DBConfigManager DBConfig = new DBConfigManager();
    private UserDataManager udmanager = new UserDataManager();

    public DALManager() {
        EventLogger.setUsername(udmanager.read());
        DBLog.loadLog();
    }

    @Override
    public void saveLog(EventLog log) {
        DBLog.saveLog(log);
    }

    @Override
    public void saveConfig(Config config) {
        DBConfig.saveConfig(config);
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

    @Override
    public List<Config> loadConfigs() {
        return DBConfig.loadConfigs();
    }

    @Override
    public void removeConfig(Config selected) {
        DBConfig.deleteConfig(selected);
    }
}
