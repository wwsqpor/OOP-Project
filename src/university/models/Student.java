package university.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import university.enums.DegreeType;
import university.exceptions.CreditLimitExceededException;
import university.exceptions.FailLimitExceededException;
import university.exceptions.LowHIndexException;

public class Student extends User {
    private static final int MAX_CREDITS = 21;
    private static final int MAX_FAILS = 3;

    private final int year;
    private final DegreeType degreeType;
    private final List<Course> courses = new ArrayList<>();
    private final Map<String, Mark> marks = new HashMap<>();
    private Teacher supervisor;

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

    public Teacher getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Teacher supervisor) throws LowHIndexException {
        if (year >= 4 && (supervisor == null || !supervisor.isResearcher() || supervisor.getHIndex() < 3)) {
            throw new LowHIndexException("4th-year student needs researcher supervisor with h-index >= 3");
        }
        this.supervisor = supervisor;
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

    @Override
    public String getRole() {
        return "Student (year " + year + ")";
    }
}
