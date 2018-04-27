/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.BE;

import java.time.LocalDate;

/**
 *
 * @author Dominik
 */
public class EventLog {

    public enum Type {
        ERROR,
        INFORMATION,
        ALERT,
        SUCCESS
    }

    private LocalDate date;
    private Type type;
    private String description;

    public EventLog(Type type, String description) {
        this.date = LocalDate.now();
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
