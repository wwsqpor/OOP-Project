package university.demo;

import java.util.ArrayList;
import java.util.List;

import university.enums.RequestStatus;
import university.exceptions.CreditLimitExceededException;
import university.exceptions.FailLimitExceededException;
import university.exceptions.NotResearcherException;
import university.models.Course;
import university.models.RegistrationRequest;
import university.models.ResearchProject;
import university.models.Student;
import university.models.Teacher;
import university.models.User;
import university.patterns.UniversityDatabase;
import university.utils.ResearchService;

public final class StudentMenu {
    private StudentMenu() {
    }

    public static void open(UniversityDatabase db, Student student) {
        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View courses");
            System.out.println("2. Register course directly");
            System.out.println("3. Create registration request");
            System.out.println("4. View marks");
            System.out.println("5. Join research project");
            System.out.println("6. Rate teacher");
            System.out.println("7. View course teacher info");
            System.out.println("8. View transcript");
            System.out.println("9. View schedule");
            System.out.println("10. Financial info");
            System.out.println("11. Pay for retake");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");
            if (choice == 0) {
                return;
            }
            switch (choice) {
                case 1 -> db.getCourses().forEach(System.out::println);
                case 2 -> registerDirect(db, student);
                case 3 -> createRequest(db, student);
                case 4 -> student.getMarks().forEach((k, v) -> System.out.println(k + " -> " + v));
                case 5 -> joinProject(db, student);
                case 6 -> rateTeacher(db);
                case 7 -> viewCourseTeacherInfo(db);
                case 8 -> viewTranscript(student);
                case 9 -> university.utils.ScheduleService.viewStudentSchedule(db, student);
                case 10 -> System.out.printf("Balance: %.2f | Scholarship: %s%n",
                        student.getBalance(), student.isHasScholarship() ? "Yes" : "No");
                case 11 -> payForRetake(student);
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void payForRetake(Student student) {
        double cost = 50000.0;
        System.out.println("Retake cost: " + cost);
        try {
            student.pay(cost);
            System.out.println("Payment successful. Remaining balance: " + student.getBalance());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void viewCourseTeacherInfo(UniversityDatabase db) {
        String code = ConsoleUtils.ask("Course code: ");
        Course course = db.findCourseByCode(code);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        System.out.println("Instructors for " + course.getName() + ":");
        course.getInstructors().forEach(t -> {
            System.out.println(t.getName() + " | Title: " + t.getTitle() + " | Rating: "
                    + (t.getRatingsCount() == 0 ? "N/A" : String.format("%.2f", t.getAverageRating())));
        });
    }

    private static void viewTranscript(Student student) {
        System.out.println("=== Transcript for " + student.getName() + " ===");
        if (student.getMarks().isEmpty()) {
            System.out.println("No marks recorded.");
            return;
        }
        student.getMarks().forEach((code, mark) -> {
            System.out.printf("%-10s : %s%n", code, mark.toString());
        });
        double totalGpa = student.getMarks().values().stream()
                .mapToDouble(m -> m.total())
                .average()
                .orElse(0.0);
        System.out.printf("Average Score: %.2f%n", totalGpa);
    }

    private static void registerDirect(UniversityDatabase db, Student student) {
        String code = ConsoleUtils.ask("Course code: ");
        Course course = db.findCourseByCode(code);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        try {
            student.registerCourse(course);
            System.out.println("Registered.");
        } catch (CreditLimitExceededException | FailLimitExceededException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void createRequest(UniversityDatabase db, Student student) {
        String code = ConsoleUtils.ask("Course code: ");
        Course course = db.findCourseByCode(code);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        RegistrationRequest request = new RegistrationRequest(student, course);
        request.setStatus(RequestStatus.PENDING);
        db.getRegistrationRequests().add(request);
        System.out.println("Request submitted.");
    }

    private static void joinProject(UniversityDatabase db, Student student) {
        if (db.getResearchProjects().isEmpty()) {
            System.out.println("No research projects.");
            return;
        }
        for (int i = 0; i < db.getResearchProjects().size(); i++) {
            System.out.println((i + 1) + ". " + db.getResearchProjects().get(i).getName());
        }
        int idx = ConsoleUtils.askInt("Project number: ") - 1;
        if (idx < 0 || idx >= db.getResearchProjects().size()) {
            System.out.println("Invalid project.");
            return;
        }
        ResearchProject project = db.getResearchProjects().get(idx);
        try {
            ResearchService.joinProject(student, project);
            System.out.println("Joined project.");
        } catch (NotResearcherException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void rateTeacher(UniversityDatabase db) {
        List<Teacher> teachers = new ArrayList<>();
        for (User user : db.getUsers()) {
            if (user instanceof Teacher) {
                teachers.add((Teacher) user);
            }
        }
        if (teachers.isEmpty()) {
            System.out.println("No teachers available.");
            return;
        }

        for (int i = 0; i < teachers.size(); i++) {
            Teacher teacher = teachers.get(i);
            String averageText = teacher.getRatingsCount() == 0
                    ? "N/A"
                    : String.format("%.2f", teacher.getAverageRating());
            System.out.println((i + 1) + ". " + teacher.getName() + " (avg: " + averageText + ", ratings: "
                    + teacher.getRatingsCount() + ")");
        }

        int idx = ConsoleUtils.askInt("Teacher number: ") - 1;
        if (idx < 0 || idx >= teachers.size()) {
            System.out.println("Invalid teacher.");
            return;
        }

        int rating = ConsoleUtils.askInt("Your rating (1..5): ");
        if (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5.");
            return;
        }

        Teacher teacher = teachers.get(idx);
        teacher.addRating(rating);
        System.out.println("Rating submitted. New average: " + String.format("%.2f", teacher.getAverageRating()));
    }

    private static void getTeachersInfo(UniversityDatabase db) {
        for (User user : db.getUsers()) {
            if (user instanceof Teacher) {
                Teacher teacher = (Teacher) user;
                String averageText = teacher.getRatingsCount() == 0
                        ? "N/A"
                        : String.format("%.2f", teacher.getAverageRating());
                System.out.println(teacher.getName() + " | Title: " + teacher.getTitle() + " | Researcher: "
                        + teacher.isResearcher() + " | H-index: " + teacher.getHIndex() + " | Avg rating: "
                        + averageText + " | Ratings count: " + teacher.getRatingsCount());
            }
        }
    }
}
