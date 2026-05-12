package university.models;

import java.io.Serializable;
import university.enums.LessonType;
import university.enums.WeekDay;
import university.enums.RoomType;

public class Lesson implements Serializable {
    private final Course course;
    private final LessonType type;
    private final String topic;
    private final WeekDay day;
    private final int timeSlot; // e.g., 1-7
    private final Room room;

    public Lesson(Course course, LessonType type, String topic, WeekDay day, int timeSlot, Room room) {
        this.course = course;
        this.type = type;
        this.topic = topic;
        this.day = day;
        this.timeSlot = timeSlot;
        this.room = room;
        validateRoomType();
    }

    private void validateRoomType() {
        if (type == LessonType.LECTURE && room.getType() != RoomType.LECTURE_HALL) {
            System.err.println("Warning: Lecture assigned to non-lecture hall " + room.getId());
        }
        if (type == LessonType.LAB && room.getType() != RoomType.COMPUTER_LAB) {
            System.err.println("Warning: Lab assigned to non-computer lab " + room.getId());
        }
    }

    public Course getCourse() { return course; }
    public LessonType getType() { return type; }
    public String getTopic() { return topic; }
    public WeekDay getDay() { return day; }
    public int getTimeSlot() { return timeSlot; }
    public Room getRoom() { return room; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) at Slot %d in %s", day, course.getName(), type, timeSlot, room.getId());
    }
}
