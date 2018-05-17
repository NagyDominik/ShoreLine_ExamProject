/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.GUI.Model.Model;
import shoreline_examproject.GUI.Model.ModelException;
import shoreline_examproject.Utility.EventLogger;
import shoreline_examproject.Utility.EventPopup;

/**
 * FXML Controller class
 *
 * @author Bence
 */
public class DetailWindowController implements Initializable {

    @FXML
    private JFXButton closeButton;
    @FXML
    private Label taskNameLbl;
    @FXML
    private Label importLbl;
    @FXML
    private Label exportLbl;
    @FXML
    private Label startTimeLbl;
    @FXML
    private ProgressBar progressBar;

    private Model model;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            model = Model.getInstance();
            setUpDetails(model.getSelectedTask());
        } catch (ModelException ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
            EventPopup.showAlertPopup(ex);
        }
    }

    @FXML
    private void backClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void setUpDetails(ConversionTask task) {
        taskNameLbl.setText(task.getConfigName());
        progressBar.progressProperty().bind(task.progressProperty());
        importLbl.setText(task.getInputData().getImportPath());
        exportLbl.setText("TO BE IMPLEMENTED!");
        startTimeLbl.setText(task.getStartTimeAsString());
    }

}
