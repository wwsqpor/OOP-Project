package university.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    public static void joinProject(User user, ResearchProject project) throws NotResearcherException {
        if (!(user instanceof Researcher)) {
            throw new NotResearcherException("Only researchers can join projects");
        }
        project.addMember(user.getId());
    }
}
