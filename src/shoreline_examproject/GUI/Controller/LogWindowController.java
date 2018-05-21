package shoreline_examproject.GUI.Controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;
import shoreline_examproject.GUI.Model.Model;
import shoreline_examproject.BE.EventLog;
import shoreline_examproject.BE.EventLog.Type;
import shoreline_examproject.GUI.Model.ModelException;
import shoreline_examproject.Utility.EventLogger;
import shoreline_examproject.Utility.EventPopup;

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
    private TableColumn<EventLog, String> userCol;
    @FXML
    private TableColumn<EventLog, Type> typeCol;
    @FXML
    private TableColumn<EventLog, String> descCol;
    @FXML
    private JFXButton closeButton;

    private Model model;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            model = Model.getInstance();
            setupTV();
            logTV.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY); 
        } catch (ModelException ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
            EventPopup.showAlertPopup(ex);
        }
    }

    private void setupTV() {
        
        logTV.setItems(EventLogger.getLog());
        dateCol.setCellValueFactory(getCustomDateCellFactory());
        userCol.setCellValueFactory(new PropertyValueFactory("user"));
        typeCol.setCellValueFactory(new PropertyValueFactory("type"));
        typeCol.setCellFactory(getCustomRowFactory());
        descCol.setCellValueFactory(new PropertyValueFactory("description"));
    }

    @FXML
    private void backClicked(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    private Callback<TableColumn.CellDataFeatures<EventLog, String>, ObservableValue<String>> getCustomDateCellFactory() {
           return (TableColumn.CellDataFeatures<EventLog, String> param) -> {
               DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
               SimpleStringProperty formated = new SimpleStringProperty(param.getValue().getLocalDateTime().format(formater));
               return formated;
           };
    }

    private Callback<TableColumn<EventLog, Type>, TableCell<EventLog, Type>> getCustomRowFactory() {
        return (TableColumn<EventLog, Type> param) -> {
            return new TableCell<EventLog, Type>() {
                @Override
                protected void updateItem(Type item, boolean empty) {
                    super.updateItem(item, empty);
                    TableRow<EventLog> row = getTableRow();
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item.toString());
                        switch (item) {
                            case ERROR:
                                row.setStyle("-fx-background-color: #FF7F7F");
                                setFont(Font.font(getFont().getFamily(), FontWeight.BOLD, getFont().getSize()));
                                break;
                            case ALERT:
                                row.setStyle("-fx-background-color: yellow");
                                setFont(Font.font(getFont().getFamily(), FontWeight.BOLD, getFont().getSize()));
                                break;
                            case INFORMATION:
                                row.setStyle("-fx-background-color: lightblue");
                                setFont(Font.font(getFont().getFamily(), FontWeight.BOLD, getFont().getSize()));
                                break;
                            case SUCCESS:
                                row.setStyle("-fx-background-color: lightgreen");
                                setFont(Font.font(getFont().getFamily(), FontWeight.BOLD, getFont().getSize()));
                                break;
                        }
                    }
                }
            };
        };
    }

}
