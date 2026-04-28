package university.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResearchProject implements Serializable {
    private final String name;
    private final List<String> memberIds = new ArrayList<>();
    private final List<ResearchPaper> papers = new ArrayList<>();

    public ResearchProject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public List<ResearchPaper> getPapers() {
        return papers;
    }

    public void addMember(String userId) {
        if (!memberIds.contains(userId)) {
            memberIds.add(userId);
        }
    }

    public void addPaper(ResearchPaper paper) {
        papers.add(paper);
    }
}
