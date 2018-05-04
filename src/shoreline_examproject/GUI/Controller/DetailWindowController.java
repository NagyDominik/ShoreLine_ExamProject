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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.GUI.Model.Model;

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
        model = Model.getInstance();
        setUpDetails(model.getSelectedTask());
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
