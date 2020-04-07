package crypto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionGenerator {

	public static Connection generate() {
		
		// Database parameters
		final String dbHost = "127.0.0.1";
		final String dbUser = "root";
		final String dbPass = "";
		final String dbName = "kaese";
		final String dbDriver = "jdbc:mysql://";
		final String connectionCommand = dbDriver + dbHost + "/" + dbName + "?user=" + dbUser + "&password=" + dbPass;

		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(connectionCommand);
			if (conn == null) {
				try {
					throw new SQLException();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			conn.setAutoCommit(false);
		} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}

}
