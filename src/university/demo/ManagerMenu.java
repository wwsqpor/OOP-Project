package university.demo;

import university.enums.RequestStatus;
import university.models.Course;
import university.models.Manager;
import university.models.RegistrationRequest;
import university.models.Teacher;
import university.patterns.UniversityDatabase;

public final class ManagerMenu {
    private ManagerMenu() {
    }

    public static void open(UniversityDatabase db, Manager manager) {
        while (true) {
            System.out.println("\n--- Manager Menu ---");
            System.out.println("1. List users");
            System.out.println("2. Add course");
            System.out.println("3. Process registration requests");
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
            }
        }
    }
}
