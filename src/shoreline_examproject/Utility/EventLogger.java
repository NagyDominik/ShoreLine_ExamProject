package shoreline_examproject.Utility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shoreline_examproject.BE.EventLog;

/**
 *
 * @author Dominik
 */
public class EventLogger {

    public enum Level {
        ERROR,
        INFORMATION,
        ALERT,
        SUCCESS;
    }
    
    private static ObservableList<EventLog> logList = FXCollections.observableArrayList();

    public EventLogger() {
        log(Level.ALERT, "Alert type test");
        log(Level.ERROR, "Error type test");
        log(Level.INFORMATION, "Information type test");
        log(Level.SUCCESS, "Success type test");
    }

    public static void log(Level level, String desc) {
        logList.add(new EventLog(EventLog.Type.valueOf(level.name()), desc));
        //TODO DAL TO SAVE TO DATABASE
    }
    
    public static ObservableList getLog() {
        return logList;
    }

}
