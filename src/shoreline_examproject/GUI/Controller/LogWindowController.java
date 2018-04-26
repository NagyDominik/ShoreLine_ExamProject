/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.GUI.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import shoreline_examproject.GUI.Model.Model;
import shoreline_examproject.Utility.EventLog;

/**
 * FXML Controller class
 *
 * @author Dominik
 */
public class LogWindowController implements Initializable {

    @FXML
    private TableView<EventLog> logTV;
    @FXML
    private TableColumn<EventLog, String> dateCol;
    @FXML
    private TableColumn<EventLog, String> typeCol;
    @FXML
    private TableColumn<EventLog, String> descCol;
    
    private Model model = Model.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTV();
    }
    
    private void setupTV() {
        logTV.setItems(model.getLogList());
        dateCol.setCellValueFactory(new PropertyValueFactory("date"));
        typeCol.setCellValueFactory(new PropertyValueFactory("type"));
        descCol.setCellValueFactory(new PropertyValueFactory("description"));
    }
    
}
