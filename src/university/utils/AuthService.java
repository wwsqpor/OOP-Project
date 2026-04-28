package university.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import university.exceptions.AuthenticationException;
import university.interfaces.Observable;
import university.interfaces.Observer;
import university.models.User;
import university.patterns.UniversityDatabase;

public class AuthService implements Observable {
    private final UniversityDatabase db;
    private final List<Observer> observers = new ArrayList<>();
    private User currentUser;

    public AuthService(UniversityDatabase db) {
        this.db = db;
    }

    public User login(String email, String password) throws AuthenticationException {
        Optional<User> userOpt = db.findUserByEmail(email);
        if (userOpt.isEmpty() || !userOpt.get().checkPassword(password)) {
            notifyObservers("FAILED_LOGIN: " + email);
            throw new AuthenticationException("Invalid email or password");
        }
        currentUser = userOpt.get();
        notifyObservers("LOGIN: " + currentUser.getEmail());
        return currentUser;
    }

    public void logout() {
        if (currentUser != null) {
            notifyObservers("LOGOUT: " + currentUser.getEmail());
        }
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(String event) {
        for (Observer observer : observers) {
            observer.onEvent(event);
        }
    }
}
