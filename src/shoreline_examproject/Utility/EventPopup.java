package shoreline_examproject.Utility;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Creates popup notifications
 *
 * @author Dominik
 */
public class EventPopup {

    /**
     * Show an alert popup, using the message of the supplied exception as the
     * message of the popup.
     *
     * @param ex The supplied exception.
     */
    public static void showAlertPopup(Exception ex) {
        showAlertPopup(ex.getMessage());
    }

    /**
     * Show an alert popup, using the supplied string as the message.
     *
     * @param message The supplied message.
     */
    public static void showAlertPopup(String message) {
        Alert a = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        a.show();
    }

    /**
     * Show an information popup, using the message of the supplied exception as
     * the message of the popup.
     *
     * @param ex The supplied exception.
     */
    public static void showInformationPopup(Exception ex) {
        showAlertPopup(ex.getMessage());
    }

    /**
     * Show an information popup, using the message of the supplied exception as
     * the message of the popup.
     *
     * @param message The supplied message.
     */
    public static void showInformationPopup(String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        a.show();
    }
}
