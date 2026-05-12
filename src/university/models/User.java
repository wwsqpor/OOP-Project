package university.models;

import java.io.Serializable;
import university.interfaces.Printable;

public abstract class User implements Serializable, Printable, Comparable<User> {
    private final String id;
    private final String name;
    private final String email;
    private String password;

    protected User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    
    public abstract String getRole();

    public boolean checkPassword(String rawPassword) {
        return password.equals(rawPassword);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int compareTo(User other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    @Override
    public String print() {
        return String.format("%s (%s)", name, getRole());
    }
}
