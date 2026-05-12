package university.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import university.exceptions.NotResearcherException;
import university.interfaces.Researcher;

public class ResearchProject implements Serializable {
    private final String topic;
    private final List<Researcher> participants = new ArrayList<>();
    private final List<ResearchPaper> papers = new ArrayList<>();

    public ResearchProject(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public List<Researcher> getParticipants() {
        return participants;
    }

    public List<ResearchPaper> getPapers() {
        return papers;
    }

    public void addParticipant(Object person) throws NotResearcherException {
        if (!(person instanceof Researcher)) {
            throw new NotResearcherException("Person is not a Researcher and cannot join the project: " + person);
        }
        Researcher researcher = (Researcher) person;
        if (!participants.contains(researcher)) {
            participants.add(researcher);
            researcher.addResearchProject(this);
        }
    }

    public void addPaper(ResearchPaper paper) {
        if (!papers.contains(paper)) {
            papers.add(paper);
        }
    }
}
