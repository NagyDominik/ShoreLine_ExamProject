package shoreline_examproject.GUI.Model;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.BE.FolderInformation;
import shoreline_examproject.BLL.BLLException;
import shoreline_examproject.BLL.BLLManager;
import shoreline_examproject.BLL.IBLLManager;

/**
 *
 * @author Bence
 */
public class Model {

    private static Model instance;

    private final IBLLManager bllManager;
    private String currentUser;
    private AttributesCollection currentAttributes; //The attributes of the currently loaded file.  
    private ObservableList<ConversionTask> tasks = FXCollections.observableArrayList();
    private ObservableList<Config> confList = FXCollections.observableArrayList();

    private ConversionTask currentConversionTask;

    private Config selected;
    private boolean configEdit;

    private ObservableList<FolderInformation> monitoredFolders = FXCollections.observableArrayList();

    private Model() throws ModelException {
        try {
            bllManager = new BLLManager();
            confList.addAll(bllManager.loadConfigs());
        } catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    public static Model getInstance() throws ModelException {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }

    public void loadFileData(String path) {
        currentAttributes = bllManager.loadFileData(path);
    }

    public void startConversion() throws ModelException {
        bllManager.convertData();
    }

    public AttributesCollection getCurrentAttributes() throws ModelException {
        if (currentAttributes == null) {
            throw new ModelException("Current attributes is not set!");
        }
        return currentAttributes;
    }

    /**
     * Save the given configuration to the database.
     *
     * @param currentConfig The configuration that will be saved.
     */
    public void saveConfig(Config currentConfig) {
        this.confList.add(currentConfig);
        bllManager.saveConfig(currentConfig);
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public String setCurrentUser(String user) {
        return this.currentUser = user;
    }

    public ObservableList<ConversionTask> getTasks() {
        return tasks;
    }

    public void createNewConversionTask(Config value) throws ModelException {
        if (currentAttributes == null) {
            throw new ModelException("currentAttribute null! It is possible that no file was selected");
        }
        tasks.add(bllManager.createConversionTask(value, currentAttributes));
        System.out.println("tasks list size: " + tasks.size());
    }

    public ConversionTask getSelectedTask() {
        return currentConversionTask;
    }

    public void setSelectedTask(ConversionTask task) {
        this.currentConversionTask = task;
    }

    public void stopConversion(ConversionTask selectedItem) {
        bllManager.stopConversion(selectedItem);
        tasks.remove(selectedItem);
    }

    public void pauseConverion(ConversionTask selectedItem) {
        bllManager.pauseConversion(selectedItem);
    }

    public ObservableList<Config> getConfList() {
        return confList;
    }

    public void setSelectedConfig(Config selected) {
        this.selected = selected;
    }

    public Config getSelectedConfig() {
        return selected;
    }

    public void setConfigEdit(boolean b) {
        this.configEdit = b;
    }

    public boolean isConfigEdit() {
        return configEdit;
    }

    public ObservableList<FolderInformation> getMonitoredFolders() {
        return monitoredFolders;
    }

    public void addFolderToList(FolderInformation fi) throws ModelException {
        try {
            this.monitoredFolders.add(fi);
            bllManager.assignFolder(fi);
        } catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }
    
    public void changeMonitoring() {
        bllManager.changeMonitoring();
    }
    
    public BooleanProperty isMonitoring() {
        return bllManager.isMonitoring();
    }

    public void removeFolder(FolderInformation selected) throws ModelException{
        try {
            this.bllManager.removeFolder(selected);
            this.monitoredFolders.remove(selected);
        } catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }
}
