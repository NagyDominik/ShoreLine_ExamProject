package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import shoreline_examproject.BE.Config;
import shoreline_examproject.GUI.Model.Model;
import shoreline_examproject.Utility.EventPopup;

/**
 * FXML Controller class
 *
 * @author Bence
 */
public class NewConfigWindowController implements Initializable {

    @FXML
    private JFXButton closeButton;

    private Model model;
    @FXML
    private ListView<String> lstViewImportAttributes;
    @FXML
    private TableView<String> lstViewExportAttributes;
    @FXML
    private JFXButton btnRemove;
    @FXML
    private TableColumn<?, ?> tblViewOriginalName;
    @FXML
    private TableColumn<?, ?> tblViewEditedName;
    
    private Config currentConfig;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = Model.getInstance();
        setUpLists();
        btnRemove.setDisable(true);
    }

    @FXML
    private void backClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void setUpLists()
    {
        try {
            lstViewImportAttributes.getItems().addAll(model.getCurrentAttributes().getAttributes());
            lstViewImportAttributes.setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    btnRemove.setDisable(true);
                }
            });
            lstViewExportAttributes.setOnMouseClicked(new EventHandler<MouseEvent>() //Only show the remove button if a value is selected
            {
                @Override
                public void handle(MouseEvent event)
                {
                    if (lstViewExportAttributes.getSelectionModel().getSelectedItem() != null) {
                        btnRemove.setDisable(false);
                    }
                }
            });
        } catch (Exception ex) {
           EventPopup.showAlertPopup(ex);
        }
    }   

    @FXML
    private void btnAddClicked(ActionEvent event)
    {
        String selected = lstViewImportAttributes.getSelectionModel().getSelectedItem();
        
        if (selected != null && !lstViewExportAttributes.getItems().contains(selected)) {
            lstViewExportAttributes.getItems().add(selected);
            lstViewImportAttributes.getItems().remove(selected);
        }
    }

    @FXML
    private void btnRemoveClicked(ActionEvent event)
    {
        String selected = lstViewExportAttributes.getSelectionModel().getSelectedItem();
        
        if (selected != null && lstViewExportAttributes.getItems().contains(selected)) {
             lstViewExportAttributes.getItems().remove(selected);
             lstViewImportAttributes.getItems().add(0, selected);
         }
    }
}
