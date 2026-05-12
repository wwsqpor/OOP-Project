package university.models;

public class Rector extends Employee {
    public Rector(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public String getRole() { return "Rector"; }

    public void signRequest(EmployeeRequest request) {
        request.setSignedByRector(true);
    }
}
