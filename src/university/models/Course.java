package university.models;

import java.io.Serializable;

public class Course implements Serializable {
    private final String code;
    private final String name;
    private final int credits;
    private Teacher teacher;

    public Course(String code, String name, int credits, Teacher teacher) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.teacher = teacher;
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

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return code + " - " + name + " (" + credits + " cr)";
    }
}
