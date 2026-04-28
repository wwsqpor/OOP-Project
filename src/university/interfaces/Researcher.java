package university.interfaces;

import java.util.Comparator;
import java.util.List;
import university.models.ResearchPaper;

public interface Researcher {
    int getHIndex();

    List<ResearchPaper> getResearchPapers();

    void addResearchPaper(ResearchPaper paper);

    void PrintPapers(Comparator<ResearchPaper> c);
}
