package university.demo;

import java.time.LocalDate;
import java.util.List;
import university.exceptions.NotResearcherException;
import university.models.ResearchPaper;
import university.models.ResearchProject;
import university.models.ResearcherEmployee;
import university.patterns.UniversityDatabase;
import university.utils.ResearchService;

public final class ResearcherMenu {
    private ResearcherMenu() {
    }

    public static void open(UniversityDatabase db, ResearcherEmployee researcher) {
        while (true) {
            System.out.println("\n--- Researcher Menu ---");
            System.out.println("1. Create research paper");
            System.out.println("2. View research papers");
            System.out.println("3. Create research project");
            System.out.println("4. Join research project");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");

            if (choice == 0) {
                return;
            }
            if (choice == 1) {
                createResearchPaper(researcher);
            } else if (choice == 2) {
                viewResearchPapers(researcher);
            } else if (choice == 3) {
                createResearchProject(db, researcher);
            } else if (choice == 4) {
                joinProject(db, researcher);
            }
        }
    }

    private static void createResearchPaper(ResearcherEmployee researcher) {
        String title = ConsoleUtils.ask("Paper title: ");
        int citations = ConsoleUtils.askInt("Citations: ");
        int pages = ConsoleUtils.askInt("Pages: ");
        researcher.addResearchPaper(new ResearchPaper(title, researcher.getName(), LocalDate.now(), citations, pages));
        System.out.println("Research paper created.");
    }

    private static void viewResearchPapers(ResearcherEmployee researcher) {
        List<ResearchPaper> papers = researcher.getResearchPapers();
        if (papers.isEmpty()) {
            System.out.println("No research papers.");
            return;
        }

        System.out.println("By date:");
        ResearchService.sortByDate(papers).forEach(System.out::println);
        System.out.println("By citations:");
        ResearchService.sortByCitations(papers).forEach(System.out::println);
        System.out.println("By pages:");
        ResearchService.sortByPages(papers).forEach(System.out::println);
    }

    private static void createResearchProject(UniversityDatabase db, ResearcherEmployee researcher) {
        String name = ConsoleUtils.ask("Project name: ");
        if (name.isBlank()) {
            System.out.println("Project name cannot be empty.");
            return;
        }

        ResearchProject project = new ResearchProject(name);
        project.addMember(researcher.getId());
        db.getResearchProjects().add(project);
        System.out.println("Research project created.");
    }

    private static void joinProject(UniversityDatabase db, ResearcherEmployee researcher) {
        if (db.getResearchProjects().isEmpty()) {
            System.out.println("No research projects available.");
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
            ResearchService.joinProject(researcher, project);
            System.out.println("Joined project.");
        } catch (NotResearcherException e) {
            System.out.println(e.getMessage());
        }
    }
}
