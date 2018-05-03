package shoreline_examproject.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.BE.EventLog;
import shoreline_examproject.BLL.BLLManager;
import shoreline_examproject.BLL.IBLLManager;
import shoreline_examproject.Utility.EventLogger;

/**
 *
 * @author Bence
 */
public class Model {

    private static Model instance;
    private final IBLLManager bllManager;
    
    private AttributesCollection currentAttributes; //The attributes of the currently loaded file.
    private String currentUser;

    private final ObservableList<EventLog> logList = FXCollections.observableArrayList();
    
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

    public void startConversion(Config currentConfig) throws ModelException {
        if (currentAttributes == null) {
            EventLogger.log(EventLogger.Level.ERROR, "No attributes list provided!");
            throw new ModelException("No attributes! (It is possible that an input file has not been provided)");
        }

        bllManager.convertData(currentAttributes, currentConfig);
        System.out.println(currentAttributes);
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
        Config c1 = new Config("C1");
        Config c2 = new Config("C2");
        
        ConversionTask t1 = new ConversionTask();
        t1.setConfig(c1);
    
        
        ConversionTask t2 = new ConversionTask();
        t2.setConfig(c2);
        
        tasks.add(t1);
        tasks.add(t2);
        
        return tasks;
    }
}
