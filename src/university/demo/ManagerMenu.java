package university.demo;

import university.enums.RequestStatus;
import university.models.Course;
import university.models.Manager;
import university.models.RegistrationRequest;
import university.models.Student;
import university.models.Teacher;
import university.patterns.UniversityDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public final class ManagerMenu {
    private ManagerMenu() {
    }

    public static void open(UniversityDatabase db, Manager manager) {
        while (true) {
            System.out.println("\n--- Manager Menu ---");
            System.out.println("1. List users");
            System.out.println("2. Add course");
            System.out.println("3. Process registration requests");
            System.out.println("4. View students (alphabetical)");
            System.out.println("5. View teachers (alphabetical)");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");
            if (choice == 0) {
                return;
            }
            if (choice == 1) {
                db.getUsers().forEach(u -> System.out.println(u.getId() + " | " + u.print()));
            } else if (choice == 2) {
                String code = ConsoleUtils.ask("Course code: ");
                String name = ConsoleUtils.ask("Course name: ");
                int credits = ConsoleUtils.askInt("Credits: ");
                Teacher teacher = db.getUsers().stream()
                        .filter(u -> u instanceof Teacher)
                        .map(u -> (Teacher) u)
                        .findFirst()
                        .orElse(null);
                db.getCourses().add(new Course(code, name, credits, teacher));
                System.out.println("Course added.");
            } else if (choice == 3) {
                for (RegistrationRequest request : db.getRegistrationRequests()) {
                    if (request.getStatus() != RequestStatus.PENDING) {
                        continue;
                    }
                    System.out.println("Request: " + request.getStudent().getName() + " -> " + request.getCourse());
                    int decision = ConsoleUtils.askInt("1 approve / 2 reject: ");
                    request.setStatus(decision == 1 ? RequestStatus.APPROVED : RequestStatus.REJECTED);
                }
                System.out.println("Requests processed.");
            } else if (choice == 4) {
                viewStudentsAlphabetical(db);
            } else if (choice == 5) {
                viewTeachersAlphabetical(db);
            }
        }
    }

    private static void viewStudentsAlphabetical(UniversityDatabase db) {
        List<Student> students = new ArrayList<>();
        for (university.models.User user : db.getUsers()) {
            if (user instanceof Student) {
                students.add((Student) user);
            }
        }

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        students.sort(Comparator.comparing(university.models.User::getName));
        System.out.println("\n--- Students (Alphabetical) ---");
        for (Student student : students) {
            System.out.println(student.getId() + " | " + student.getName() + " | " +
                    "Year: " + student.getYear() + " | " + student.getDegreeType());
        }
    }

    private static void viewTeachersAlphabetical(UniversityDatabase db) {
        List<Teacher> teachers = new ArrayList<>();
        for (university.models.User user : db.getUsers()) {
            if (user instanceof Teacher) {
                teachers.add((Teacher) user);
            }
        }

        if (teachers.isEmpty()) {
            System.out.println("No teachers found.");
            return;
        }

        teachers.sort(Comparator.comparing(university.models.User::getName));
        System.out.println("\n--- Teachers (Alphabetical) ---");
        for (Teacher teacher : teachers) {
            System.out.println(teacher.getId() + " | " + teacher.getName() + " | " +
                    "Title: " + teacher.getTitle() + " | Researcher: " + teacher.isResearcher());
        }
    }
}
