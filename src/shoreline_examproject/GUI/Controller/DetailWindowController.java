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
    private Label configLbl;
    @FXML
    private Label importLbl;
    @FXML
    private Label exportLbl;
    @FXML
    private Label startTimeLbl;
    
    @FXML
    private ProgressBar progressBar;

    private Model model;
    private ConversionTask currentTask;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = Model.getInstance();
        setUpDetails(currentTask);

    }

    @FXML
    private void backClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void setUpDetails(ConversionTask task) {
        currentTask = task;
        configLbl.setText(model.getSelectedTaskDetails(currentTask).getConfigName());
        importLbl.setText(model.getSelectedTaskDetails(currentTask).getInputData().toString());
        progressBar.setProgress(model.getSelectedTaskDetails(currentTask).getProgress());
        
    }

}
