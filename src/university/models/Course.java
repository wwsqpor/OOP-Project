package university.models;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import university.enums.Faculty;

public class Course implements Serializable {
    private final String code;
    private final String name;
    private final int credits;
    private final Faculty targetFaculty;
    private final int targetYear;
    private final List<Teacher> instructors = new ArrayList<>();

    public Course(String code, String name, int credits, Faculty targetFaculty, int targetYear) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.targetFaculty = targetFaculty;
        this.targetYear = targetYear;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public Faculty getTargetFaculty() {
        return targetFaculty;
    }

    public int getTargetYear() {
        return targetYear;
    }

    public List<Teacher> getInstructors() {
        return instructors;
    }

    public void addInstructor(Teacher teacher) {
        if (teacher != null && !instructors.contains(teacher)) {
            instructors.add(teacher);
            teacher.addTeachingCourse(this);
        }
    }

    public void removeInstructor(Teacher teacher) {
        if (instructors.remove(teacher)) {
            teacher.removeTeachingCourse(this);
        }
    }

    @Override
    public String toString() {
        return code + " - " + name + " (" + credits + " cr)";
    }
}
