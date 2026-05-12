package university.patterns;

import university.interfaces.Researcher;
import university.models.ResearchPaper;
import university.models.ResearchProject;
import university.models.User;

import java.io.Serializable;
import java.util.*;

public class ResearcherDecorator extends User implements Researcher, Serializable {
    private static final long serialVersionUID = 1L;

    private final User wrappedUser;
    private final List<ResearchPaper> researchPapers = new ArrayList<>();
    private final List<ResearchProject> researchProjects = new ArrayList<>();

    public ResearcherDecorator(User wrappedUser) {
        super(wrappedUser.getId(), wrappedUser.getName(), wrappedUser.getEmail(), "********");
        this.wrappedUser = wrappedUser;
    }

    @Override
    public String getRole() {
        return wrappedUser.getRole() + " + Researcher";
    }

    @Override
    public boolean checkPassword(String rawPassword) {
        return wrappedUser.checkPassword(rawPassword);
    }

    @Override
    public void setPassword(String password) {
        wrappedUser.setPassword(password);
    }

    @Override
    public int getHIndex() {
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort((p1, p2) -> Integer.compare(p2.getCitations(), p1.getCitations()));
        int h = 0;
        for (int i = 0; i < sorted.size(); i++) {
            if (sorted.get(i).getCitations() >= i + 1) {
                h = i + 1;
            } else {
                break;
            }
        }
        return h;
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
        List<ResearchPaper> sorted = new ArrayList<>(researchPapers);
        sorted.sort(comparator);
        for (ResearchPaper paper : sorted) {
            System.out.println(paper);
        }
    }

    @Override
    public List<ResearchProject> getResearchProjects() {
        return researchProjects;
    }

    @Override
    public void addResearchProject(ResearchProject project) {
        researchProjects.add(project);
    }

    @Override
    public String toString() {
        return String.format("[Researcher Decorator] %s (H-Index: %d)", wrappedUser.getName(), getHIndex());
    }
}
