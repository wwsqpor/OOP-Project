package university.demo;

import java.util.List;
import university.data.DataStorage;
import university.enums.DegreeType;
import university.enums.ManagerType;
import university.enums.TeacherTitle;
import university.exceptions.AuthenticationException;
import university.exceptions.LowHIndexException;
import university.models.Admin;
import university.models.Course;
import university.models.Manager;
import university.models.ResearchProject;
import university.models.ResearcherEmployee;
import university.models.Student;
import university.models.Teacher;
import university.models.User;
import university.patterns.UniversityDatabase;
import university.utils.AuthService;

import university.enums.Faculty;

public class Main {
    private static final String DATA_PATH = "university_data.ser";

    public static void main(String[] args) {
        UniversityDatabase db = UniversityDatabase.getInstance();
        loadIfPresent(db);
        seedIfEmpty(db);

        AuthService authService = new AuthService(db);
        authService.addObserver(db.getLogger());

        while (true) {
            System.out.println("\n=== University System ===");
            System.out.println("1. Login");
            System.out.println("2. View news");
            System.out.println("3. View research projects");
            System.out.println("0. Exit");
            int choice = ConsoleUtils.askInt("Choose: ");

            if (choice == 0) {
                DataStorage.save(db, DATA_PATH);
                System.out.println("Saved to " + DATA_PATH);
                return;
            }
            if (choice == 2) {
                db.getNews().forEach(System.out::println);
                continue;
            }
            if (choice == 3) {
                viewResearchProjects(db);
                continue;
            }
            if (choice != 1) {
                continue;
            }

            String email = ConsoleUtils.ask("Email: ");
            String password = ConsoleUtils.ask("Password: ");
            try {
                User user = authService.login(email, password);
                openRoleMenu(db, user);
                authService.logout();
            } catch (AuthenticationException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void loadIfPresent(UniversityDatabase db) {
        UniversityDatabase loaded = DataStorage.load(DATA_PATH);
        if (loaded == null || loaded.getUsers().isEmpty()) {
            return;
        }
        db.getUsers().clear();
        db.getUsers().addAll(loaded.getUsers());
        db.getCourses().clear();
        db.getCourses().addAll(loaded.getCourses());
        db.getNews().clear();
        db.getNews().addAll(loaded.getNews());
        db.getComplaints().clear();
        db.getComplaints().addAll(loaded.getComplaints());
        db.getRegistrationRequests().clear();
        db.getRegistrationRequests().addAll(loaded.getRegistrationRequests());
        db.getResearchProjects().clear();
        db.getResearchProjects().addAll(loaded.getResearchProjects());
        db.getEmployeeRequests().clear();
        db.getEmployeeRequests().addAll(loaded.getEmployeeRequests());
    }

    private static void openRoleMenu(UniversityDatabase db, User user) {
        if (user instanceof Admin) {
            AdminMenu.open(db);
        } else if (user instanceof Manager manager) {
            ManagerMenu.open(db, manager);
        } else if (user instanceof Teacher teacher) {
            TeacherMenu.open(db, teacher);
        } else if (user instanceof ResearcherEmployee researcherEmployee) {
            ResearcherMenu.open(db, researcherEmployee);
        } else if (user instanceof Student student) {
            StudentMenu.open(db, student);
        } else if (user instanceof Dean dean) {
            DeanMenu.open(db, dean);
        } else if (user instanceof Rector rector) {
            RectorMenu.open(db, rector);
        } else {
            System.out.println("No menu for role: " + user.getRole());
        }
    }

    private static void seedIfEmpty(UniversityDatabase db) {
        if (!db.getUsers().isEmpty()) {
            return;
        }

        Admin admin = new Admin("U1", "AdminName", "admin@uni.kz", "admin123");
        Manager manager = new Manager("U2", "ManagerName", "manager@uni.kz", "manager123", ManagerType.OR);
        Course cs101 = new Course("CS101", "Intro to Programming", 5, Faculty.IT, 1);
        Course cs201 = new Course("CS201", "Data Structures", 6, Faculty.IT, 2);
        Course cs301 = new Course("CS301", "Software Engineering", 6, Faculty.IT, 3);
        Teacher prof = new Teacher("U3", "ProfName", "beken@uni.kz", "beken123", TeacherTitle.PROFESSOR);
        Teacher lecturer = new Teacher("U4", "LecturerName", "pakita@uni.kz", "pakita123",
                TeacherTitle.SENIOR_LECTURER);
        
        // Setup course instructors manually since we changed constructors
        cs101.addInstructor(prof);
        cs301.addInstructor(prof);
        cs201.addInstructor(lecturer);

        Student s1 = new Student("U5", "Student1Name", "oleg@uni.kz", "oleg123", 1, DegreeType.BACHELOR);
        Student s2 = new Student("U6", "Student2Name", "dima@uni.kz", "dima123", 2, DegreeType.BACHELOR);
        Student s3 = new Student("U7", "Student3Name", "karim@uni.kz", "karim123", 3, DegreeType.BACHELOR);
        Student s4 = new Student("U8", "Student4Name", "tevos@uni.kz", "tevos123", 4, DegreeType.BACHELOR);
        ResearcherEmployee re = new ResearcherEmployee("U9", "ResearcherName", "norka@uni.kz", "norka123");

        try {
            s4.setSupervisor(prof);
        } catch (LowHIndexException e) {
            // This might happen if prof has no papers yet
            System.err.println("Warning: Seed supervisor assignment: " + e.getMessage());
        }

        db.getUsers().addAll(List.of(admin, manager, prof, lecturer, s1, s2, s3, s4, re));
        db.getCourses().addAll(List.of(cs101, cs201, cs301));

        db.getResearchProjects().add(new ResearchProject("Why this semester sucks"));
    }

    private static void viewResearchProjects(UniversityDatabase db) {
        List<ResearchProject> projects = db.getResearchProjects();
        if (projects.isEmpty()) {
            System.out.println("No research projects available.");
            return;
        }

        System.out.println("\n--- Research Projects ---");
        for (ResearchProject project : projects) {
            System.out.println("\nProject: " + project.getTopic());
            List<Researcher> participants = project.getParticipants();
            if (participants.isEmpty()) {
                System.out.println("  Participants: None");
            } else {
                System.out.println("  Participants:");
                for (Researcher participant : participants) {
                    System.out.println("    - " + participant.toString());
                }
            }
        }
    }
}
