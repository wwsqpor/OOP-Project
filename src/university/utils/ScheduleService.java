package university.utils;

import java.util.List;
import java.util.stream.Collectors;
import university.models.*;
import university.patterns.UniversityDatabase;

public final class ScheduleService {
    private ScheduleService() {}

    public static boolean addLesson(UniversityDatabase db, Lesson lesson) {
        // Check for room conflict
        boolean conflict = db.getLessons().stream()
                .anyMatch(l -> l.getRoom().getId().equals(lesson.getRoom().getId()) &&
                               l.getDay() == lesson.getDay() &&
                               l.getTimeSlot() == lesson.getTimeSlot());
        
        if (conflict) {
            System.err.println("Room conflict detected for " + lesson.getRoom().getId());
            return false;
        }

        db.getLessons().add(lesson);
        return true;
    }

    public static void viewStudentSchedule(UniversityDatabase db, Student student) {
        System.out.println("=== Schedule for " + student.getName() + " ===");
        List<String> courseCodes = student.getCourses().stream().map(Course::getCode).collect(Collectors.toList());
        
        db.getLessons().stream()
                .filter(l -> courseCodes.contains(l.getCourse().getCode()))
                .sorted((l1, l2) -> {
                    int dayComp = l1.getDay().ordinal() - l2.getDay().ordinal();
                    if (dayComp != 0) return dayComp;
                    return l1.getTimeSlot() - l2.getTimeSlot();
                })
                .forEach(System.out::println);
    }

    public static void viewTeacherSchedule(UniversityDatabase db, Teacher teacher) {
        System.out.println("=== Schedule for " + teacher.getName() + " ===");
        List<String> courseCodes = teacher.getTeachingCourses().stream().map(Course::getCode).collect(Collectors.toList());

        db.getLessons().stream()
                .filter(l -> courseCodes.contains(l.getCourse().getCode()))
                .sorted((l1, l2) -> {
                    int dayComp = l1.getDay().ordinal() - l2.getDay().ordinal();
                    if (dayComp != 0) return dayComp;
                    return l1.getTimeSlot() - l2.getTimeSlot();
                })
                .forEach(System.out::println);
    }
}
