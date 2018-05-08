package warsztat.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.mindrot.jbcrypt.BCrypt;

import warsztat.DbUtil;

public class User {
	private static final String DELETE_FROM_USERS_WHERE_ID = "DELETE FROM warsztaty2.users	WHERE	id=	?";
	private static final String SELECT_FROM_USERS_WHERE_ID = "SELECT * FROM	warsztaty2.users	where	id=?";
	private static final String ID_COLUMN_NAME = "ID";
	private static final String LOAD_ALL_BY_GRUP_ID = "SELECT * FROM warsztaty2.users where warsztaty2.users.user_group_id = ?;";
	private static final String INSERT_INTO_USERS = "INSERT	INTO warsztaty2.users (username,	email,	password, user_group_id)	VALUES	(?,	?,	?,?)";
	private static final String UPDATE_USERS = "UPDATE warsztaty2.users SET username=?, email=?, password=?, user_group_id = ? where id = ?";

	private long id;
	private String userName;
	private String email;
	private String password;
	private long userGroupId;

	public User(String userName, String email, String password, long userGroupId) {
		this.userName = userName;
		this.email = email;
		setPassword(password);
		this.userGroupId = userGroupId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUserGroupId(long userGroupId) {
		this.userGroupId = userGroupId;
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

	public long getUserGroupId() {
		return userGroupId;
	}

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", email=" + email + ", userGroupId=" + userGroupId + "]";
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String generatedColumns[] = { ID_COLUMN_NAME };
			PreparedStatement preparedStatement = conn.prepareStatement(INSERT_INTO_USERS, generatedColumns);
			preparedStatement.setString(1, this.userName);
			preparedStatement.setString(2, this.email);
			preparedStatement.setString(3, this.password);
			preparedStatement.setLong(4, this.userGroupId);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
			}
		}
	}

	public void save(Connection conn) throws SQLException {
		if (this.id == 0) {
			String generatedColumns[] = { ID_COLUMN_NAME };
			PreparedStatement preparedStatement = conn.prepareStatement(INSERT_INTO_USERS, generatedColumns);
			preparedStatement.setString(1, this.userName);
			preparedStatement.setString(2, this.email);
			preparedStatement.setString(3, this.password);
			preparedStatement.setLong(4, this.userGroupId);
			preparedStatement.executeQuery();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getLong(1);

			}
		} else {
			PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_USERS);
			preparedStatement.setString(1, this.userName);
			preparedStatement.setString(2, this.email);
			preparedStatement.setString(3, this.password);
			preparedStatement.setLong(4, this.userGroupId);
			preparedStatement.setLong(5, this.id);
			preparedStatement.executeUpdate();

		}

	}

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			PreparedStatement preparedStatement = conn.prepareStatement(DELETE_FROM_USERS_WHERE_ID);
			preparedStatement.setLong(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
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
		long userGroupId = resultSet.getLong("user_group_id");
		User loadedUser = new User(username, email, password, userGroupId);
		loadedUser.id = resultSet.getLong(ID_COLUMN_NAME);
		return loadedUser;
	}

	static public User[] loadAllUsers(Connection conn) throws SQLException {
		ArrayList<User> users = new ArrayList<User>();
		String sql = "SELECT * FROM warsztaty2.users";
		PreparedStatement preparedStatement = conn.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			users.add(createUser(resultSet));
		}
		User[] uArray = new User[users.size()];
		uArray = users.toArray(uArray);
		return uArray;
	}

	static public User[] loadAllByGrupId(Connection conn, long userGroupId) throws SQLException {
		ArrayList<User> users = new ArrayList<User>();
		PreparedStatement preparedStatement = conn.prepareStatement(LOAD_ALL_BY_GRUP_ID);
		preparedStatement.setLong(1, userGroupId);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			users.add(createUser(resultSet));
		}
		User[] uArray = new User[users.size()];
		uArray = users.toArray(uArray);
		return uArray;
	}
}
