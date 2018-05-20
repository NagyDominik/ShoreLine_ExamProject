package shoreline_examproject.BLL;

import java.io.IOException;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.BE.EventLog;
import shoreline_examproject.BE.FolderInformation;
import shoreline_examproject.BLL.Conversion.Converter;
import shoreline_examproject.DAL.DALManager;
import shoreline_examproject.DAL.IDataAccess;

public class BLLManager implements IBLLManager {

    private IDataAccess dalManager;
    private Converter converter;
    private FolderHandler folderHandler;
    
    public BLLManager() throws BLLException {
        try {
            this.dalManager = new DALManager();
            this.converter = new Converter(this);
            this.folderHandler = new FolderHandler(this);
        } catch (IOException ex) {
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
    public ConversionTask createConversionTask(Config c, AttributesCollection ac)
    {
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
    public void assignFolder(FolderInformation fi) throws BLLException{
        try {
            folderHandler.addFolderInformation(fi);
        } catch (IOException ex) {
            throw new BLLException(ex);
        }
    }

    @Override
    public void changeMonitoring() {
        folderHandler.changeMonitoring();
    }

    @Override
    public BooleanProperty isMonitoring() {
        if (folderHandler == null) {
            throw new NullPointerException("Folder Handler has not been initialized yet");
        }
        
        return folderHandler.isMonitoringProperty();
    }

    @Override
    public void removeFolder(FolderInformation selected) throws BLLException{
        try {
            folderHandler.removeFolder(selected);
        } catch (IOException ex) {
            throw new BLLException(ex);
        }
    }

}
