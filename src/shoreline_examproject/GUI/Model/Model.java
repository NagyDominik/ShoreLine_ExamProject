package shoreline_examproject.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.BE.EventLog;
import shoreline_examproject.BLL.BLLManager;
import shoreline_examproject.BLL.IBLLManager;

/**
 *
 * @author Bence
 */
public class Model {

    private static Model instance;
    private final IBLLManager bllManager;
    private AttributesCollection currentAttributes; //The attributes of the currently loaded file.
    private String currentUser;    
    private ObservableList<ConversionTask> tasks = FXCollections.observableArrayList();
    
    private Model() {
        bllManager = new BLLManager();
    }

    public static Model getInstance() {
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
     * @param currentConfig The configuration that will be saved.
     */
    public void saveConfig(Config currentConfig)
    {
        bllManager.saveConfig(currentConfig);
    }
    
    public String getCurrentUser(){
        return currentUser ;
    }
   
    public String setCurrentUser(String user){
        return this.currentUser = user;
    }

    public ObservableList<ConversionTask> getTasks()
    {
        return tasks;
    }

    public void createNewConversionTask(Config value) throws ModelException
    {
        if (currentAttributes == null) {
            throw new ModelException("currentAttribute null! It is possible that no file was selected");
        }
        tasks.add(bllManager.createConversionTask(value, currentAttributes));
        System.out.println("tasks list size: " + tasks.size());
    }
}
