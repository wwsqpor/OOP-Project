package university.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import university.enums.DegreeType;
import university.exceptions.CreditLimitExceededException;
import university.exceptions.FailLimitExceededException;
import university.exceptions.LowHIndexException;
import university.interfaces.Researcher;

public class Student extends User implements Researcher {
    private static final int MAX_CREDITS = 21;
    private static final int MAX_FAILS = 3;

    private final int year;
    private final DegreeType degreeType;
    private final List<Course> courses = new ArrayList<>();
    private final Map<String, Mark> marks = new HashMap<>();
    private Researcher supervisor;
    private boolean researcher;
    private int hIndex;
    private final List<ResearchPaper> researchPapers = new ArrayList<>();
    private final List<ResearchProject> researchProjects = new ArrayList<>();
    private double balance = 0.0;
    private boolean hasScholarship = true;

    public Student(String id, String name, String email, String password, int year, DegreeType degreeType) {
        super(id, name, email, password);
        this.year = year;
        this.degreeType = degreeType;
    }

    public int getYear() {
        return year;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public Map<String, Mark> getMarks() {
        return marks;
    }

    public Researcher getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Researcher supervisor) throws LowHIndexException {
        if (year >= 4) {
            if (supervisor == null) {
                throw new LowHIndexException("4th-year student needs a research supervisor");
            }
            if (supervisor.getHIndex() < 3) {
                throw new LowHIndexException("Supervisor h-index must be >= 3");
            }
        }
        this.supervisor = supervisor;
    }

    public boolean isResearcher() {
        return researcher;
    }

    public void setResearcher(boolean researcher) {
        this.researcher = researcher;
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

    @Override
    public List<ResearchPaper> getResearchPapers() {
        return researchPapers;
    }

    @Override
    public void addResearchPaper(ResearchPaper paper) {
        researchPapers.add(paper);
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
        researchProjects.add(project);
    }

    public void registerCourse(Course course) throws CreditLimitExceededException, FailLimitExceededException {
        if (countFailures() >= MAX_FAILS) {
            throw new FailLimitExceededException("Student reached fail limit of " + MAX_FAILS);
        }
        int totalCredits = courses.stream().mapToInt(Course::getCredits).sum();
        if (totalCredits + course.getCredits() > MAX_CREDITS) {
            throw new CreditLimitExceededException("Credit limit exceeded (max 21)");
        }
        courses.add(course);
    }

    public void putMark(String courseCode, Mark mark) {
        marks.put(courseCode, mark);
    }

    public int countFailures() {
        int failCount = 0;
        for (Mark mark : marks.values()) {
            if (mark.isFail()) {
                failCount++;
            }
        }
        return failCount;
    }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public boolean isHasScholarship() { return hasScholarship; }
    public void setHasScholarship(boolean hasScholarship) { this.hasScholarship = hasScholarship; }

    public void pay(double amount) throws Exception {
        if (balance < amount) throw new Exception("Insufficient funds.");
        balance -= amount;
    }

    @Override
    public String getRole() {
        return "Student (year " + year + ")";
    }
}
