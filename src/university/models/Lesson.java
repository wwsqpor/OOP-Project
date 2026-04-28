package university.models;

import java.io.Serializable;
import university.enums.LessonType;

public class Lesson implements Serializable {
    private final Course course;
    private final LessonType type;
    private final String topic;

    public Lesson(Course course, LessonType type, String topic) {
        this.course = course;
        this.type = type;
        this.topic = topic;
    }

    public Course getCourse() {
        return course;
    }

    public LessonType getType() {
        return type;
    }

    public String getTopic() {
        return topic;
    }
}
