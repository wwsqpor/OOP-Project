package university.demo;

import university.models.Rector;
import university.models.EmployeeRequest;
import university.patterns.UniversityDatabase;

public final class RectorMenu {
    private RectorMenu() {}

    public static void open(UniversityDatabase db, Rector rector) {
        while (true) {
            System.out.println("\n--- Rector Menu ---");
            System.out.println("1. View/Sign employee requests");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");
            if (choice == 0) return;
            if (choice == 1) {
                signRequests(db, rector);
            }
        }
    }

    private static void signRequests(UniversityDatabase db, Rector rector) {
        if (db.getEmployeeRequests().isEmpty()) {
            System.out.println("No requests.");
            return;
        }
        for (int i = 0; i < db.getEmployeeRequests().size(); i++) {
            EmployeeRequest req = db.getEmployeeRequests().get(i);
            System.out.println((i + 1) + ". " + req);
            if (!req.isSignedByRector()) {
                int sign = ConsoleUtils.askInt("1 to sign / 2 to skip: ");
                if (sign == 1) rector.signRequest(req);
            }
        }
    }
}
