package warsztat.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import warsztat.DbUtil;

public class Solution {
	private long id;
	private LocalDateTime created;
	private LocalDateTime updated;
	private String description;
	private long exerciseId;
	private long usersId;

	private static final String ID_COLUMN_NAME = "id";
	private static final String INSERT_INTO_SOLUTION = "INSERT INTO warsztaty2.solutions(created, description, exercise_id, users_id)VALUES (?,?,?,?);";
	private static final String LOAD_SOLUTION_BY_ID = "SELECT * FROM warsztaty2.solutions where id =?";
	private static final String LOAD_ALL_SOLUTIONS = "SELECT * FROM warsztaty2.solutions;";
	private static final String LOAD_ALL_SOLUTIONS_FROM_USER = "SELECT * FROM warsztaty2.solutions inner join warsztaty2.users on warsztaty2.solutions.users_id = warsztaty2.users.id WHERE warsztaty2.users.id=? ;";
	private static final String LOADALL_BY_EXERCISE_ID = "SELECT * FROM warsztaty2.solutions where warsztaty2.solutions.exercise_id =? order by warsztaty2.solutions.updated asc;";
	private static final String UPDATE_SOLUTION = "UPDATE warsztaty2.solutions SET updated = ?, description =?  WHERE id =? ;";
	private static final String DELETE_SOLUTION = "DELETE FROM `warsztaty2`.`solutions` WHERE warsztaty2.solutions.id =?;";

	public Solution(String description, long exerciseId, long usersId) {
		LocalDateTime localDateTime = LocalDateTime.now();
		this.description = description;
		this.exerciseId = exerciseId;
		this.usersId = usersId;
		this.created = localDateTime;
	}

	public long getId() {
		return id;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public String getDescription() {
		return description;
	}

	public long getExercise_id() {
		return exerciseId;
	}

	public long getUsers_id() {
		return usersId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Solution [id=" + id + ", created=" + created + ", updated=" + updated + ", description=" + description
				+ ", exercise_id=" + exerciseId + ", users_id=" + usersId + "]";
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String generatedColumns[] = { ID_COLUMN_NAME };
			PreparedStatement preparedStatement = conn.prepareStatement(INSERT_INTO_SOLUTION, generatedColumns);
			Timestamp timestamp = Timestamp.valueOf(this.created);
			preparedStatement.setTimestamp(1, timestamp);
			preparedStatement.setString(2, this.description);
			preparedStatement.setLong(3, this.exerciseId);
			preparedStatement.setLong(4, this.usersId);
			preparedStatement.executeUpdate();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				this.id = resultSet.getLong(1);
			}
		} else {
			PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_SOLUTION);
			Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
			preparedStatement.setTimestamp(1, timestamp);
			preparedStatement.setString(2, this.description);
			preparedStatement.setLong(3, this.id);
			preparedStatement.executeUpdate();
		}
	}

	public static Solution loadSolutionById(Connection conn, long id) throws SQLException {
		PreparedStatement preparedStatement = conn.prepareStatement(LOAD_SOLUTION_BY_ID);
		preparedStatement.setLong(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			Solution solution = createSolution(resultSet);
			return solution;
		}
		return null;
	}

	public static Solution createSolution(ResultSet resultSet) throws SQLException {
		String description = resultSet.getString("description");
		long exerciseId = resultSet.getLong("exercise_id");
		long userID = resultSet.getLong("users_id");
		Solution solution = new Solution(description, exerciseId, userID);
		solution.id = resultSet.getLong(ID_COLUMN_NAME);
		solution.created = resultSet.getTimestamp("created").toLocalDateTime();
		solution.updated = resultSet.getTimestamp("updated").toLocalDateTime();
		return solution;
	}

	public static Solution[] allSolutions(Connection conn) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		PreparedStatement preparedStatement = conn.prepareStatement(LOAD_ALL_SOLUTIONS);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			solutions.add(createSolution(resultSet));
		}
		Solution[] solutionsArray = new Solution[solutions.size()];
		solutionsArray = solutions.toArray(solutionsArray);
		return solutionsArray;
	}

	public void deleteSolution(Connection conn) throws SQLException {
		if (this.id != 0) {
			PreparedStatement preparedStatement = conn.prepareStatement(DELETE_SOLUTION);
			preparedStatement.setLong(1, this.id);
			preparedStatement.execute();
			this.id = 0;

		}
	}

	public static Solution[] loadAllByUserId(Connection conn, User user) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		PreparedStatement preparedStatement = conn.prepareStatement(LOAD_ALL_SOLUTIONS_FROM_USER);
		preparedStatement.setLong(1, user.getId());
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			solutions.add(createSolution(resultSet));
		}
		Solution[] solutionsArray = new Solution[solutions.size()];
		solutionsArray = solutions.toArray(solutionsArray);
		return solutionsArray;
	}
	public static Solution[] loadAllByExerciseId (Connection conn, long exerciseId ) throws SQLException {
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		PreparedStatement preparedStatement = conn.prepareStatement(LOADALL_BY_EXERCISE_ID);
		preparedStatement.setLong(1, exerciseId);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			solutions.add(createSolution(resultSet));
		}
		Solution[] solutionsArray = new Solution[solutions.size()];
		solutionsArray = solutions.toArray(solutionsArray);
		return solutionsArray;

	}
	
}
