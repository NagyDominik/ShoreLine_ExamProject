package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
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
    private TableView<KeyValuePair> tableViewExportAttributes;
    @FXML
    private JFXButton btnRemove;
    @FXML
    private TableColumn<KeyValuePair, String> tblViewOriginalName;
    @FXML
    private TableColumn<KeyValuePair, String> tblViewEditedName;
    
    private Config currentConfig;
    @FXML
    private JFXTextField txtFieldConfName;
      
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model = Model.getInstance();
        setUpViews();
        btnRemove.setDisable(true);
    }

    @FXML
    private void backClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void setUpViews()
    {
        try 
        {
            lstViewImportAttributes.getItems().addAll(model.getCurrentAttributes().getAttributes());    // Fill out the list view with the attributes
            
            tblViewOriginalName.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getKey())); 
            tblViewEditedName.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue()));
            
            lstViewImportAttributes.setOnMouseClicked((MouseEvent event) -> {
                if (lstViewImportAttributes.getSelectionModel().getSelectedItem() != null) {   // Disable the remove button if an imported attribute is selected.
                    btnRemove.setDisable(true);
                }
            } 
            );
            
            tableViewExportAttributes.setOnMouseClicked((MouseEvent event) -> {
                if (tableViewExportAttributes.getSelectionModel().getSelectedItem() != null) {  // Enable the remove button if a value is selected in the export attributes table view.
                    btnRemove.setDisable(false);
                }
            } 
            );
            
            tblViewEditedName.setCellFactory(TextFieldTableCell.forTableColumn()); // Enable the editing of the attribute name.
            
            tblViewEditedName.setOnEditCommit((TableColumn.CellEditEvent<KeyValuePair, String> event) -> { // Save the edit 
                KeyValuePair current = tableViewExportAttributes.getSelectionModel().getSelectedItem();
                
                if (current == null) {
                    throw new NullPointerException("Selection is null!");
                }
                
                current.setValue(event.getNewValue());
            });

        }
        catch (Exception ex) {
            EventPopup.showAlertPopup(ex);
        }
    }   

    @FXML
    private void btnAddClicked(ActionEvent event)
    {
        String selected = lstViewImportAttributes.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            return;
        }
            
        tableViewExportAttributes.getItems().add(new KeyValuePair(selected, selected));
        lstViewImportAttributes.getItems().remove(selected);
    }

    @FXML
    private void btnRemoveClicked(ActionEvent event)
    {
        KeyValuePair selected = tableViewExportAttributes.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            return;
        }
        
        if (!tableViewExportAttributes.getItems().remove(selected))
        {
            EventPopup.showAlertPopup("Could not remove selected item!");
        }
        
        lstViewImportAttributes.getItems().add(0, selected.getKey());
    } 

    @FXML
    private void btnSaveClicked(ActionEvent event)
    {
        String name = txtFieldConfName.getText();
        
        if (name == null || name.isEmpty()) {
            EventPopup.showAlertPopup("Please enter a name for this configuration!");
            return;
        }
        
        currentConfig = new Config(name);
        
        for (KeyValuePair item : tableViewExportAttributes.getItems()) { // Fill out the config with the relations.
            currentConfig.addRelation(item.getKey(), item.getValue());
        }
        
        model.saveConfig(currentConfig);
    }
    
    /**
     * Nested class, used to store key-value pairs in the export attributes table view.
     */
    class KeyValuePair {
        private String key;
        private String value;

        public KeyValuePair(String key, String value)
        {
            this.key = key;
            this.value = value;
        }

        public String getKey()
        {
            return key;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }
    }
}
