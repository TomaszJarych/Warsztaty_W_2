package userApp.warsztaty;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import warsztat.DbUtil;
import warsztat.entities.Solution;
import warsztat.entities.User;

public class UserApp {
	public static void main(String[] args) {
		try (Connection conn = DbUtil.createConnection(); Scanner scan = new Scanner(System.in)) {

			String operations = "";
			User user = getUser(conn, scan);

			do {

				System.out.println("Wybierz operację. Wpisz  :" + "\n");
				System.out.println("add - przypisz zadanie " + "\n" + "view - wyświetl zadania" + "\n" + ""
						+ "quit - zakończenie programu");
				operations = getString(scan);
				switch (operations) {
				case "add":
					System.out.println("Wybrano opcję 'add'");
					Solution[] solutions = Solution.loadAllByUserId(conn, user);
					for (Solution solution : solutions) {
						if (solution.getDescription().isEmpty())
							System.out.println(solution.toString());
					}
					System.out.println("Podaj id zadania do rozwiązania");
					long solutionId = getID(scan);
					Solution solution = Solution.loadSolutionById(conn, solutionId);
					if (solution.getDescription().isEmpty()) {
						System.out.println("Wpisz rozwiązanie");
						String userDescription = getString(scan);
						solution.setDescription(userDescription);
						solution.saveToDB(conn);
						// zgodnie ze specyfikacją dodaje wpisane rozwiązanie
						// tylko wtedy, gdy nie istnieje wcześniejsze
						// rozwiązanie, nie można edytować ani usuwać
						// rozwiązania
					} else {
						System.out.println("Zadanie już zostało rozwiązane");
						
					}

					break;

				case "view":
					Solution[] solutionsToView = Solution.loadAllByUserId(conn, user);
					for (Solution solutionToView : solutionsToView) {
						if (!solutionToView.getDescription().isEmpty())
							System.out.println(solutionToView.toString());
					}

					break;
				case "quit":
					System.out.println("Zakończono program");

					break;
				}

			} while (!operations.equals("quit"));
		} catch (SQLException | InputMismatchException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("Użytkownik nie istniej");
		}
	}

	public static User getUser(Connection conn, Scanner scan) throws SQLException, NullPointerException {
		System.out.println("Podaj swoje id");
		long userId = getID(scan);
		User loggedUser = User.loadUserById(conn, userId);
		return loggedUser;

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
