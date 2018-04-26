/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.GUI.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import shoreline_examproject.BE.LogEntry;

/**
 * FXML Controller class
 *
 * @author Dominik
 */
public class LogWindowController implements Initializable {

    @FXML
    private TableView<LogEntry> logTV;
    @FXML
    private TableColumn<LogEntry, String> dateCol;
    @FXML
    private TableColumn<LogEntry, String> typeCol;
    @FXML
    private TableColumn<LogEntry, String> descCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
