package university.models;

import java.io.Serializable;
import university.enums.RequestStatus;

public class EmployeeRequest implements Serializable {
    private final String employeeId;
    private final String content;
    private RequestStatus status = RequestStatus.PENDING;
    private boolean signedByDean = false;
    private boolean signedByRector = false;

    public EmployeeRequest(String employeeId, String content) {
        this.employeeId = employeeId;
        this.content = content;
    }

    public String getEmployeeId() { return employeeId; }
    public String getContent() { return content; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public boolean isSignedByDean() { return signedByDean; }
    public void setSignedByDean(boolean signedByDean) { this.signedByDean = signedByDean; }

    public boolean isSignedByRector() { return signedByRector; }
    public void setSignedByRector(boolean signedByRector) { this.signedByRector = signedByRector; }

    @Override
    public String toString() {
        return String.format("Request from %s: %s [Status: %s, Dean: %s, Rector: %s]",
                employeeId, content, status, signedByDean ? "Signed" : "Wait", signedByRector ? "Signed" : "Wait");
    }
}
