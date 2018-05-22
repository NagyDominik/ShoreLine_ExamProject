/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.DAL.DataBase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import shoreline_examproject.BE.Config;
import shoreline_examproject.Utility.EventLogger;

/**
 *
 * @author Dominik
 */
public class DBConfigManager {

    private ConnectionPool conpool = new ConnectionPool();

    public void saveConfig(Config config) {
        try (Connection con = conpool.checkOut()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO Config(configBinary) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(config);
            oos.flush();
            oos.close();
            
            byte[] data = baos.toByteArray();
            ps.setObject(1, data);
            
            int affected = ps.executeUpdate();
            if (affected < 1) {
                EventLogger.log(EventLogger.Level.ERROR, "The config could not be saved!");
            }
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                config.setId(rs.getInt(1));
            }
        }
        catch (SQLException | IOException ex) {
            EventLogger.log(EventLogger.Level.ERROR, "An exception has occured: " + ex.getMessage());
        }
    }

    public List<Config> loadConfigs() {
        List<Config> configList = new ArrayList();
        try (Connection con = conpool.checkOut()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Config");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ByteArrayInputStream bais = new ByteArrayInputStream(rs.getBytes("configBinary"));
                ObjectInputStream ois = new ObjectInputStream(bais);
                Config temp = (Config) ois.readObject();
                temp.setId(rs.getInt("id"));
                configList.add(temp);
            }
        }
        catch (SQLException | IOException | ClassNotFoundException ex) {
            EventLogger.log(EventLogger.Level.ERROR, "An exception has occured: " + ex.getMessage());
        }
        return configList;
    }

    public void deleteConfig(Config config) {
        try (Connection con = conpool.checkOut()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Config WHERE id = ?");
            ps.setInt(1, config.getId());
            
            int affected = ps.executeUpdate();
            if (affected < 1) {
                EventLogger.log(EventLogger.Level.ERROR, "The config could not be deleted!");
            }
        }
        catch (SQLException ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
        }

    }

}
