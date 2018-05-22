package shoreline_examproject.Utility;

import java.time.LocalDateTime;

/**
 *
 * @author Dominik
 */
public class EventLog {

    public enum Type {
        ERROR,
        INFORMATION,
        ALERT,
        SUCCESS,
        NOTIFICATION;
    }

    private LocalDateTime date;
    private Type type;
    private String description;
    private String user;

    public EventLog(Type type, String description) {
        this.date = LocalDateTime.now();
        this.user = System.getProperty("user.name");
        this.type = type;
        this.description = description;
    }

    public EventLog() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    
    public void setType(String type) {
        this.type = EventLog.Type.valueOf(type);
    }

    public LocalDateTime getLocalDateTime() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
