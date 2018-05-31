package shoreline_examproject.GUI.Model;

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

    private ObservableList<ConversionTask> tasks = FXCollections.observableArrayList();
    private ObservableList<Config> confList = FXCollections.observableArrayList();
    private ObservableList<FolderInformation> monitoredFolders = FXCollections.observableArrayList();

    private final IBLLManager bllManager;
    private String currentUser;
    private AttributesCollection currentAttributes; //The attributes of the currently loaded file.  
    private ConversionTask currentConversionTask;
    private Config selected;

    private Model() throws ModelException {
        try {
            bllManager = new BLLManager();
            confList.addAll(bllManager.loadConfigs());
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    /**
     * GETTERS AND SETTERS------------------------------------------------------
     */
    public static Model getInstance() throws ModelException {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
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

    public ConversionTask getSelectedTask() {
        return currentConversionTask;
    }

    public void setSelectedTask(ConversionTask task) {
        this.currentConversionTask = task;
    }

    public ObservableList<Config> getConfList() {
        return confList;
    }

    public Config getSelectedConfig() {
        return selected;
    }

    public void setSelectedConfig(Config selected) {
        this.selected = selected;
    }

    public AttributesCollection getCurrentAttributes() throws ModelException {
        if (currentAttributes == null) {
            throw new ModelException("Current attributes is not set!");
        }
        return currentAttributes;
    }

    public ObservableList<FolderInformation> getMonitoredFolders() {
        return monitoredFolders;
    }

    public BooleanProperty getIsWatching() {
        return this.bllManager.isWatching();
    }

    /**
     * DATA HANDLING FUNCTIONS--------------------------------------------------
     */
    /**
     * Attempts to load data from the file at the provided location
     *
     * @param path file path
     */
    public void loadFileData(String path) {
        currentAttributes = bllManager.loadFileData(path);
    }

    /**
     * Saves the given configuration to the database.
     *
     * @param currentConfig The configuration that will be saved.
     */
    public void saveConfig(Config currentConfig) {
        this.confList.add(currentConfig);
        bllManager.saveConfig(currentConfig);
    }

    /**
     * Removes the provided config from the database
     *
     * @param selected The config that will be removed
     */
    public void removeConfig(Config selected) {
        this.bllManager.removeConfig(selected);
        this.confList.remove(selected);
    }

    /**
     * Adds the provided folder to the watchlist of the FolderConverter
     *
     * @param fi INformations about the folder
     * @throws ModelException
     */
    public void registerDirectory(FolderInformation fi) throws ModelException {
        try {
            this.bllManager.registerFolder(fi);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    /**
     * Removes the provided folder from the watchlist of the FolderConverter
     *
     * @param fi INformations about the folder
     */
    public void removeDirectory(FolderInformation fi) {
        bllManager.removeFolder(fi);
    }

    public void createNewConversionTask(Config value) throws ModelException {
        if (currentAttributes == null) {
            throw new ModelException("currentAttribute null! It is possible that no file was selected");
        }
        tasks.add(bllManager.createConversionTask(value, currentAttributes));
        System.out.println("tasks list size: " + tasks.size());
    }

    public void updateFolderInformation(FolderInformation fi) throws ModelException {
        try {
            bllManager.updateFolderInformation(fi);
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

    /**
     * CONTROL FUNCTIONS--------------------------------------------------------
     */
    public void startConversion() throws ModelException {
        bllManager.convertData();
    }

    public void pauseConverion(ConversionTask selectedItem) {
        bllManager.pauseConversion(selectedItem);
    }

    public void stopConversion(ConversionTask selectedItem) {
        bllManager.stopConversion(selectedItem);
        tasks.remove(selectedItem);
    }

    /**
     * Starts the monitoring of the folder selected for automatic conversion
     *
     * @throws ModelException
     */
    public void startWatch() throws ModelException {
        try {
            this.bllManager.startFolderWatch();
        }
        catch (BLLException ex) {
            throw new ModelException(ex);
        }
    }

}