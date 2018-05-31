package shoreline_examproject.BLL;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.Utility.EventLog;
import shoreline_examproject.BE.FolderInformation;
import shoreline_examproject.BLL.Conversion.Converter;
import shoreline_examproject.BLL.Conversion.FolderConverter;
import shoreline_examproject.DAL.DALManager;
import shoreline_examproject.DAL.IDataAccess;

public class BLLManager implements IBLLManager {

    private IDataAccess dalManager;
    private Converter converter;
    private FolderHandler folderHandler;
    private FolderConverter folderConverter;

    public BLLManager() throws BLLException {
        try {
            this.dalManager = new DALManager();
            this.converter = new Converter(this);
            this.folderHandler = new FolderHandler(this);
            this.folderConverter = new FolderConverter(this);
        }
        catch (IOException ex) {
            throw new BLLException(ex);
        }
    }

    @Override
    public void saveLog(EventLog log) {
        dalManager.saveLog(log);
    }

    @Override
    public void saveConfig(Config config) {
        dalManager.saveConfig(config);
    }

    @Override
    public List<Config> loadConfigs() {
        return dalManager.loadConfigs();
    }

    @Override
    public AttributesCollection loadFileData(String filePath) {
        return dalManager.loadFileData(filePath);
    }

    @Override
    public void saveToJSON(AttributesCollection data) {
        dalManager.saveData(data);
    }

    @Override
    public void convertData() {
        converter.convertAll();
    }

    @Override
    public ConversionTask createConversionTask(Config c, AttributesCollection ac) {
        return converter.createConversionTask(c, ac);
    }

    @Override
    public void stopConversion(ConversionTask selectedItem) {
        converter.stopTask(selectedItem);
    }

    @Override
    public void pauseConversion(ConversionTask selectedItem) {
        converter.pauseTask(selectedItem);
    }

    @Override
    public void removeConfig(Config selected) {
        dalManager.removeConfig(selected);
    }

    @Override
    public void registerFolder(FolderInformation fi) throws BLLException {
        try {
            folderHandler.registerDirectory(fi);
        }
        catch (IOException ex) {
            throw new BLLException(ex);
        }
    }

    @Override
    public void startFolderWatch() throws BLLException {
        try {
            folderHandler.startMonitoring();
        }
        catch (InterruptedException ex) {
            throw new BLLException(ex);
        }
    }

    @Override
    public BooleanProperty isWatching() {
        return folderHandler.isRunningProperty();
    }

    @Override
    public void removeFolder(FolderInformation fi) {
        folderHandler.removeFolder(fi);
    }

    public void addNewFileToFolderConverter(Path p, FolderInformation fi) throws InterruptedException {
        folderConverter.addConversionTask(p, fi);
    }

}
