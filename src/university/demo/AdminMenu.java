package university.demo;

import university.models.NewsItem;
import university.patterns.UniversityDatabase;

public final class AdminMenu {
    private AdminMenu() {
    }

    public static void open(UniversityDatabase db) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add news");
            System.out.println("2. View logs");
            System.out.println("0. Back");
            int choice = ConsoleUtils.askInt("Choose: ");
            if (choice == 0) {
                return;
            }
            if (choice == 1) {
                String title = ConsoleUtils.ask("Title: ");
                String body = ConsoleUtils.ask("Body: ");
                db.getNews().add(new NewsItem(title, body));
                System.out.println("News added.");
            } else if (choice == 2) {
                db.getLogger().getLogs().forEach(System.out::println);
            }
        }
    }
}
