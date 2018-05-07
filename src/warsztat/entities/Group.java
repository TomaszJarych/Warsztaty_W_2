package warsztat.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import warsztat.DbUtil;

public class Group {
	private long id;
	private String name;

	private static final String ID_COLUMN_NAME = "id";
	private static final String INSERT_INTO_GROUP = "INSERT INTO warsztaty2.user_groups (name)VALUES(?);";
	private static final String LOAD_GROUP_BY_ID = "select * from warsztaty2.user_groups  where warsztaty2.user_groups.id =?;";
	private static final String LOAD_ALL_GROUPS = "select * from warsztaty2.user_groups ;";
	private static final String UPDATE_GROUP = "UPDATE warsztaty2.user_groups SET name = ? where id = ?";
	private static final String DELETE_GROUP ="DELETE FROM warsztaty2.user_groups WHERE warsztaty2.user_groups.id=?;";
	
	public Group(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + "]";
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String generatedColumns[] = { ID_COLUMN_NAME };
			PreparedStatement preparedStatement = conn.prepareStatement(INSERT_INTO_GROUP, generatedColumns);
			preparedStatement.setString(1, this.name);
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				this.id = resultSet.getLong(1);
			}
		} else {
			PreparedStatement preparedStatement2 = conn.prepareStatement(UPDATE_GROUP);
			preparedStatement2.setString(1, this.name);
			preparedStatement2.setLong(2, this.id);
			preparedStatement2.executeUpdate();

		}
	}

	public static  Group loadGroupByID(Connection conn, long id) throws SQLException {
		PreparedStatement preparedStatement = conn.prepareStatement(LOAD_GROUP_BY_ID);
		preparedStatement.setLong(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			Group loadedGroup = createGroup(resultSet);
			return loadedGroup;
		}
		return null;

	}

	public static Group createGroup(ResultSet resultSet) throws SQLException {
		String name = resultSet.getString("name");
		Group loadedGroup = new Group(name);
		loadedGroup.id = resultSet.getLong(ID_COLUMN_NAME);
		return loadedGroup;
	}

	public static Group[] loadAllGroups(Connection conn) throws SQLException {
		ArrayList<Group> allGroups = new ArrayList<>();
		PreparedStatement preparedStatement = conn.prepareStatement(LOAD_ALL_GROUPS);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			allGroups.add(createGroup(resultSet));

		}
		Group[] groups = new Group[allGroups.size()];
		groups = allGroups.toArray(groups);
		return groups;
	}
	public void deleteGroup (Connection conn) throws SQLException{
		if (this.id != 0) {
			PreparedStatement preparedStatement = conn.prepareStatement(DELETE_GROUP);
			preparedStatement.setLong(1, this.id);
			preparedStatement.executeUpdate();
			this.id =0;
			
			
		}
	}

	public static void main(String[] args) {
		try (Connection conn = DbUtil.createConnection()) {
			Group group1 = loadGroupByID(conn, 4);
			group1.deleteGroup(conn);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
