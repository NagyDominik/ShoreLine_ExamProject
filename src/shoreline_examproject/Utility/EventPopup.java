package shoreline_examproject.Utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Dominik
 */
public class EventPopup {

    public static void showAlertPopup(Exception ex) {
        Alert a = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
        a.show();
    }
    
    public static void showInformationPopup(Exception ex){
        Alert a = new Alert(Alert.AlertType.INFORMATION, ex.getMessage(), ButtonType.OK);
        a.show();
    }

}
