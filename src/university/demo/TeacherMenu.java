package university.demo;

import java.time.LocalDate;
import java.util.List;
import university.models.Mark;
import university.models.ResearchPaper;
import university.models.Student;
import university.models.Teacher;
import university.patterns.UniversityDatabase;
import university.utils.ResearchService;

public final class TeacherMenu {
    private TeacherMenu() {
    }

    public static void open(UniversityDatabase db, Teacher teacher) {
        while (true) {
            System.out.println("\n--- Teacher Menu ---");
            System.out.println("1. Put mark");
            System.out.println("2. Add research paper");
            System.out.println("3. View papers sorted by date/citations/pages");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");
            if (choice == 0) {
                return;
            }
            if (choice == 1) {
                putMark(db);
            } else if (choice == 2) {
                addPaper(teacher);
            } else if (choice == 3) {
                showSortedPapers(teacher);
            }
        }
    }

    private static void putMark(UniversityDatabase db) {
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
        double a1 = Double.parseDouble(ConsoleUtils.ask("Attestation 1 (0..30): "));
        double a2 = Double.parseDouble(ConsoleUtils.ask("Attestation 2 (0..30): "));
        double fin = Double.parseDouble(ConsoleUtils.ask("Final (0..40): "));
        student.putMark(courseCode, new Mark(a1, a2, fin));
        System.out.println("Mark saved.");
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
}
