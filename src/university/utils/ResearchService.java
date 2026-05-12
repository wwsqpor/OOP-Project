package university.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import university.exceptions.NotResearcherException;
import university.interfaces.Researcher;
import university.models.ResearchPaper;
import university.models.ResearchProject;
import university.models.User;

public final class ResearchService {
    private ResearchService() {
    }

    public static List<ResearchPaper> sortByDate(List<ResearchPaper> papers) {
        List<ResearchPaper> copy = new ArrayList<>(papers);
        copy.sort(Comparator.comparing(ResearchPaper::getPublishedAt).reversed());
        return copy;
    }

    public static List<ResearchPaper> sortByCitations(List<ResearchPaper> papers) {
        List<ResearchPaper> copy = new ArrayList<>(papers);
        copy.sort(Comparator.comparingInt(ResearchPaper::getCitations).reversed());
        return copy;
    }

    public static List<ResearchPaper> sortByPages(List<ResearchPaper> papers) {
        List<ResearchPaper> copy = new ArrayList<>(papers);
        copy.sort(Comparator.comparingInt(ResearchPaper::getPages).reversed());
        return copy;
    }

    public static void printAllUniversityPapers(UniversityDatabase db, Comparator<ResearchPaper> comparator) {
        List<ResearchPaper> allPapers = new ArrayList<>();
        for (User user : db.getUsers()) {
            if (user instanceof Researcher researcher) {
                allPapers.addAll(researcher.getResearchPapers());
            }
        }
        // Remove duplicates if any
        Set<ResearchPaper> uniquePapers = new HashSet<>(allPapers);
        List<ResearchPaper> sortedPapers = new ArrayList<>(uniquePapers);
        sortedPapers.sort(comparator);

        System.out.println("=== All University Research Papers ===");
        for (ResearchPaper paper : sortedPapers) {
            System.out.println(paper);
        }
    }

    public static void joinProject(User user, ResearchProject project) throws NotResearcherException {
        project.addParticipant(user);
    }
}
