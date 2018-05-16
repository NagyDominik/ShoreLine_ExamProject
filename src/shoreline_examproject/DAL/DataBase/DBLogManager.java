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
import java.time.ZoneId;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shoreline_examproject.BE.EventLog;
import shoreline_examproject.DAL.DALException;
import shoreline_examproject.Utility.EventLogger;

/**
 *
 * @author Dominik
 */
public class DBLogManager {

    private EventLogger logList;
    private ConnectionPool conpool = new ConnectionPool();

    //listener kell az eventloggernek ischangedpropertyre
    
    
    
    
    public void saveLog(EventLog log) throws DALException {
        try (Connection con = conpool.create())/* Lehet nem a create kell*/ {
            PreparedStatement ps = con.prepareStatement("INSERT INTO Log(Date, userName, Type, Descripton) VALUES(?, ?, ?, ?)");
            
            ps.setDate(1, new java.sql.Date(log.getLocalDateTime().atZone(ZoneId.systemDefault()).toEpochSecond()*1000L));
            ps.setString(2, log.getUser());
            ps.setString(3, log.getType().toString());
            ps.setString(4, log.getDescription());
            int affected = ps.executeUpdate();
            if (affected < 1) {
                throw new DALException("The log could be saved!");
            }
        } catch (SQLException ex) {
            throw new DALException(ex);
        }
    }

    public List<EventLog> loadLog() throws DALException {
        List<EventLog> log = logList.getLogList();
        try (Connection con = conpool.create())/* Lehet nem a create a megfelelő*/ {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Log");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                EventLog tmp = new EventLog();
                //tmp.setDate(new java.sql.Date(rs.getDate("Date"))); have no idea how to retrive the date...
                tmp.setUser(rs.getString("userName"));
                tmp.setType(EventLog.Type.valueOf(rs.getString("Type")));
                tmp.setDescription(rs.getString("Descripton"));                
                log.add(tmp);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBLogManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return log;

    }
    
    /*public Timestamp dateConverter(LocalDateTime ldt){      
        return Timestamp.valueOf(ldt);
    }*/
}
