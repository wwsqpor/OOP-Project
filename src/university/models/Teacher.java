package university.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import university.enums.TeacherTitle;
import university.interfaces.Researcher;

public class Teacher extends Employee implements Researcher {
    private final TeacherTitle title;
    private final boolean researcher;
    private final List<ResearchPaper> researchPapers = new ArrayList<>();
    private final List<ResearchProject> researchProjects = new ArrayList<>();
    private final List<Course> teachingCourses = new ArrayList<>();
    private final List<Integer> ratings = new ArrayList<>();

    public Teacher(String id, String name, String email, String password, TeacherTitle title) {
        this(id, name, email, password, title, new ArrayList<>());
    }

    public Teacher(String id, String name, String email, String password, TeacherTitle title,
            List<Course> teachingCourses) {
        super(id, name, email, password);
        this.title = title;
        this.researcher = title == TeacherTitle.PROFESSOR;
        if (teachingCourses != null) {
            for (Course course : teachingCourses) {
                if (course != null) {
                    course.addInstructor(this);
                }
            }
        }
    }

    public TeacherTitle getTitle() {
        return title;
    }

    public boolean isResearcher() {
        return researcher;
    }

    @Override
    public int getHIndex() {
        if (!researcher) return 0;
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
    public void PrintPapers(Comparator<ResearchPaper> c) {
        List<ResearchPaper> sortedPapers = new ArrayList<>(researchPapers);
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
        if (researcher) {
            researchProjects.add(project);
        }
    }

    @Override
    public String getRole() {
        return "Teacher (" + title + ")";
    }

    public List<Course> getTeachingCourses() {
        return teachingCourses;
    }

    public void addTeachingCourse(Course course) {
        if (course == null) {
            return;
        }
        boolean exists = teachingCourses.stream()
                .anyMatch(c -> c.getCode().equalsIgnoreCase(course.getCode()));
        if (!exists) {
            teachingCourses.add(course);
        }
    }

    public void removeTeachingCourse(Course course) {
        if (course == null) {
            return;
        }
        teachingCourses.removeIf(c -> c.getCode().equalsIgnoreCase(course.getCode()));
    }

    public boolean teachesCourse(String courseCode) {
        if (courseCode == null) {
            return false;
        }
        return teachingCourses.stream().anyMatch(c -> c.getCode().equalsIgnoreCase(courseCode));
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
}
