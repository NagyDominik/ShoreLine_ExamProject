package shoreline_examproject.Utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Responsible for managing the event logs in the application
 *
 * @author Dominik
 */
public class EventLogger {

    public enum Level {
        ERROR,
        INFORMATION,
        ALERT,
        SUCCESS,
        NOTIFICATION;
    }

    private static ObservableList<EventLog> logList = FXCollections.observableArrayList();
    private static StringProperty username = new SimpleStringProperty();
    private static Boolean isSetUp = false;
    private static Boolean isObservable = true;

    public EventLogger() {
    }

    /**
     * Creates a new EventLog entry
     *
     * @param level The type of the entry
     * @param desc Description of the entry
     */
    public static void log(Level level, String desc) {
        logList.add(new EventLog(EventLog.Type.valueOf(level.name()), desc));
    }

    /**
     * Provides the same functionality as the log() function, but without saving
     * the created log to the database
     *
     * @param level The type of the entry
     * @param desc Description of the entry
     */
    public static void logIncognito(Level level, String desc) {
        isObservable = false;
        logList.add(new EventLog(EventLog.Type.valueOf(level.name()), desc));
        isObservable = true;
    }

    public static StringProperty getUsernameProperty() {
        return username;
    }

    public static void setUsername(String username) {
        EventLogger.username.set(username);
    }

    public static String getUsername() {
        return username.get();
    }

    public static ObservableList<EventLog> getLog() {
        return logList;
    }

    public static Boolean isSetUp() {
        return isSetUp;
    }

    public static void setIsSetUp(Boolean isSetUp) {
        EventLogger.isSetUp = isSetUp;
    }

    public static Boolean isObservable() {
        return isObservable;
    }

    public static void setIsObservable(Boolean isObservable) {
        EventLogger.isObservable = isObservable;
    }

}
