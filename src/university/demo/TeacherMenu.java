package university.demo;

import java.time.LocalDate;
import java.util.List;
import university.exceptions.CourseNotTaughtException;
import university.models.Mark;
import university.models.Course;
import university.models.ResearchPaper;
import university.models.Student;
import university.models.Teacher;
import university.patterns.UniversityDatabase;
import university.utils.ResearchService;
import university.models.Message;

import university.models.Complaint;
import university.models.Employee;

public final class TeacherMenu {
    private TeacherMenu() {
    }

    public static void open(UniversityDatabase db, Teacher teacher) {
        while (true) {
            System.out.println("\n--- Teacher Menu ---");
            System.out.println("1. View my courses");
            System.out.println("2. Manage course");
            System.out.println("3. View students (all)");
            System.out.println("4. Put mark");
            System.out.println("5. Add research paper");
            System.out.println("6. View my papers");
            System.out.println("7. View messages");
            System.out.println("8. Send message");
            System.out.println("9. Send complaint");
            System.out.println("10. Send request to Dean/Rector");
            System.out.println("11. View schedule");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");
            if (choice == 0) {
                return;
            }
            switch (choice) {
                case 1 -> viewMyCourses(teacher);
                case 2 -> manageCourse(db, teacher);
                case 3 -> viewStudents(db);
                case 4 -> putMark(db, teacher);
                case 5 -> addPaper(teacher);
                case 6 -> showSortedPapers(teacher);
                case 7 -> showMessages(db, teacher);
                case 8 -> sendMessage(db, teacher);
                case 9 -> sendComplaint(db, teacher);
                case 10 -> sendRequest(db, teacher);
                case 11 -> university.utils.ScheduleService.viewTeacherSchedule(db, teacher);
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void sendRequest(UniversityDatabase db, Teacher teacher) {
        String content = ConsoleUtils.ask("Request content: ");
        EmployeeRequest request = new EmployeeRequest(teacher.getId(), content);
        db.getEmployeeRequests().add(request);
        System.out.println("Request sent.");
    }

    private static void viewMyCourses(Teacher teacher) {
        List<Course> courses = teacher.getTeachingCourses();
        if (courses.isEmpty()) {
            System.out.println("You are not teaching any courses.");
        } else {
            courses.forEach(System.out::println);
        }
    }

    private static void manageCourse(UniversityDatabase db, Teacher teacher) {
        String code = ConsoleUtils.ask("Course code: ");
        Course course = teacher.getTeachingCourses().stream()
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
        if (course == null) {
            System.out.println("You do not teach this course.");
            return;
        }

        System.out.println("Students in " + course.getName() + ":");
        List<Student> courseStudents = db.getUsers().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .filter(s -> s.getCourses().stream().anyMatch(c -> c.getCode().equalsIgnoreCase(code)))
                .toList();

        if (courseStudents.isEmpty()) {
            System.out.println("No students registered.");
        } else {
            courseStudents.forEach(s -> System.out.println(s.getId() + " | " + s.getName()));
        }
    }

    private static void viewStudents(UniversityDatabase db) {
        db.getUsers().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .forEach(s -> System.out.println(s.getId() + " | " + s.getName() + " | Year: " + s.getYear()));
    }

    private static void sendComplaint(UniversityDatabase db, Teacher teacher) {
        String studentId = ConsoleUtils.ask("Student id: ");
        Student student = db.getUsers().stream()
                .filter(u -> u instanceof Student && u.getId().equals(studentId))
                .map(u -> (Student) u)
                .findFirst()
                .orElse(null);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        String content = ConsoleUtils.ask("Complaint content: ");
        // Urgency is requested in some Complaint models, let's check Complaint.java
        Complaint complaint = new Complaint(teacher.getId(), studentId, content);
        db.getComplaints().add(complaint);
        System.out.println("Complaint sent.");
    }

    private static void putMark(UniversityDatabase db, Teacher teacher) {
        String studentId = ConsoleUtils.ask("Student id: ");
        Student student = db.getUsers().stream()
                .filter(u -> u instanceof Student && u.getId().equals(studentId))
                .map(u -> (Student) u)
                .findFirst()
                .orElse(null);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        String courseCode = ConsoleUtils.ask("Course code: ");
        Course course = db.findCourseByCode(courseCode);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        try {
            ensureTeacherTeachesCourse(teacher, courseCode);
        } catch (CourseNotTaughtException e) {
            System.out.println(e.getMessage());
            return;
        }
        double a1 = Double.parseDouble(ConsoleUtils.ask("Attestation 1 (0..30): "));
        double a2 = Double.parseDouble(ConsoleUtils.ask("Attestation 2 (0..30): "));
        double fin = Double.parseDouble(ConsoleUtils.ask("Final (0..40): "));
        student.putMark(course.getCode(), new Mark(a1, a2, fin));
        System.out.println("Mark saved.");
    }

    private static void ensureTeacherTeachesCourse(Teacher teacher, String courseCode) throws CourseNotTaughtException {
        if (!teacher.teachesCourse(courseCode)) {
            throw new CourseNotTaughtException("Cannot put mark: you do not teach this course.");
        }
    }

    private static void addPaper(Teacher teacher) {
        if (!teacher.isResearcher()) {
            System.out.println("This teacher is not a researcher.");
            return;
        }
        String title = ConsoleUtils.ask("Paper title: ");
        int citations = ConsoleUtils.askInt("Citations: ");
        int pages = ConsoleUtils.askInt("Pages: ");
        teacher.addResearchPaper(new ResearchPaper(title, teacher.getName(), LocalDate.now(), citations, pages));
        System.out.println("Paper added.");
    }

    private static void showSortedPapers(Teacher teacher) {
        List<ResearchPaper> papers = teacher.getResearchPapers();
        System.out.println("By date:");
        ResearchService.sortByDate(papers).forEach(System.out::println);
        System.out.println("By citations:");
        ResearchService.sortByCitations(papers).forEach(System.out::println);
        System.out.println("By pages:");
        ResearchService.sortByPages(papers).forEach(System.out::println);
    }

    private static void showMessages(UniversityDatabase db, Teacher teacher) {
        List<Message> messages = db.getMessagesWithUser(teacher.getId());
        if (messages.isEmpty()) {
            System.out.println("No messages found.");
        } else {
            messages.forEach(System.out::println);
        }
    }

    private static void sendMessage(UniversityDatabase db, Teacher teacher) {
        String toId = ConsoleUtils.ask("To (user id): ");
        String content = ConsoleUtils.ask("Message content: ");
        Message message = new Message(teacher.getId(), toId, content);
        db.getMessages().add(message);
        System.out.println("Message sent.");
    }
}
