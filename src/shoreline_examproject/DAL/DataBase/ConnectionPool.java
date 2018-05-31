/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.DAL.DataBase;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import shoreline_examproject.Utility.EventLogger;
import shoreline_examproject.Utility.ObjectPool;

/**
 * An ObjectPool containing database connections
 *
 * @author Dominik
 */
public class ConnectionPool extends ObjectPool<Connection> {

    private ConnectionManager cm = ConnectionManager.getInstance();

    public ConnectionPool() {
        super();
    }

    @Override
    protected Connection create() {
        Connection con = null;
        try {
            con = cm.getConnection();
        }
        catch (SQLServerException ex) {
            EventLogger.log(EventLogger.Level.ERROR, "An exception occured while attempting to create a Connection. Message: \n" + ex.getMessage());
        }
        return con;
    }

    @Override
    protected boolean validate(Connection o) {
        return o != null;
    }

    @Override
    public void expire(Connection o) {
        o = null;
    }

}
