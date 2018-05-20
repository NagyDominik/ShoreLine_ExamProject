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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import shoreline_examproject.BE.EventLog;
import shoreline_examproject.Utility.EventLogger;

/**
 *
 * @author Dominik
 */
public class DBConfigManager {
    
    private ConnectionPool conpool = new ConnectionPool();

    public void saveLog(EventLog log) {
        try (Connection con = conpool.checkOut()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO Config(configName, siteName, assetSerialNumber, type, externalWorkOrderId, "
                    + "systemStatus, userStatus, name, priority, status, latestFinishDate, earliestFinishDate, latestStartDate, estimatedTime) VALUES(?, ?, ?, ?)");

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
        try (Connection con = conpool.checkOut()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Config");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                EventLog tmp = new EventLog();
                tmp.setDate(Instant.ofEpochMilli(rs.getTimestamp("Date").getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
                tmp.setUser(rs.getString("userName"));
                tmp.setType(rs.getString("Type").trim().toUpperCase());
                tmp.setDescription(rs.getString("Description"));
            }
        }
        catch (SQLException ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
        }
        EventLogger.setIsSetUp(Boolean.TRUE);
        return null;
    }

}
