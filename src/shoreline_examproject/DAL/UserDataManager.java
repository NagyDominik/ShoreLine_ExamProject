/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.DAL;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import shoreline_examproject.Utility.EventLogger;

/**
 *
 * @author Dominik
 */
public class UserDataManager {

    private DataInputStream reader;
    private String path = System.getProperty("user.home") + File.separator + "shorelineconverter" + File.separator + "userdata.bin";
    private File file = new File(path);

    public UserDataManager() {
        setListener();
    }

    /**
     * Writes the provided username to the logged in user's directory
     *
     * @param name username
     */
    public void write(String name) {
        try {
            if (!file.getParentFile().mkdirs()) {
                reader = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
                String username = reader.readUTF();
                System.out.println(username);
                reader.close();
                if (!username.equals(name)) {
                    DataOutputStream outstream = new DataOutputStream(new FileOutputStream(file));
                    outstream.writeUTF(name);
                    outstream.close();
                }
            } else {
                DataOutputStream outstream = new DataOutputStream(new FileOutputStream(file));
                outstream.writeUTF(name);
                outstream.close();
            }
        }
        catch (IOException ex) {
            EventLogger.log(EventLogger.Level.ERROR, ex.getMessage());
        }
    }

    /**
     * Attempts to read back the previously saved username
     *
     * @return username
     */
    public String read() {
        String name = null;
        try {
            if (file.exists()) {
                reader = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
                name = reader.readUTF();
                reader.close();
            } else {
                name = System.getProperty("user.name");
            }
        }
        catch (Exception ex) {
            EventLogger.log(EventLogger.Level.ERROR, "An exception has occured: " + ex.getMessage());
        }
        return name;
    }

    private void setListener() {
        EventLogger.getUsernameProperty().addListener((observable, oldValue, newValue) -> {
            write(newValue);
        });
    }
    
}
