package university.demo;

import java.util.Scanner;

public final class ConsoleUtils {
    public static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleUtils() {
    }

    public static String ask(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    public static int askInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(ask(prompt));
            } catch (NumberFormatException ignored) {
                System.out.println("Enter a valid number.");
            }
        }
    }

    public static void pause() {
        System.out.println("Press Enter to continue...");
        SCANNER.nextLine();
    }
}
