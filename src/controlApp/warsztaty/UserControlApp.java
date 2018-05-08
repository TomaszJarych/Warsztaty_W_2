package controlApp.warsztaty;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import warsztat.DbUtil;
import warsztat.entities.User;

public class UserControlApp {
	public static void main(String[] args) {
		try (Connection conn = DbUtil.createConnection(); Scanner scan = new Scanner(System.in)) {
			String operations = "";
			do {
				User[] users = User.loadAllUsers(conn);
				for (User user : users) {
					System.out.println(user.toString());
				}
				System.out.println("");
				System.out.println("Wybierz operację. Wpisz  :"+"\n");
				System.out.println("add - dodanie nowego użytkownika "+"\n"+"edit - edycja użytkownika"+"\n"+""
						+ "delete - usunięcie użytownika"+"\n"+"quit - zakończenie programu");
				operations = getString(scan);
				switch (operations) {
				case "add":
					System.out.println("Wybrano opcję 'add'");
					System.out.println("Podaj imię i nazwisko użytkownika ");
					String name = getString(scan);
					System.out.println("Podaj email: ");
					String email = getString(scan);
					System.out.println("Wpisz hasło: ");
					String password = getString(scan);
					System.out.println("Podaj numer grupy");
					long userGroup = getID(scan);
					User user = new User(name, email, password, userGroup);
					user.saveToDB(conn);
					System.out.println("Dodano użytkownika");
					break;

				case "edit":
					System.out.println("Wybrano opcję 'edit'. Wybierz id użytkownika: ");
					long id = getID(scan);
					User userToEdit = User.loadUserById(conn, id);
					System.out.println("Podaj imię i nazwisko użytkownika ");
					String name2 = getString(scan);
					System.out.println("Podaj email: ");
					String email2 = getString(scan);
					System.out.println("Wpisz hasło: ");
					String password2 = getString(scan);
					System.out.println("Podaj numer grupy");
					long userGroup2 = getID(scan);
					userToEdit.setUserName(name2);
					userToEdit.setEmail(email2);
					userToEdit.setPassword(password2);
					userToEdit.setUserGroupId(userGroup2);
					userToEdit.save(conn);
					System.out.println("Zmodyfikowano użytkownika");

					break;
				case "delete":
					System.out.println("Wybrano opcję 'delete'");
					System.out.println("Podaj id użytkownika");
					long idToDelete = getID(scan);
					User userToDelete = User.loadUserById(conn, idToDelete);
					userToDelete.delete(conn);
					System.out.println("Usunięto użytkownika");

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
