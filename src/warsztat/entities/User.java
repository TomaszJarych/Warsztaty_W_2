package warsztat.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

import com.sun.org.apache.bcel.internal.generic.Select;

import sun.security.pkcs11.Secmod.DbMode;
import warsztat.DbUtil;

public class User {
	public static final String SELECT_FROM_USERS_WHERE_ID = "SELECT	*	FROM	warsztaty2.users	where	id=?";
	private static final String ID_COLUMN_NAME = "ID";
	private static final String INSERT_INTO_USERS = "INSERT	INTO warsztaty2.users (username,	email,	password)	VALUES	(?,	?,	?)";

	private long id;
	private String userName;
	private String email;
	private String password;
	private int userGroupId;

	public User(String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		setPassword(password);
	}

	public long getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public int getUserGroupId() {
		return userGroupId;
	}

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String generatedColumns[] = { ID_COLUMN_NAME };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(INSERT_INTO_USERS, generatedColumns);
			preparedStatement.setString(1, this.userName);
			preparedStatement.setString(2, this.email);
			preparedStatement.setString(3, this.password);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
			}
		}
	}

	static public User loadUserById(Connection conn, long id) throws SQLException {
		PreparedStatement preparedStatement = conn.prepareStatement(SELECT_FROM_USERS_WHERE_ID);
		preparedStatement.setLong(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			User loadedUser = createUser(resultSet);
			return loadedUser;
		}
		return null;
	}

	public static User createUser(ResultSet resultSet) throws SQLException {
		String username = resultSet.getString("username");
		String password = resultSet.getString("password");
		String email = resultSet.getString("email");
		User loadedUser = new User(username, email, password);
		loadedUser.id = resultSet.getLong(ID_COLUMN_NAME);
		return loadedUser;
	}

	static public User[] loadAllUsers(Connection conn) throws SQLException {
		ArrayList<User> users = new ArrayList<User>();
		String sql = "SELECT	*	FROM	Users";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			users.add(createUser(resultSet));
		}
		User[] uArray = new User[users.size()];
		uArray = users.toArray(uArray);
		return uArray;
	}
}
	if	(this.id	==	0)	{
		
}	else	{
		String	sql	=	"UPDATE	Users	SET	username=?,	email=?,	password=?	where	id	=	?";
		PreparedStatement	preparedStatement;
		preparedStatement	=	conn.prepareStatement(sql);
		preparedStatement.setString(1,	this.username);
		preparedStatement.setString(2,	this.email);
		preparedStatement.setString(3,	this.password);
		preparedStatement.setInt(4,	this.id);
		preparedStatement.executeUpdate();
}


	public static void main(String[] args) throws SQLException {

		try (Connection conn = DbUtil.createConnection()) {
			User user1 = new User("Jan Kowalski", "kowal1@gmail.com", "Wrocław");
			// user1.saveToDB(conn);
			User user2 = loadUserById(conn, 1);
			System.out.println(
					user2.getId() + " " + user2.getUserName() + " " + user2.getEmail() + " " + user2.getPassword());

		}
	}
}
