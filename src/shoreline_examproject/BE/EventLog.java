package shoreline_examproject.BE;

import java.time.LocalDate;
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
        SUCCESS;
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

    public LocalDateTime getDate() {
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
