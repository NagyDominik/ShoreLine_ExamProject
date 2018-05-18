package shoreline_examproject.Utility;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jdk.nashorn.internal.codegen.CompilerConstants;
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
    private static StringProperty username = new SimpleStringProperty();
    private static Boolean isSetUp = false;
    
    public EventLogger() {
    }
    
    public static void log(Level level, String desc) {
        logList.add(new EventLog(EventLog.Type.valueOf(level.name()), desc));
    }

    public static ObservableList<EventLog> getLogList() {
        return logList;
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
    
    public static ObservableList getLog() {
        return logList;
    }

    public static Boolean isSetUp() {
        return isSetUp;
    }

    public static void setIsSetUp(Boolean isSetUp) {
        EventLogger.isSetUp = isSetUp;
    }
    
    public static void loadLog(List<EventLog> list) {
        //logList.addAll(list);
    }
}
