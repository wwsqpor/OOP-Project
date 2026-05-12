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

import university.enums.Faculty;
import university.models.EmployeeRequest;
import university.models.Mark;
import university.models.NewsItem;
import university.patterns.ReportGenerator;

public final class ManagerMenu {
    private ManagerMenu() {
    }

    public static void open(UniversityDatabase db, Manager manager) {
        while (true) {
            System.out.println("\n--- Manager Menu ---");
            System.out.println("1. List users");
            System.out.println("2. Add course for registration");
            System.out.println("3. Assign course to teacher");
            System.out.println("4. Process registration requests");
            System.out.println("5. View student info (sorted)");
            System.out.println("6. View teacher info (sorted)");
            System.out.println("7. Manage news");
            System.out.println("8. Create statistical reports");
            System.out.println("9. View employee requests");
            System.out.println("10. Manage rooms");
            System.out.println("11. Create lesson (schedule)");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");
            if (choice == 0) {
                return;
            }
            switch (choice) {
                case 1 -> db.getUsers().forEach(u -> System.out.println(u.getId() + " | " + u.print()));
                case 2 -> addCourseForRegistration(db);
                case 3 -> assignCourseToTeacher(db);
                case 4 -> processRegistrationRequests(db);
                case 5 -> viewStudentInfo(db);
                case 6 -> viewTeacherInfo(db);
                case 7 -> manageNews(db);
                case 8 -> createReports(db);
                case 9 -> viewEmployeeRequests(db);
                case 10 -> manageRooms(db);
                case 11 -> createLesson(db);
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void manageRooms(UniversityDatabase db) {
        System.out.println("1. View rooms, 2. Add room");
        int choice = ConsoleUtils.askInt("Choose: ");
        if (choice == 1) {
            db.getRooms().forEach(System.out::println);
        } else if (choice == 2) {
            String id = ConsoleUtils.ask("Room ID: ");
            System.out.println("Type: 1. Lecture Hall, 2. Computer Lab, 3. Practice Room");
            int t = ConsoleUtils.askInt("Choice: ");
            university.enums.RoomType type = switch (t) {
                case 2 -> university.enums.RoomType.COMPUTER_LAB;
                case 3 -> university.enums.RoomType.PRACTICE_ROOM;
                default -> university.enums.RoomType.LECTURE_HALL;
            };
            int cap = ConsoleUtils.askInt("Capacity: ");
            db.getRooms().add(new university.models.Room(id, type, cap));
            System.out.println("Room added.");
        }
    }

    private static void createLesson(UniversityDatabase db) {
        String code = ConsoleUtils.ask("Course code: ");
        Course course = db.findCourseByCode(code);
        if (course == null) return;

        System.out.println("Type: 1. Lecture, 2. Practice, 3. Lab");
        int t = ConsoleUtils.askInt("Choice: ");
        university.enums.LessonType type = switch (t) {
            case 2 -> university.enums.LessonType.PRACTICE;
            case 3 -> university.enums.LessonType.LAB;
            default -> university.enums.LessonType.LECTURE;
        };

        String topic = ConsoleUtils.ask("Topic: ");
        System.out.println("Day: 1.Mon, 2.Tue, 3.Wed, 4.Thu, 5.Fri, 6.Sat");
        int d = ConsoleUtils.askInt("Choice: ");
        university.enums.WeekDay day = university.enums.WeekDay.values()[d - 1];
        int slot = ConsoleUtils.askInt("Time Slot (1-7): ");

        if (db.getRooms().isEmpty()) {
            System.out.println("No rooms available.");
            return;
        }
        for (int i = 0; i < db.getRooms().size(); i++) {
            System.out.println((i + 1) + ". " + db.getRooms().get(i));
        }
        int rIdx = ConsoleUtils.askInt("Room index: ") - 1;
        university.models.Room room = db.getRooms().get(rIdx);

        university.models.Lesson lesson = new university.models.Lesson(course, type, topic, day, slot, room);
        if (university.utils.ScheduleService.addLesson(db, lesson)) {
            System.out.println("Lesson scheduled.");
        }
    }

    private static void addCourseForRegistration(UniversityDatabase db) {
        String code = ConsoleUtils.ask("Course code: ");
        String name = ConsoleUtils.ask("Course name: ");
        int credits = ConsoleUtils.askInt("Credits: ");
        System.out.println("Target Faculty:");
        Faculty[] faculties = Faculty.values();
        for (int i = 0; i < faculties.length; i++) {
            System.out.println((i + 1) + ". " + faculties[i]);
        }
        int facultyIdx = ConsoleUtils.askInt("Choose faculty: ") - 1;
        Faculty targetFaculty = faculties[facultyIdx];
        int targetYear = ConsoleUtils.askInt("Target year (1-4): ");

        db.getCourses().add(new Course(code, name, credits, targetFaculty, targetYear));
        System.out.println("Course added for " + targetFaculty + " year " + targetYear);
    }

    private static void assignCourseToTeacher(UniversityDatabase db) {
        String courseCode = ConsoleUtils.ask("Course code: ");
        Course course = db.findCourseByCode(courseCode);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        String teacherId = ConsoleUtils.ask("Teacher ID: ");
        User user = db.findUserById(teacherId);
        if (user instanceof Teacher teacher) {
            course.addInstructor(teacher);
            System.out.println("Assigned " + teacher.getName() + " to " + course.getName());
        } else {
            System.out.println("Teacher not found.");
        }
    }

    private static void processRegistrationRequests(UniversityDatabase db) {
        if (db.getRegistrationRequests().isEmpty()) {
            System.out.println("No registration requests.");
            return;
        }
        for (RegistrationRequest request : db.getRegistrationRequests()) {
            if (request.getStatus() != RequestStatus.PENDING) {
                continue;
            }
            System.out.println("Request: " + request.getStudent().getName() + " -> " + request.getCourse().getName());
            int decision = ConsoleUtils.askInt("1 approve / 2 reject / 3 skip: ");
            if (decision == 1) {
                try {
                    request.getStudent().registerCourse(request.getCourse());
                    request.setStatus(RequestStatus.APPROVED);
                    System.out.println("Approved and registered.");
                } catch (Exception e) {
                    System.out.println("Approval failed: " + e.getMessage());
                    request.setStatus(RequestStatus.REJECTED);
                }
            } else if (decision == 2) {
                request.setStatus(RequestStatus.REJECTED);
                System.out.println("Rejected.");
            }
        }
    }

    private static void viewStudentInfo(UniversityDatabase db) {
        List<Student> students = db.getUsers().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .collect(java.util.stream.Collectors.toList());

        System.out.println("Sort by: 1. Alphabetical, 2. GPA/Total Score, 3. Year");
        int sortChoice = ConsoleUtils.askInt("Choose: ");
        switch (sortChoice) {
            case 1 -> students.sort(Comparator.comparing(User::getName));
            case 2 -> students.sort((s1, s2) -> {
                double gpa1 = s1.getMarks().values().stream().mapToDouble(Mark::total).average().orElse(0);
                double gpa2 = s2.getMarks().values().stream().mapToDouble(Mark::total).average().orElse(0);
                return Double.compare(gpa2, gpa1); // Descending
            });
            case 3 -> students.sort(Comparator.comparingInt(Student::getYear));
        }

        students.forEach(s -> {
            double gpa = s.getMarks().values().stream().mapToDouble(Mark::total).average().orElse(0);
            System.out.printf("%s | %s | Year: %d | Avg Score: %.2f%n", s.getId(), s.getName(), s.getYear(), gpa);
        });
    }

    private static void viewTeacherInfo(UniversityDatabase db) {
        List<Teacher> teachers = db.getUsers().stream()
                .filter(u -> u instanceof Teacher)
                .map(u -> (Teacher) u)
                .collect(java.util.stream.Collectors.toList());

        System.out.println("Sort by: 1. Alphabetical, 2. Rating");
        int sortChoice = ConsoleUtils.askInt("Choose: ");
        switch (sortChoice) {
            case 1 -> teachers.sort(Comparator.comparing(User::getName));
            case 2 -> teachers.sort(Comparator.comparingDouble(Teacher::getAverageRating).reversed());
        }

        teachers.forEach(t -> System.out.printf("%s | %s | Title: %s | Avg Rating: %.2f%n",
                t.getId(), t.getName(), t.getTitle(), t.getAverageRating()));
    }

    private static void manageNews(UniversityDatabase db) {
        System.out.println("1. View news, 2. Add news, 3. Remove news");
        int choice = ConsoleUtils.askInt("Choose: ");
        if (choice == 1) {
            db.getNews().forEach(System.out::println);
        } else if (choice == 2) {
            String title = ConsoleUtils.ask("Title: ");
            String content = ConsoleUtils.ask("Content: ");
            db.getNews().add(new NewsItem(title, content));
            System.out.println("News added.");
        } else if (choice == 3) {
            for (int i = 0; i < db.getNews().size(); i++) {
                System.out.println((i + 1) + ". " + db.getNews().get(i).getTitle());
            }
            int idx = ConsoleUtils.askInt("Index to remove: ") - 1;
            if (idx >= 0 && idx < db.getNews().size()) {
                db.getNews().remove(idx);
                System.out.println("News removed.");
            }
        }
    }

    private static void createReports(UniversityDatabase db) {
        System.out.println("1. Academic Performance, 2. Top Researchers");
        int choice = ConsoleUtils.askInt("Choose: ");
        ReportGenerator generator;
        if (choice == 1) {
            generator = new ReportGenerator(new ReportGenerator.AcademicPerformanceReport());
            generator.run(db);
        } else if (choice == 2) {
            int year = ConsoleUtils.askInt("Year: ");
            generator = new ReportGenerator(new ReportGenerator.TopResearchersReport(year));
            generator.run(db);
        }
    }

    private static void viewEmployeeRequests(UniversityDatabase db) {
        if (db.getEmployeeRequests().isEmpty()) {
            System.out.println("No employee requests.");
            return;
        }
        db.getEmployeeRequests().forEach(System.out::println);
        System.out.println("Only Dean or Rector can sign these. (This logic is usually in their menus)");
    }
}
