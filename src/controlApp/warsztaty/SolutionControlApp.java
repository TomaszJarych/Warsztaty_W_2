package controlApp.warsztaty;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import warsztat.DbUtil;
import warsztat.entities.Exercise;
import warsztat.entities.Solution;
import warsztat.entities.User;

public class SolutionControlApp {

	public static void main(String[] args) {
		try (Connection conn = DbUtil.createConnection(); Scanner scan = new Scanner(System.in)) {
			String operations = "";
			do {
				
				System.out.println("Wybierz operację. Wpisz  :" + "\n");
				System.out.println("add - przypisz zadanie " + "\n" + "view - wyświetl zadania" + "\n" + ""
						+  "quit - zakończenie programu");
				operations = getString(scan);
				switch (operations) {
				case "add":
					System.out.println("Wybrano opcję 'add'");
					User[] users = User.loadAllUsers(conn);
					for (User user : users) {
						System.out.println(user.toString());
					}
					System.out.println("Podaj id użytkownika");
					long userId = getID(scan);
					User user = User.loadUserById(conn, userId);
					System.out.println(user.toString());
					Exercise[] exercises = Exercise.allExercises(conn);
					for (Exercise exercise : exercises) {
						System.out.println(exercise.toString());
					}
					System.out.println("Podaj id zadania ");
					long exerciseId = getID(scan);
					Exercise selectedExercise = Exercise.exerciseLoadById(conn, exerciseId);
					System.out.println(selectedExercise.toString());
					String description = null;
					Solution solution = new Solution(description, selectedExercise.getId(), user.getId());
					solution.saveToDB(conn);
					break;

				case "view":
					System.out.println("Wybrano opcję 'view'. Wybierz id użytkownika: ");
					User[] users2 = User.loadAllUsers(conn);
					for (User user2 : users2) {
						System.out.println(user2.toString());			
					}
					long userIdToView = getID(scan);
					User user2 = User.loadUserById(conn, userIdToView);
					Solution[] solutions2 = Solution.loadAllByUserId(conn, user2);
					for (Solution solution2 : solutions2) {
						System.out.println(solution2.toString());
					}
					

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
			scan.next();
		}
		return scan.nextLong();
	}
}
