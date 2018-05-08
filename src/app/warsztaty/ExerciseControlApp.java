package app.warsztaty;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import warsztat.DbUtil;
import warsztat.entities.Exercise;

public class ExerciseControlApp {

	public static void main(String[] args) {
		try (Connection conn = DbUtil.createConnection(); Scanner scan = new Scanner(System.in)) {
			String operations = "";
			do {
				Exercise[] allExercises = Exercise.allExercises(conn);
				for (Exercise exercise : allExercises) {
					System.out.println(exercise.toString());
				}
				System.out.println("");
				System.out.println("Wybierz operację. Wpisz  :" + "\n");
				System.out.println("add - dodanie nowego zadania " + "\n" + "edit - edycja zadania" + "\n" + ""
						+ "delete - usunięcie zadania" + "\n" + "quit - zakończenie programu");
				operations = getString(scan);
				switch (operations) {
				case "add":
					System.out.println("Wybrano opcję 'add'");
					System.out.println("Podaj tytuł zadania");
					String title = getString(scan);
					System.out.println("Podaj opis zadania");
					String description = getString(scan);
					System.out.println("Dodano zadanie");
					Exercise exercise = new Exercise(title, description);
					exercise.saveToDB(conn);
					break;

				case "edit":
					System.out.println("Wybrano opcję 'edit'. Wybierz id zadania: ");
					long id = getID(scan);
					Exercise exercise2 = Exercise.exerciseLoadById(conn, id);
					System.out.println(exercise2.toString());
					System.out.println("Podaj tytuł zadania");
					String title2 = getString(scan);
					exercise2.setTitle(title2);
					System.out.println("Podaj opis zadania");
					String description2 = getString(scan);
					exercise2.setDescription(description2);
					exercise2.saveToDB(conn);
					System.out.println("Zmodyfikowano zadanie");

					break;
				case "delete":
					System.out.println("Wybrano opcję 'delete'");
					System.out.println("Podaj id zadania");
					long idToDelete = getID(scan);
					Exercise exerciseToDelete = Exercise.exerciseLoadById(conn, idToDelete);
					exerciseToDelete.deleteExercise(conn);
					System.out.println("Usunięto zadanie");

					break;
				case "quit":
					System.out.println("Zakończono program");

					break;
				}

			} while (!operations.equals("quit"));
		} catch (SQLException | InputMismatchException e) {
			e.printStackTrace();
		}
	}

	public static String getString(Scanner scan) throws InputMismatchException {
		String text = scan.nextLine();
		while (text.isEmpty()) {
			text = scan.nextLine();
		}
		return text;
	}

	public static long getID(Scanner scan) throws InputMismatchException {
		while (!scan.hasNextLong()) {
			System.out.println("Podaj prawidłowy numer");
			scan.nextLong();
		}
		return scan.nextLong();
	}
}
