package shoreline_examproject.Utility;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
    private final BooleanProperty isChanged = new SimpleBooleanProperty();
    
    public EventLogger() {
    }
    
    public static void log(Level level, String desc) {
        logList.add(new EventLog(EventLog.Type.valueOf(level.name()), desc));
        //TODO DAL TO SAVE TO DATABASE
    }

    public static ObservableList<EventLog> getLogList() {
        return logList;
    }
    
    public boolean isIsChanged() {
        return isChanged.get();
    }

    public void setIsChanged(boolean value) {
        isChanged.set(value);
    }

    public BooleanProperty isChangedProperty() {
        return isChanged;
    }
    
    public static ObservableList getLog() {
        return logList;
    }

}
