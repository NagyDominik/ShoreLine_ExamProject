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
            PreparedStatement ps = con.prepareStatement("INSERT INTO Config(configBinary) VALUES(?)");
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
        }
        catch (SQLException | IOException ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
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
                configList.add(temp);
            }
        }
        catch (SQLException | IOException | ClassNotFoundException ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
        }
        return configList;
    }

}
