package university.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import university.interfaces.Researcher;

public class ResearcherEmployee extends Employee implements Researcher {
    private final List<ResearchPaper> papers = new ArrayList<>();
    private final List<ResearchProject> researchProjects = new ArrayList<>();

    public ResearcherEmployee(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public int getHIndex() {
        List<ResearchPaper> sorted = new ArrayList<>(papers);
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

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return papers;
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        papers.add(paper);
    }

    @Override
    public void PrintPapers(Comparator<ResearchPaper> c) {
        List<ResearchPaper> sortedPapers = new ArrayList<>(papers);
        sortedPapers.sort(c);
        for (ResearchPaper paper : sortedPapers) {
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
    public String getRole() {
        return "ResearcherEmployee";
    }
}
