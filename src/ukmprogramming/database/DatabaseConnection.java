package ukmprogramming.database;

/* Import Kebutuhan Module */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	
	/* Login Database Informasi */
	private static String dbUrl = "jdbc:mysql://localhost:3306/namaDatabase";
	private static String dbUsername = "username";
	private static String dbPassword = "password";
	
	/* Function untuk koneksi ke Database */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
	}

}
