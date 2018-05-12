package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
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
import shoreline_examproject.GUI.Model.ModelException;
import shoreline_examproject.Utility.EventLogger;
import shoreline_examproject.Utility.EventPopup;

/**
 * FXML Controller class
 *
 * @author Bence
 */
public class NewConfigWindowController implements Initializable {

    @FXML
    private ListView<String> lstViewImportAttributes;
    @FXML
    private JFXTextField txtFieldConfName;
    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton btnRemove;
    @FXML
    private TableView<KeyValuePair> exportTblView;
    @FXML
    private TableColumn<KeyValuePair, String> originalExportTblCol;
    @FXML
    private TableColumn<KeyValuePair, String> editedExportTblCol;
    @FXML
    private JFXTextField txtFieldImportSearch;
    @FXML
    private JFXTextField txtFieldExportSearch;
    
        
    private Config currentConfig;
    private Model model;
    private FilteredList<String> filteredAttributeList;
    private FilteredList<KeyValuePair> filteredKeyValuePairList;
    private ObservableList<String> attributeList;
    private ObservableList<KeyValuePair> keyValuePairList;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            model = Model.getInstance();
            attributeList = FXCollections.observableArrayList(model.getCurrentAttributes().getAttributesAsString());

            if (model.isConfigEdit()) {
                currentConfig = model.getSelected();
                //TODO: make it possible to edit a config.
                keyValuePairList = FXCollections.observableArrayList();

                filteredAttributeList = new FilteredList<String>(attributeList);
                filteredKeyValuePairList = new FilteredList<KeyValuePair>(keyValuePairList);
            }
            else {
                currentConfig = new Config();

                keyValuePairList = FXCollections.observableArrayList();

                filteredAttributeList = new FilteredList<String>(attributeList);
                filteredKeyValuePairList = new FilteredList<KeyValuePair>(keyValuePairList);
            }

            
            setUpViews();
            setUpSearch();
            btnRemove.setDisable(true);
        } catch (ModelException ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
        }
    }

    @FXML
    private void backClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void setUpViews() {
        try {
            lstViewImportAttributes.setItems(filteredAttributeList);
            
            exportTblView.setItems(filteredKeyValuePairList);
            
            originalExportTblCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getKey()));
            editedExportTblCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue()));
            
            lstViewImportAttributes.setOnMouseClicked((MouseEvent event) -> {
                if (lstViewImportAttributes.getSelectionModel().getSelectedItem() != null) {   // Disable the remove button if an imported attribute is selected.
                    btnRemove.setDisable(true);
                }
            }
            );

            exportTblView.setOnMouseClicked((MouseEvent event) -> {
                if (exportTblView.getSelectionModel().getSelectedItem() != null) {  // Enable the remove button if a value is selected in the export attributes table view.
                    btnRemove.setDisable(false);
                }
            }
            );
            
            editedExportTblCol.setCellFactory(TextFieldTableCell.forTableColumn());
            
            editedExportTblCol.setOnEditCommit((TableColumn.CellEditEvent<KeyValuePair, String> event) -> { try {
                // Save edit
                KeyValuePair kvp = event.getRowValue();
                kvp.setValue(event.getNewValue());
                currentConfig.updateValue(kvp.getKey(), event.getNewValue());
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(NewConfigWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
        catch (Exception ex) {
            EventLogger.log(EventLogger.Level.ERROR, "An exception has occured! Exception texts: \n" + ex.getMessage());
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
        
        KeyValuePair kvp = new KeyValuePair(selected, selected);
//        exportTblView.getItems().add(kvp);
//        lstViewImportAttributes.getItems().remove(kvp.getKey());   

        keyValuePairList.add(kvp);
        attributeList.remove(selected);
        
        currentConfig.addRelation(selected, selected);
    }

    @FXML
    private void btnRemoveClicked(ActionEvent event)
    {
        KeyValuePair selected = exportTblView.getSelectionModel().getSelectedItem();
        
        if (selected == null) {
            return;
        }
        
//        exportTblView.getItems().remove(selected);
//        lstViewImportAttributes.getItems().add(0, selected.key);
    
        attributeList.add(0, selected.getKey());
        keyValuePairList.remove(selected);
    }

    @FXML
    private void btnSaveClicked(ActionEvent event)
    {
        String name = txtFieldConfName.getText();
        if (name == null || name.isEmpty()) {
            EventPopup.showInformationPopup("Please enter a name!");
            return;
        }
        currentConfig.setName(name);
        model.addConfig(currentConfig);
        Stage s = (Stage) btnRemove.getScene().getWindow();
        s.close();
    }

    @FXML
    private void btnToRootClicked(ActionEvent event)
    {
        try {
            KeyValuePair kvp = exportTblView.getSelectionModel().getSelectedItem();
            if (kvp == null) {
                return;
            }
            
            currentConfig.designateAsRoot(kvp.getKey());
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NewConfigWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Set up events so the table and list view can be filtered interactively.
     */
    private void setUpSearch()
    {
        txtFieldImportSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            {filteredAttributeList.setPredicate((attribute) -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                return attribute.toLowerCase().contains(newValue.toLowerCase());
            });
        }}); 
        
        txtFieldExportSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            {filteredKeyValuePairList.setPredicate((kvp) -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                if (kvp.getKey().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                
                if (kvp.getKey().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }
                
                return false;
                
            });
        }});
    }

    /**
     * Nested class, used to store key-value pairs in the export attributes, to make displaying them easier.
     * table view.
     */
    class KeyValuePair {

        private String key;
        private String value;

        public KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
