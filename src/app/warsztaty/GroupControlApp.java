package app.warsztaty;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import warsztat.DbUtil;
import warsztat.entities.Group;

public class GroupControlApp {

	public static void main(String[] args) {
		try (Connection conn = DbUtil.createConnection(); Scanner scan = new Scanner(System.in)) {
			String operations = "";
			do { Group[] groups = Group.loadAllGroups(conn);
				for (Group group : groups) {
					System.out.println(group.toString());
				}
				
				System.out.println("");
				System.out.println("Wybierz operację. Wpisz  :"+"\n");
				System.out.println("add - dodanie nowego zadania "+"\n"+"edit - edycja zadania"+"\n"+""
						+ "delete - usunięcie zadania"+"\n"+"quit - zakończenie programu");
				operations = getString(scan);
				switch (operations) {
				case "add":
					System.out.println("Wybrano opcję 'add'");
					System.out.println("Podaj nazwę grupy");
					String name = getString(scan);
					Group group = new Group(name);
					group.saveToDB(conn);
					break;

				case "edit":
					System.out.println("Wybrano opcję 'edit'. Wybierz id zadania: ");
					long idGroupToEdit = getID(scan);
					Group group2 = Group.loadGroupByID(conn, idGroupToEdit);
					System.out.println(group2.toString());
					System.out.println("Podaj nazwę grupy");
					String newName = getString(scan);
					group2.setName(newName);
					group2.saveToDB(conn);
					

					break;
				case "delete":
					System.out.println("Wybrano opcję 'delete'");
					System.out.println("Podaj id grupy");
					long idGroupToDelete = getID(scan);
					Group group3 = Group.loadGroupByID(conn, idGroupToDelete);
					group3.deleteGroup(conn);
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
