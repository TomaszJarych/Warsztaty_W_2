package warsztat;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
	
	public static Connection createConnection() throws SQLException{
		return DriverManager.getConnection("jdbc:mysql://localhost:3306/?useSSL=false", "root", "coderslab");
// return DriverManager.getConnection("jdbc:mysql://localhost:3306/?characterEncoding=utf8&useSSL=false", "root", "coderslab");
// jeżeli mają być polskie znaki
	}

}
