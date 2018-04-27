/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
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
    @FXML
    private JFXButton closeButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model.addLog(new EventLog(EventLog.Type.ERROR, "description"));
        setupTV();
    }

    private void setupTV() {
        logTV.setItems(model.getLogList());
        dateCol.setCellValueFactory(new PropertyValueFactory("date"));
        typeCol.setCellValueFactory(new PropertyValueFactory("type"));
        descCol.setCellValueFactory(new PropertyValueFactory("description"));
        /*logTV.setRowFactory(new Callback<TableView<EventLog>, TableRow<EventLog>>() {
            @Override
            public TableRow<EventLog> call(TableView<EventLog> tableview) {
                return new TableRow<EventLog>()
            }
        });*/
    }

    @FXML
    private void backClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
