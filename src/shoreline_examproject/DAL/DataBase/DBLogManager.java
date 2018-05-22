/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.DAL.DataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import javafx.collections.ListChangeListener;
import shoreline_examproject.BE.EventLog;
import shoreline_examproject.Utility.EventLogger;

/**
 *
 * @author Dominik
 */
public class DBLogManager {

    private ConnectionPool conpool = new ConnectionPool();

    public DBLogManager() {
        setUpListener();
    }

    public void saveLog(EventLog log) {
        try (Connection con = conpool.checkOut()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO Log(Date, userName, Type, Description) VALUES(?, ?, ?, ?)");

            ps.setTimestamp(1, new Timestamp(log.getLocalDateTime().toInstant(ZoneOffset.UTC).toEpochMilli()));
            ps.setString(2, log.getUser());
            ps.setString(3, log.getType().toString());
            ps.setString(4, log.getDescription());
            int affected = ps.executeUpdate();
            if (affected < 1) {
                EventLogger.log(EventLogger.Level.ERROR, "The log could not be saved!");
            }
        }
        catch (SQLException ex) {
            EventLogger.logIncognito(EventLogger.Level.ERROR, ex.getMessage());
        }
    }

    public List<EventLog> loadLog() {
        List<EventLog> log = EventLogger.getLog();
        try (Connection con = conpool.checkOut()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Log");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EventLog tmp = new EventLog();
                tmp.setDate(Instant.ofEpochMilli(rs.getTimestamp("Date").getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
                tmp.setUser(rs.getString("userName"));
                tmp.setType(rs.getString("Type").trim().toUpperCase());
                tmp.setDescription(rs.getString("Description"));
                log.add(tmp);
            }
        }
        catch (SQLException ex) {
            EventLogger.log(EventLogger.Level.ERROR, "An exception has occured: " + ex.getMessage());
        }
        EventLogger.setIsSetUp(Boolean.TRUE);
        return log;
    }

    private void setUpListener() {
        EventLogger.getLog().addListener(new ListChangeListener<EventLog>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends EventLog> c) {
                if (EventLogger.isSetUp() && EventLogger.isObservable()) {
                    while (c.next()) {
                        if (c.wasAdded()) {
                            List<EventLog> changes = new ArrayList<>();
                            changes.addAll(c.getAddedSubList());
                            for (EventLog change : changes) {
                                if (change.getType()!= EventLog.Type.NOTIFICATION) {
                                    saveLog(change);
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
