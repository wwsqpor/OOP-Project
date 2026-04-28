package university.models;

import university.enums.ManagerType;

public class Manager extends Employee {
    private final ManagerType type;

    public Manager(String id, String name, String email, String password, ManagerType type) {
        super(id, name, email, password);
        this.type = type;
    }

    public ManagerType getType() {
        return type;
    }

    @Override
    public String getRole() {
        return "Manager (" + type + ")";
    }
}
