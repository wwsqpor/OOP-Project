package university.models;

import java.util.ArrayList;
import java.util.List;
import university.interfaces.Researcher;

public class ResearcherEmployee extends Employee implements Researcher {
    private int hIndex;
    private final List<ResearchPaper> papers = new ArrayList<>();

    public ResearcherEmployee(String id, String name, String email, String password, int hIndex) {
        super(id, name, email, password);
        this.hIndex = hIndex;
    }

    @Override
    public int getHIndex() {
        return hIndex;
    }

    public void setHIndex(int hIndex) {
        this.hIndex = Math.max(0, hIndex);
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
    public String getRole() {
        return "ResearcherEmployee";
    }
}
