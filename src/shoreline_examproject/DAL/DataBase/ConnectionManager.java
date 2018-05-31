package shoreline_examproject.DAL.DataBase;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;

/**
 * This class handles the connection to the database
 *
 * @author Dominik
 */
public class ConnectionManager {

    private SQLServerDataSource source = new SQLServerDataSource();
    private static ConnectionManager instance;

    public static ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    /**
     * Creates a new ConnectionManager, and set the values
     */
    private ConnectionManager() {
        source.setDatabaseName("CS2017B_7_ShoreLineConverter");
        source.setUser("CS2017B_7_java");
        source.setPassword("javajava");
        source.setPortNumber(1433);
        source.setServerName("10.176.111.31");
    }

    /**
     * Returns a Connection object that can be used to connect to the database
     *
     * @return A connection object that can be used to connect to the database
     * @throws SQLServerException If an error occurs during connection
     */
    public Connection getConnection() throws SQLServerException {
        return source.getConnection();
    }
}
