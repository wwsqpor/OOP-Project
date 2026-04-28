package university.models;

import java.util.ArrayList;
import java.util.List;
import university.enums.TeacherTitle;
import university.interfaces.Researcher;

public class Teacher extends Employee implements Researcher {
    private final TeacherTitle title;
    private final boolean researcher;
    private int hIndex;
    private final List<ResearchPaper> researchPapers = new ArrayList<>();
    private final List<Integer> ratings = new ArrayList<>();

    public Teacher(String id, String name, String email, String password, TeacherTitle title, boolean researcher,
            int hIndex) {
        super(id, name, email, password);
        this.title = title;
        this.researcher = title == TeacherTitle.PROFESSOR || researcher;
        this.hIndex = this.researcher ? Math.max(hIndex, title == TeacherTitle.PROFESSOR ? 3 : 0) : 0;
    }

    public TeacherTitle getTitle() {
        return title;
    }

    public boolean isResearcher() {
        return researcher;
    }

    public void setHIndex(int hIndex) {
        if (researcher) {
            this.hIndex = Math.max(0, hIndex);
        }
    }

    public void addRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        ratings.add(rating);
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0;
        }
        int total = 0;
        for (int rating : ratings) {
            total += rating;
        }
        return (double) total / ratings.size();
    }

    public int getRatingsCount() {
        return ratings.size();
    }

    @Override
    public int getHIndex() {
        return hIndex;
    }

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return researchPapers;
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        if (researcher) {
            researchPapers.add(paper);
        }
    }

    @Override
    public String getRole() {
        return "Teacher (" + title + ")";
    }
}
