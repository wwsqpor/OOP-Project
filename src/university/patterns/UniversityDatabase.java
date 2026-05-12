package university.patterns;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import university.models.*;

public class UniversityDatabase implements Serializable {
    private static final UniversityDatabase INSTANCE = new UniversityDatabase();

    private final List<User> users = new ArrayList<>();
    private final List<Course> courses = new ArrayList<>();
    private final List<NewsItem> news = new ArrayList<>();
    private final List<Complaint> complaints = new ArrayList<>();
    private final List<ResearchProject> researchProjects = new ArrayList<>();
    private final List<EmployeeRequest> employeeRequests = new ArrayList<>();
    private final List<Lesson> lessons = new ArrayList<>();
    private final List<Room> rooms = new ArrayList<>();
    private final SystemLogger logger = new SystemLogger();
    private final List<Message> messages = new ArrayList<>();

    private UniversityDatabase() {
    }

    public static UniversityDatabase getInstance() {
        return INSTANCE;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<NewsItem> getNews() {
        return news;
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public List<RegistrationRequest> getRegistrationRequests() {
        return registrationRequests;
    }

    public List<ResearchProject> getResearchProjects() {
        return researchProjects;
    }

    public List<EmployeeRequest> getEmployeeRequests() {
        return employeeRequests;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public SystemLogger getLogger() {
        return logger;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public Optional<User> findUserByEmail(String email) {
        return users.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public User findUserById(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public List<Message> getMessagesWithUser(String userId) {
        List<Message> userMessages = new ArrayList<>();
        for (Message message : messages) {
            if (message.getTo().equals(userId) || message.getFrom().equals(userId)) {
                userMessages.add(message);
            }
        }
        return userMessages;
    }

    public Course findCourseByCode(String code) {
        for (Course course : courses) {
            if (course.getCode().equalsIgnoreCase(code)) {
                return course;
            }
        }
        return null;
    }
}
