/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shoreline_examproject.BE.AttributesCollection;
import shoreline_examproject.BE.EventLog;
import shoreline_examproject.BLL.BLLManager;
import shoreline_examproject.BLL.IBLLManager;

/**
 *
 * @author Bence
 */
public class Model {

    private static Model instance;
    private ObservableList<EventLog> logList = FXCollections.observableArrayList();
    private AttributesCollection currentAttributes; //The attributes of the currently loaded file.
    
    
    private IBLLManager bllManager;
    
    private Model() {
        bllManager = new BLLManager();
    }

    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        return instance;
    }
    
    public void addLog(EventLog entry) {
        logList.add(entry);
    }
    
    public ObservableList<EventLog> getLogList() {
        return logList;
    }
    
    public void loadFileData(String path)
    {
        currentAttributes = bllManager.loadFileData(path);
    }
    
    public void startConversion() throws ModelException
    {
        if (currentAttributes == null) {
            throw new ModelException("No attributes! (It is possible that an input file has not been provided)");
        }
        
        
        //TODO: add implementation for converting between formats in the BLL.
        System.out.println(currentAttributes); 
    }

    public AttributesCollection getCurrentAttributes() throws ModelException
    {
        if (currentAttributes == null) {
            throw new ModelException("Current attributes is not set!");
        }
        return currentAttributes;
    }
}
