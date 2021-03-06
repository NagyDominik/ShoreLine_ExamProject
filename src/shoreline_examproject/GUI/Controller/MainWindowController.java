package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import shoreline_examproject.BE.Config;
import shoreline_examproject.BE.ConversionTask;
import shoreline_examproject.Utility.EventLog;
import shoreline_examproject.GUI.Model.Model;
import shoreline_examproject.GUI.Model.ModelException;
import shoreline_examproject.Utility.EventLogger;
import shoreline_examproject.Utility.EventPopup;

/**
 * FXML Controller class
 *
 * @author Bence
 */
public class MainWindowController implements Initializable {

    @FXML
    private Label taskNameLbl;
    @FXML
    private Label progressLbl;
    @FXML
    private JFXComboBox<Config> configComboBox;
    @FXML
    private Label filePathLbl;
    @FXML
    private TableView<ConversionTask> taskTV;
    @FXML
    private Label userNameLbl;
    @FXML
    private JFXTextArea txtAreaPreview;
    @FXML
    private TableColumn<ConversionTask, String> taskCol;
    @FXML
    private TableColumn<ConversionTask, Double> progressCol;
    @FXML
    private Label startTimeLbl;
    @FXML
    private JFXButton btnDeletConfig;

    private Model model;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            userNameLbl.setAlignment(Pos.CENTER_RIGHT);
            model = Model.getInstance();
            taskTV.setItems(model.getTasks());
            configComboBox.setItems(model.getConfList());
            userNameLbl.textProperty().bind(EventLogger.getUsernameProperty());
            model.setCurrentUser(userNameLbl.getText());
            btnDeletConfig.setDisable(true);
            setUpConfigComboBox();
            setUpTaskTableView();
            setUpHandlersAndListeners();
        }
        catch (ModelException ex) {
            EventLogger.log(EventLogger.Level.ERROR, "An exception has occured: " + ex.getMessage());
        }
    }

    @FXML
    private void importFileClicked(ActionEvent event) {
        try {
            FileChooser fc = new FileChooser();
            File f = fc.showOpenDialog(this.userNameLbl.getScene().getWindow());
            if (f == null) {
                return;
            }
            loadFile(f.getPath());
        }
        catch (Exception ex) {
            EventPopup.showAlertPopup(ex);
        }
    }

    @FXML
    private void newConfigClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline_examproject/GUI/View/NewConfigWindow.fxml"));
        Parent root = (Parent) loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("New Config");
        stage.getIcons().add(new Image("shoreline_examproject/img/shortlogo.png"));

        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void startClicked(ActionEvent event) {
        try {
            model.startConversion();
        }
        catch (ModelException ex) {
            EventPopup.showAlertPopup(ex);
        }
    }

    @FXML
    private void btnAddTask(ActionEvent event) {
        if (configComboBox.getSelectionModel().getSelectedItem() == null) {
            EventPopup.showAlertPopup("Please select a configuration!");
            return;
        }
        try {
            model.createNewConversionTask(configComboBox.getValue());
        }
        catch (ModelException ex) {
            EventLogger.log(EventLogger.Level.ERROR, "An exception has occured: " + ex.getMessage());
        }
        //System.out.println(taskTV.getItems().size());
    }

    @FXML
    private void deleteTask(ActionEvent event) {
        ConversionTask selectedItem = taskTV.getSelectionModel().getSelectedItem();
        model.stopConversion(selectedItem);
    }

    @FXML
    private void stopClicked(ActionEvent event) {
        model.stopConversion(taskTV.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void pauseClicked(ActionEvent event) {
        model.pauseConverion(taskTV.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void optionsClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline_examproject/GUI/View/OptionsWindow.fxml"));
            Parent root = (Parent) loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Options");
            stage.getIcons().add(new Image("shoreline_examproject/img/shortlogo.png"));
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException ex) {
            EventLogger.log(EventLogger.Level.NOTIFICATION, "Failed to open window OptionsWindow! \n" + ex.getMessage());
        }
    }

    @FXML
    private void moreDetailClicked(ActionEvent event) {
        try {
            model.setSelectedTask(taskTV.getSelectionModel().getSelectedItem());
            if (model.getSelectedTask() == null) {
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline_examproject/GUI/View/DetailWindow.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Details");
            stage.getIcons().add(new Image("shoreline_examproject/img/shortlogo.png"));
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException ex) {
            EventLogger.log(EventLogger.Level.NOTIFICATION, "Failed to open window DetailWindow! \n" + ex.getMessage());
        }
    }

    @FXML
    private void logClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline_examproject/GUI/View/LogWindow.fxml"));
            Parent root = (Parent) loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Log");
            stage.getIcons().add(new Image("shoreline_examproject/img/shortlogo.png"));
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException ex) {
            EventLogger.log(EventLogger.Level.NOTIFICATION, "Failed to open window LogWindow! \n" + ex.getMessage());
        }
    }

    @FXML
    private void btnAssignFolderClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/shoreline_examproject/GUI/View/AssignFolderWindow.fxml"));
            Parent root = (Parent) loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Folders");
            stage.getIcons().add(new Image("shoreline_examproject/img/shortlogo.png"));
            stage.setResizable(false);
            stage.show();
        }
        catch (IOException ex) {
            EventLogger.log(EventLogger.Level.NOTIFICATION, "Failed to open window AssignFolderWindow! \n" + ex.getMessage());
        }
    }

    @FXML
    private void exportFile(ActionEvent event) throws ModelException {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose export location");
            //Set extension
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON file (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);
            //Show save file dialog
            File file = fileChooser.showSaveDialog(this.userNameLbl.getScene().getWindow());
            if (file == null) {
                return;
            }
            System.out.println(file.getAbsolutePath());
            model.getCurrentAttributes().setExportPath(file.getAbsolutePath());
        }
        catch (Exception e) {
            EventPopup.showAlertPopup(e);
        }
    }

    @FXML
    private void btnDeleteConfigPressed(ActionEvent event) throws IOException {
        Config selected = configComboBox.getSelectionModel().getSelectedItem();

        if (selected == null) {
            return;
        }

        model.removeConfig(selected);
    }

    private void setUpTaskTableView() {

        taskCol.setCellValueFactory((TableColumn.CellDataFeatures<ConversionTask, String> param) -> {
            ConversionTask ct = param.getValue();
            return new ReadOnlyStringWrapper(ct.getConfigName());
        });

        taskCol.setCellFactory((TableColumn<ConversionTask, String> param) -> new TableCell<ConversionTask, String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (!isEmpty()) {
                    ConversionTask task = getTableView().getItems().get(getIndex());
                    setText(task.getConfigName());
                    task.cancelledPropertyProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                        if (newValue) {
                            setStyle("-fx-background-color: red");
                        }
                    });
                }
            }
        });

        progressCol.setCellValueFactory((TableColumn.CellDataFeatures<ConversionTask, Double> param) -> {
            ConversionTask ct = param.getValue();

            return ct.progressProperty().asObject();

        });

        progressCol.setCellFactory(ProgressBarTableCell.<ConversionTask>forTableColumn());
    }

    /**
     * Set up the combo box to correctly display the names of the contained
     * configurations.
     */
    private void setUpConfigComboBox() {
        configComboBox.setConverter(new StringConverter<Config>() {
            @Override
            public String toString(Config object) {
                return object.getName();
            }

            @Override
            public Config fromString(String string) {
                return configComboBox.getItems().stream().filter(c -> c.getName().equals(string)).findFirst().orElse(null); // Curtesy of StackOverflow
            }
        });

        configComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Config selected = configComboBox.getSelectionModel().getSelectedItem();

                if (selected == null) {
                    btnDeletConfig.setDisable(true);
                    return;
                }

                txtAreaPreview.setText(selected.getAssociationMap());
                btnDeletConfig.setDisable(false);
            }
        });
    }

    private void loadFile(String path) {
        try {
            Runnable r1 = () -> {
                Scene s = userNameLbl.getScene();
                s.setCursor(Cursor.WAIT);
                model.loadFileData(path);
                s.setCursor(Cursor.DEFAULT);
            };

            Thread t1 = new Thread(r1);
            t1.start();
            filePathLbl.setText(path);
        }
        catch (Exception ex) {
            EventLogger.log(EventLogger.Level.NOTIFICATION, String.format("An error occured while attempting to load the given file: %s \nException message: %s", path, ex.getMessage()));
        }
    }

    private void setUpHandlersAndListeners() {
        taskTV.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ConversionTask> o, ConversionTask oldV, ConversionTask newV) -> {
            if (taskTV.getSelectionModel().getSelectedItem() != null) {
                taskNameLbl.setText(newV.getConfigName());
                progressLbl.textProperty().bind(newV.progressProperty().multiply(100).asString("%.0f %%"));
                startTimeLbl.setText(newV.getStartTimeAsString());
            } else {
                taskNameLbl.setText("");
                progressLbl.textProperty().unbind();
                progressLbl.setText("");
                startTimeLbl.setText("");
            }
        });

        EventLogger.getLog().addListener(new ListChangeListener<EventLog>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends EventLog> c) {
                if (EventLogger.isSetUp()) {
                    while (c.next()) {
                        if (c.wasAdded()) {
                            List<EventLog> changes = new ArrayList<>();
                            changes.addAll(c.getAddedSubList());
                            for (EventLog change : changes) {
                                if (change.getType() == EventLog.Type.ERROR) {
                                    Platform.runLater(() -> {
                                        EventPopup.showAlertPopup(change.getDescription());
                                    });
                                }
                                if (change.getType() == EventLog.Type.NOTIFICATION) {
                                    Platform.runLater(() -> {
                                        EventPopup.showAlertPopup(change.getDescription());
                                    });
                                }
                            }
                        }
                    }
                }
            }
        });
    }

}
