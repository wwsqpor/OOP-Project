package university.models;

public class Dean extends Employee {
    public Dean(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    public String getRole() { return "Dean"; }

    public void signRequest(EmployeeRequest request) {
        request.setSignedByDean(true);
    }
}
