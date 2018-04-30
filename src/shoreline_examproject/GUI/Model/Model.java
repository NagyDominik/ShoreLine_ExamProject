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
    
    public AttributesCollection loadFileData(String path)
    {
        return bllManager.loadFileData(path);
    }
}
