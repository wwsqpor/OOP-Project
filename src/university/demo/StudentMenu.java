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
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");
            if (choice == 0) {
                return;
            }
            if (choice == 1) {
                db.getCourses().forEach(System.out::println);
            } else if (choice == 2) {
                registerDirect(db, student);
            } else if (choice == 3) {
                createRequest(db, student);
            } else if (choice == 4) {
                student.getMarks().forEach((k, v) -> System.out.println(k + " -> " + v));
            } else if (choice == 5) {
                joinProject(db, student);
            } else if (choice == 6) {
                rateTeacher(db);
            }
        }
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
}
