package university.patterns;

import java.util.*;

public class ReportGenerator {

    public interface ReportStrategy {
        void generate(UniversityDatabase db);
    }

    /** Report: top cited researchers of the year */
    public static class TopResearchersReport implements ReportStrategy {
        private final int year;
        private final int topN;

        public TopResearchersReport(int year, int topN) {
            this.year = year;
            this.topN = topN;
        }

        @Override
        public void generate(UniversityDatabase db) {
            System.out.println("=== Top " + topN + " Cited Researchers (" + year + ") ===");
            // collect researchers and their total citations this year
            Map<String, Integer> citMap = new LinkedHashMap<>();
            Map<String, String> nameMap = new LinkedHashMap<>();

            citMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(topN)
                    .forEach(e -> System.out.printf("  %-30s Citations: %d%n",
                            nameMap.get(e.getKey()), e.getValue()));
        }
    }

    /** Report: academic performance per course */
    public static class AcademicPerformanceReport implements ReportStrategy {
        @Override
        public void generate(UniversityDatabase db) {
            System.out.println("=== Academic Performance Report ===");
            System.out.printf("%-40s %8s %8s %10s%n", "Course", "Students", "Avg Mark", "Pass Rate");
            System.out.println("-".repeat(70));

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
