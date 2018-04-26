/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.GUI.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shoreline_examproject.Utility.EventLog;

/**
 *
 * @author Bence
 */
public class Model {

    private static Model instance;
    private ObservableList<EventLog> logList = FXCollections.observableArrayList();

    private Model() {
        
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
}
