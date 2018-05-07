package warsztat.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Exercise {
	private long id;
	private String title;
	private String description;

	private static final String ID_COLUMN_NAME = "id";
	private static final String INSERT_INTO_EXERCISE = "INSERT INTO warsztaty2.exercises(title, description) VALUES (?,?);";
	private static final String LOAD_EXERCISE_BY_ID = "SELECT * FROM warsztaty2.exercises where warsztaty2.exercises.id =?;";
	private static final String LOAD_ALL_EXERCISES = "SELECT * FROM warsztaty2.exercises;";
	private static final String UPDATE_EXERCISE = "UPDATE warsztaty2.exercises SET title = ?, description =? WHERE id = ?;";
	private static final String DELETE_EXERCISE = "DELETE FROM warsztaty2.exercises WHERE warsztaty2.exercises.id =?;";

	public Exercise(String title, String description) {
		this.title = title;
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Exercise [id=" + id + ", title=" + title + ", description=" + description + "]";
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String generatedColumns[] = { ID_COLUMN_NAME };
			PreparedStatement preparedStatement = conn.prepareStatement(INSERT_INTO_EXERCISE, generatedColumns);
			preparedStatement.setString(1, this.title);
			preparedStatement.setString(2, this.description);
			preparedStatement.execute();
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				this.id = resultSet.getLong(1);
			}
		} else {
			PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_EXERCISE);
			preparedStatement.setString(1, this.title);
			preparedStatement.setString(2, this.description);
			preparedStatement.setLong(3, this.id);
			preparedStatement.executeUpdate();
		}
	}

	public static Exercise exerciseLoadById(Connection conn, long id) throws SQLException {
		PreparedStatement preparedStatement = conn.prepareStatement(LOAD_EXERCISE_BY_ID);
		preparedStatement.setLong(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()) {
			Exercise exercise = creatExercise(resultSet);
			return exercise;

		}
		return null;
	}

	public static Exercise[] allExercises(Connection conn) throws SQLException {
		ArrayList<Exercise> exercises = new ArrayList<Exercise>();
		PreparedStatement preparedStatement = conn.prepareStatement(LOAD_ALL_EXERCISES);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			exercises.add(creatExercise(resultSet));
		}
		Exercise[] exercisesArray = new Exercise[exercises.size()];
		exercisesArray = exercises.toArray(exercisesArray);
		return exercisesArray;
	}

	public static Exercise creatExercise(ResultSet resultSet) throws SQLException {
		String title = resultSet.getString("title");
		String description = resultSet.getString("description");
		Exercise exercise = new Exercise(title, description);
		exercise.id = resultSet.getLong(ID_COLUMN_NAME);
		return exercise;
	}

	public void deleteExercise(Connection conn) throws SQLException {
		if (this.id != 0) {
			PreparedStatement preparedStatement = conn.prepareStatement(DELETE_EXERCISE);
			preparedStatement.setLong(1, this.id);
			preparedStatement.execute();
			this.id = 0;
		}
	}

}
