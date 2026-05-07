package university.patterns;

import university.interfaces.Researcher;
import university.models.ResearchPaper;
import university.models.User;

import java.io.Serializable;
import java.util.*;

public class ResearcherDecorator implements Researcher, Serializable {
    private static final long serialVersionUID = 1L;

    private final User wrappedUser;
    private final List<ResearchPaper> researchPapers = new ArrayList<>();
    private int hIndex = 0;

    public ResearcherDecorator(User wrappedUser) {
        this.wrappedUser = wrappedUser;
    }

    @Override
    public int getHIndex() {
        return hIndex;
    }

    public User getWrappedUser() {
        return wrappedUser;
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        researchPapers.add(paper);
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return new ArrayList<>(researchPapers);
    }

    @Override
    public void PrintPapers(Comparator<ResearchPaper> comparator) {
        researchPapers.sort(comparator);
        for (ResearchPaper paper : researchPapers) {
            System.out.println(paper);
        }
    }

    @Override
    public String toString() {
        return "[Researcher] " + wrappedUser.getName() + " (H-Index: " + hIndex + ")";
    }
}
