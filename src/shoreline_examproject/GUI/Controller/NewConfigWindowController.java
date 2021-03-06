package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
    private JFXTextField txtFieldImportSearch;
    @FXML
    private JFXTextField txtFieldExportSearch;
    @FXML
    private TableColumn<KeyValuePair, String> exportTblCol;

    private Config currentConfig;
    private Model model;

    private FilteredList<String> filteredAttributeList;
    private FilteredList<KeyValuePair> filteredKeyValuePairList;
    private ObservableList<String> attributeList;
    private ObservableList<KeyValuePair> keyValuePairList;

    private final String[] normalAttributes = new String[]{"siteName", "assetSerialNumber", "type", "externalWorkOrderId", "systemStatus", "userStatus", "createdOn", "createdBy", "name", "priority", "status"};
    private final String[] planningAttributes = new String[]{"latestFinishDate", "earliestStartDate", "latestStartDate", "estimatedTime"};

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            model = Model.getInstance();
            attributeList = FXCollections.observableArrayList(model.getCurrentAttributes().getAttributesAsString());
            txtFieldConfName.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.ENTER) {
                        btnSaveClicked(null);
                    }
                }
            });

            currentConfig = new Config();

            keyValuePairList = FXCollections.observableArrayList();

            filteredAttributeList = new FilteredList<>(attributeList);
            filteredKeyValuePairList = new FilteredList<>(keyValuePairList);

            setUpViews();
            setUpSearch();
            btnRemove.setDisable(true);

        }
        catch (ModelException ex) {
            EventLogger.log(EventLogger.Level.NOTIFICATION, "An exception has occured: " + ex.getMessage());
        }
    }

    @FXML
    private void backClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnRemoveClicked(ActionEvent event) {
        KeyValuePair selected = exportTblView.getSelectionModel().getSelectedItem();

        if (selected == null && selected.getKey().equals("planning") || !selected.editable || selected.isHasDefault()) {
            return;
        }

        attributeList.add(0, selected.getValue());
        currentConfig.updateOutputName(selected.getKey(), "");
        exportTblView.refresh();
        selected.setValue("");
    }

    @FXML
    private void btnSaveClicked(ActionEvent event) {
        String name = txtFieldConfName.getText();
        if (name == null || name.isEmpty()) {
            EventPopup.showInformationPopup("Please enter a name!");
            return;
        }
        currentConfig.setName(name);
        model.saveConfig(currentConfig);
        Stage s = (Stage) btnRemove.getScene().getWindow();
        s.close();
    }

    private void setUpViews() {
        try {
            setUpDragAndDrop();
            addAttributes();

            lstViewImportAttributes.setItems(filteredAttributeList);

            exportTblView.setRowFactory((TableView<KeyValuePair> param) -> {
                TableRow<KeyValuePair> tr = new TableRow();

                tr.setOnDragEntered((DragEvent event) -> {
                    if (event.getSource() != exportTblView) {
                        if (!tr.getItem().editable) {
                            tr.setStyle("-fx-background-color: red");
                        } else {
                            tr.setStyle("-fx-background-color: green"); // If the source is valid, indicate it to the user, by changing the color of the row   
                        }
                    }

                    event.consume();
                });

                tr.setOnDragExited((DragEvent event) -> {
                    tr.setStyle("");    // Reset the stylo upon exiting from the row

                    event.consume();
                });

                tr.setOnDragOver((DragEvent event) -> {
                    if (event.getGestureSource() != exportTblView && event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.COPY);
                    }

                    event.consume();
                });

                tr.setOnDragDropped((DragEvent event) -> {
                    Dragboard db = event.getDragboard();
                    boolean success = false;

                    if (db.hasString()) {
                        KeyValuePair kvp = tr.getItem();
                        if (kvp.getKey().equals("planning")) {
                            return;
                        }

                        if (!kvp.isEditable()) {
                            return;
                        }

                        if (!kvp.getValue().isEmpty()) {    // If the row already contains a value, return it to the left table.
                            attributeList.add(0, kvp.getValue());
                        }

                        kvp.setValue(db.getString());
                        currentConfig.updateOutputName(kvp.getKey().trim(), kvp.getValue());

                        attributeList.remove(kvp.getValue());

                        exportTblView.refresh();
                        success = true;
                    }

                    event.setDropCompleted(success);

                    event.consume();
                });
                return tr;
            });

            exportTblView.setEditable(true);
            exportTblView.setItems(filteredKeyValuePairList);

            originalExportTblCol.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue().getKey()));
            exportTblCol.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().getValue()));

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
        }
        catch (Exception ex) {
            EventLogger.log(EventLogger.Level.ERROR, "An exception has occured! Exception text: " + ex.getMessage());
            EventPopup.showAlertPopup(ex);
        }
    }

    /**
     * Set up events so the table and list view can be filtered interactively.
     */
    private void setUpSearch() {
        txtFieldImportSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            {
                filteredAttributeList.setPredicate((attribute) -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    return attribute.toLowerCase().contains(newValue.toLowerCase());
                });
            }
        });

        txtFieldExportSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            {
                filteredKeyValuePairList.setPredicate((kvp) -> {
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
            }
        });
    }

    private void addAttributes() {
        for (String normalAttribute : normalAttributes) {
            KeyValuePair kvp = new KeyValuePair(normalAttribute, "");

            if (normalAttribute.equals("siteName")) {
                kvp.setValue("");
                kvp.setEditable(false);
                kvp.setHasDefault(true);
            }

            if (normalAttribute.equals("status")) {
                kvp.setValue("NEW");
                kvp.setEditable(true);
                kvp.setHasDefault(true);
            }

            if (normalAttribute.equals("createdOn")) {
                kvp.setValue(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
                kvp.setEditable(false);
                kvp.setHasDefault(true);
            }

            if (normalAttribute.equals("priority")) {
                kvp.setValue("Low");
                kvp.setEditable(true);
                kvp.setHasDefault(true);
            }

            if (normalAttribute.equals("createdBy")) {
                kvp.setValue("SAP");
                kvp.setEditable(false);
                kvp.setHasDefault(true);
            }

            keyValuePairList.add(kvp);
            currentConfig.addRelation(normalAttribute, kvp.getValue(), false, kvp.hasDefault.get());

        }

        keyValuePairList.add(new KeyValuePair("planning", "-----------------"));

        for (String planningAttribute : planningAttributes) {
            KeyValuePair kvp = new KeyValuePair("\t" + planningAttribute, "");

            if (planningAttribute.equals("estimatedTime")) {
                kvp.setValue(" ");
                kvp.setEditable(true);
                kvp.setHasDefault(true);
            }

            keyValuePairList.add(kvp);
            currentConfig.addRelation(planningAttribute, kvp.getValue(), true, kvp.hasDefault.get());
        }
    }

    private void setUpDragAndDrop() {
        lstViewImportAttributes.setOnDragDetected((MouseEvent event) -> {
            Dragboard db = lstViewImportAttributes.startDragAndDrop(TransferMode.COPY_OR_MOVE);
            ClipboardContent cc = new ClipboardContent();
            cc.putString(lstViewImportAttributes.getSelectionModel().getSelectedItem());
            db.setContent(cc);

            event.consume();
        });

        lstViewImportAttributes.setOnDragDone((DragEvent event) -> {
            if (event.isDropCompleted() && event.getTransferMode() == TransferMode.MOVE) {
                lstViewImportAttributes.getItems().remove(event.getDragboard().getString());
            }

            event.consume();
        });
    }

    /**
     * Nested class, used to store key-value pairs in the export attributes, to
     * make displaying them easier. table view.
     */
    class KeyValuePair {

        private String key;
        private String value;
        private boolean editable;
        private final BooleanProperty hasDefault = new SimpleBooleanProperty(false);

        public KeyValuePair(String key, String value) {
            this.key = key;
            this.value = value;
            this.editable = true;
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

        private void setEditable(boolean b) {
            this.editable = b;
        }

        public boolean isEditable() {
            return editable;
        }

        public boolean isHasDefault() {
            return hasDefault.get();
        }

        public void setHasDefault(boolean value) {
            hasDefault.set(value);
        }

        public BooleanProperty hasDefaultProperty() {
            return hasDefault;
        }
    }

}
