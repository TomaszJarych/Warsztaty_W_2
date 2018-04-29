package warsztat;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
	 static final String CREATE_DATABASE_STATEMENT = "CREATE DATABASE warsztaty2;";
	 private static final String CREATE_TABLE_USER_GROUPS_STATEMENT =
		     "CREATE TABLE warsztaty2.user_groups("
		         + "id INT AUTO_INCREMENT NOT NULL, "
		         + "name VARCHAR(255) NOT NULL, "
		         + "PRIMARY KEY(id))";

		 private static final String CREATE_TABLE_EXERCISES_STATEMENT =
		     "CREATE TABLE warsztaty2.exercises("
		         + "id INT AUTO_INCREMENT NOT NULL, "
		         + "title VARCHAR(255), "
		         + "description TEXT, "
		         + "PRIMARY KEY(id))";

		 private static final String CREATE_TABLE_SOLUTIONS_STATEMENT =
		     "CREATE TABLE warsztaty2.solutions("
		         + "id INT AUTO_INCREMENT, "
		         + "created DATETIME, "
		         + "updated DATETIME, "
		         + "description VARCHAR(255), "
		         + "exercise INT, "
		         + "users_id BIGINT, "
		         + "PRIMARY KEY (id), "
		         + "FOREIGN KEY (users_id) REFERENCES warsztaty2.users(id))";

		 private static final String CREATE_TABLE_USERS_STATEMENT =
		     "CREATE TABLE warsztaty2.users("
		         + "id BIGINT(20) NOT NULL AUTO_INCREMENT, "
		         + "username VARCHAR(255) NOT NULL, "
		         + "email VARCHAR(255) NOT NULL UNIQUE , "
		         + "password VARCHAR(245) NOT NULL, "
		         + "user_group_id INT(11) NOT NULL, "
		         + "PRIMARY KEY (id), "
		         + "FOREIGN KEY (user_group_id) REFERENCES warsztaty2.user_groups(id))";
	 public static void main(String[] args) throws SQLException {
		try (Connection conn = DbUtil.createConnection(); Statement stat = conn.createStatement()){
			stat.execute(CREATE_DATABASE_STATEMENT);
			stat.execute(CREATE_TABLE_USER_GROUPS_STATEMENT);
			stat.execute(CREATE_TABLE_EXERCISES_STATEMENT);
			stat.execute(CREATE_TABLE_USERS_STATEMENT);
			stat.execute(CREATE_TABLE_SOLUTIONS_STATEMENT);
			
			
		}
	}
}
