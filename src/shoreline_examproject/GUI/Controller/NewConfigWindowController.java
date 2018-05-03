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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
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
    private ListView<String> lstViewImportAttributes;
    @FXML
    private JFXTextField txtFieldConfName;
    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton btnRemove;
    @FXML
    private TreeTableView<KeyValuePair> treeTblViewExport;
    @FXML
    private TreeTableColumn<KeyValuePair, String> treeTblViewOriginal;
    @FXML
    private TreeTableColumn<KeyValuePair, String> treeTblViewEdited;
    @FXML
    private JFXButton btnAddTreeRout;
    
    private Config currentConfig;
    private Model model;
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

    private void setUpViews() {
        try {

            lstViewImportAttributes.getItems().addAll(model.getCurrentAttributes().getAttributesAsString());    // Fill out the list view with the attributes
            System.out.println("Number of attributes: " + lstViewImportAttributes.getItems().size());
            treeTblViewOriginal.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getKey()));
            treeTblViewEdited.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getValue().getValue()));

            lstViewImportAttributes.getItems().addAll(model.getCurrentAttributes().getAttributesAsString()); // Fill out the list view with the attributes

            lstViewImportAttributes.setOnMouseClicked((MouseEvent event) -> {
                if (lstViewImportAttributes.getSelectionModel().getSelectedItem() != null) {   // Disable the remove button if an imported attribute is selected.
                    btnRemove.setDisable(true);
                }
            }
            );

            treeTblViewExport.setOnMouseClicked((MouseEvent event) -> {
                if (treeTblViewExport.getSelectionModel().getSelectedItem() != null) {  // Enable the remove button if a value is selected in the export attributes table view.
                    btnRemove.setDisable(false);
                }
            }
            );

            //treeTblViewEdited.setCellFactory(TextFieldTableCell.forTableColumn()); // Enable the editing of the attribute name.
//            treeTblViewEdited.setOnEditCommit((TableColumn.CellEditEvent<KeyValuePair, String> event) -> { // Save the edit 
//                KeyValuePair current = treeTblViewExport.getSelectionModel().getSelectedItem();
//                if (current == null) {
//                    throw new NullPointerException("Selection is null!");
//                }
//                current.setValue(event.getNewValue());
//            });
            
            treeTblViewEdited.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<KeyValuePair, String>>() {
                public void handle(TreeTableColumn.CellEditEvent<KeyValuePair, String> event) {
                    KeyValuePair current = treeTblViewExport.getSelectionModel().getSelectedItem().valueProperty().getValue();
                    if (current == null) {
                    throw new NullPointerException("Selection is null!");
                    }
                    current.setValue(event.getNewValue());
                }
            });
        }
        catch (Exception ex) {
            EventPopup.showAlertPopup(ex);
        }
    }

    @FXML
    private void btnAddClicked(ActionEvent event) {
        String selected = lstViewImportAttributes.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        //treeTblViewExport.getItems().add(new KeyValuePair(selected, selected));
        
        lstViewImportAttributes.getItems().remove(selected);
    }

    @FXML
    private void btnRemoveClicked(ActionEvent event) {
        TreeItem<KeyValuePair> selected = treeTblViewExport.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        /*if (!treeTblViewExport.getItems().remove(selected)) {
            EventPopup.showAlertPopup("Could not remove selected item!");
        }
        lstViewImportAttributes.getItems().add(0, selected.getItems());*/
    }

    @FXML
    private void btnSaveClicked(ActionEvent event) {
        String name = txtFieldConfName.getText();
        if (name == null || name.isEmpty()) {
            EventPopup.showAlertPopup("Please enter a name for this configuration!");
            return;
        }
       /* currentConfig = new Config(name);
        for (KeyValuePair item : treeTblViewExport.getItems()) { // Fill out the config with the relations.
            currentConfig.addRelation(item.getKey(), item.getValue());
        }
        model.saveConfig(currentConfig);*/
    }

    @FXML
    private void addTreeRout(ActionEvent event) {
    }

    /**
     * Nested class, used to store key-value pairs in the export attributes
     * table view.
     */
    class KeyValuePair {

        private  String key;
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
