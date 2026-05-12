package university.patterns;

import university.interfaces.Researcher;
import university.models.Course;
import university.models.Mark;
import university.models.ResearchPaper;
import university.models.Student;
import university.models.User;

import java.util.*;
import java.util.stream.Collectors;

public class ReportGenerator {

    public interface ReportStrategy {
        void generate(UniversityDatabase db);
    }

    /** Report: top cited researchers of the year */
    public static class TopResearchersReport implements ReportStrategy {
        private final int year;

        public TopResearchersReport(int year) {
            this.year = year;
        }

        @Override
        public void generate(UniversityDatabase db) {
            System.out.println("=== Top Cited Researchers (" + year + ") ===");

            Map<Researcher, Integer> citMap = new HashMap<>();

            for (User user : db.getUsers()) {
                if (user instanceof Researcher researcher) {
                    int citations = researcher.getResearchPapers().stream()
                            .filter(p -> p.getPublishedAt().getYear() == year)
                            .mapToInt(ResearchPaper::getCitations)
                            .sum();
                    citMap.put(researcher, citations);
                }
            }

            citMap.entrySet().stream()
                    .sorted(Map.Entry.<Researcher, Integer>comparingByValue().reversed())
                    .forEach(e -> System.out.printf("  %-30s Citations: %d%n",
                            e.getKey().toString(), e.getValue()));
        }
    }

    /** Report: academic performance per course */
    public static class AcademicPerformanceReport implements ReportStrategy {
        @Override
        public void generate(UniversityDatabase db) {
            System.out.println("=== Academic Performance Report ===");
            System.out.printf("%-40s %8s %8s %10s%n", "Course", "Students", "Avg Mark", "Pass Rate");
            System.out.println("-".repeat(70));

            for (Course course : db.getCourses()) {
                List<Mark> courseMarks = new ArrayList<>();
                for (User user : db.getUsers()) {
                    if (user instanceof Student student) {
                        Mark m = student.getMarks().get(course.getCode());
                        if (m != null) {
                            courseMarks.add(m);
                        }
                    }
                }

                if (courseMarks.isEmpty()) {
                    System.out.printf("%-40s %8d %8s %10s%n", course.getName(), 0, "N/A", "N/A");
                    continue;
                }

                double avg = courseMarks.stream().mapToDouble(Mark::total).average().orElse(0);
                long passed = courseMarks.stream().filter(m -> !m.isFail()).count();
                double passRate = (double) passed / courseMarks.size() * 100;

                System.out.printf("%-40s %8d %8.2f %9.1f%%%n", course.getName(), courseMarks.size(), avg, passRate);
            }
        }
    }

    // Context holder
    private ReportStrategy strategy;

    public ReportGenerator(ReportStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ReportStrategy strategy) {
        this.strategy = strategy;
    }

    public void run(UniversityDatabase db) {
        strategy.generate(db);
    }
}
