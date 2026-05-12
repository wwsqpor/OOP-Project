package university.demo;

import university.enums.DegreeType;
import university.enums.ManagerType;
import university.enums.TeacherTitle;
import university.models.*;
import university.patterns.UniversityDatabase;

import java.util.Optional;

public final class AdminMenu {
    private AdminMenu() {
    }

    public static void open(UniversityDatabase db) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add user");
            System.out.println("2. Remove user");
            System.out.println("3. Update user info");
            System.out.println("4. Add news");
            System.out.println("5. View logs");
            System.out.println("6. Promote user to Researcher (Decorator)");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");
            if (choice == 0) {
                return;
            }
            switch (choice) {
                case 1 -> addUser(db);
                case 2 -> removeUser(db);
                case 3 -> updateUser(db);
                case 4 -> {
                    String title = ConsoleUtils.ask("Title: ");
                    String body = ConsoleUtils.ask("Body: ");
                    db.getNews().add(new NewsItem(title, body));
                    System.out.println("News added.");
                }
                case 5 -> db.getLogger().getLogs().forEach(System.out::println);
                case 6 -> promoteToResearcher(db);
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void promoteToResearcher(UniversityDatabase db) {
        String id = ConsoleUtils.ask("User ID to promote: ");
        User user = db.findUserById(id);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        if (user instanceof Researcher) {
            System.out.println("User is already a Researcher.");
            return;
        }
        ResearcherDecorator decorated = new ResearcherDecorator(user);
        // Replace user in the list
        db.getUsers().remove(user);
        db.getUsers().add(decorated);
        System.out.println(user.getName() + " promoted to Researcher using Decorator.");
    }

    private static void addUser(UniversityDatabase db) {
        System.out.println("1. Student, 2. Teacher, 3. Manager, 4. Admin, 5. ResearcherEmployee, 6. Dean, 7. Rector");
        int type = ConsoleUtils.askInt("Choose type: ");
        String id = ConsoleUtils.ask("ID: ");
        String name = ConsoleUtils.ask("Name: ");
        String email = ConsoleUtils.ask("Email: ");
        String password = ConsoleUtils.ask("Password: ");

        User user = null;
        switch (type) {
            case 1 -> {
                int year = ConsoleUtils.askInt("Year: ");
                user = new Student(id, name, email, password, year, DegreeType.BACHELOR);
            }
            case 2 -> {
                System.out.println("Title: 1. Tutor, 2. Lector, 3. Senior Lector, 4. Professor");
                int tChoice = ConsoleUtils.askInt("Choose: ");
                TeacherTitle tt = switch (tChoice) {
                    case 2 -> TeacherTitle.LECTOR;
                    case 3 -> TeacherTitle.SENIOR_LECTURER;
                    case 4 -> TeacherTitle.PROFESSOR;
                    default -> TeacherTitle.TUTOR;
                };
                user = new Teacher(id, name, email, password, tt);
            }
            case 3 -> {
                System.out.println("Type: 1. OR, 2. Dean Office, 3. Department");
                int mChoice = ConsoleUtils.askInt("Choose: ");
                ManagerType mt = switch (mChoice) {
                    case 2 -> ManagerType.DEAN_OFFICE;
                    case 3 -> ManagerType.DEPARTMENT;
                    default -> ManagerType.OR;
                };
                user = new Manager(id, name, email, password, mt);
            }
            case 4 -> user = new Admin(id, name, email, password);
            case 5 -> {
                user = new ResearcherEmployee(id, name, email, password);
            }
            case 6 -> user = new Dean(id, name, email, password);
            case 7 -> user = new Rector(id, name, email, password);
        }

        if (user != null) {
            db.getUsers().add(user);
            System.out.println("User added.");
        }
    }

    private static void removeUser(UniversityDatabase db) {
        String id = ConsoleUtils.ask("ID to remove: ");
        boolean removed = db.getUsers().removeIf(u -> u.getId().equals(id));
        System.out.println(removed ? "User removed." : "User not found.");
    }

    private static void updateUser(UniversityDatabase db) {
        String id = ConsoleUtils.ask("ID to update: ");
        User user = db.findUserById(id);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        System.out.println("Current: " + user.getName() + " (" + user.getEmail() + ")");
        String newPassword = ConsoleUtils.ask("New password (leave empty to skip): ");
        if (!newPassword.isBlank()) {
            user.setPassword(newPassword);
            System.out.println("Password updated.");
        }
    }
}
