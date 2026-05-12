package university.models;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    private final String code;
    private final String name;
    private final int credits;
    private final List<Teacher> instructors = new ArrayList<>();

    public Course(String code, String name, int credits, Teacher instructor) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        if (instructor != null) {
            addInstructor(instructor);
        }
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
