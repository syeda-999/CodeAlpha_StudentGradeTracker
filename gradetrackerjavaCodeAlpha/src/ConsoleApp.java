import java.util.Scanner;

/**
 * ConsoleApp.java  --  FRONT END #1 (console / terminal)
 *
 * This class ONLY talks to the user: it reads input with Scanner and
 * prints output with System.out. ALL the real work (storing students,
 * averaging, finding highest/lowest) is delegated to GradeManager.java.
 *
 * That separation is the whole point: swap this file for GuiApp.java and
 * the logic still works unchanged.
 */
public class ConsoleApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("        STUDENT GRADE TRACKER");
        System.out.println("========================================");

        // 1) Ask how many students -> this sizes the array
        int numStudents = readInt(sc, "\nHow many students do you want to enter? ", 1);

        // 2) Ask how many scores per student
        int numScores = readInt(sc, "How many scores (subjects) per student? ", 1);

        // 3) Hand those numbers to the BACKEND
        GradeManager manager = new GradeManager(numStudents);

        // 4) Loop and collect each student
        for (int i = 0; i < numStudents; i++) {
            System.out.println("\n--- Student " + (i + 1) + " of " + numStudents + " ---");

            System.out.print("Name: ");
            String name = sc.nextLine().trim();
            while (name.isEmpty()) {
                System.out.print("Name cannot be blank. Name: ");
                name = sc.nextLine().trim();
            }

            double[] scores = new double[numScores];
            for (int j = 0; j < numScores; j++) {
                scores[j] = readDouble(sc, "  Score " + (j + 1) + ": ");
            }

            // create the model object and store it via the backend
            manager.addStudent(new Student(name, scores));
        }

        // 5) Ask the BACKEND for the report and just print it
        System.out.println("\n\n================ SUMMARY REPORT ================");
        System.out.print(manager.buildReport());

        sc.close();
    }

    // ---------- small input helpers (part of the front end) ----------

    private static int readInt(Scanner sc, String prompt, int min) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v >= min) return v;
                System.out.println("Please enter a number >= " + min + ".");
            } catch (NumberFormatException e) {
                System.out.println("That is not a whole number. Try again.");
            }
        }
    }

    private static double readDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("  That is not a number. Try again.");
            }
        }
    }
}
