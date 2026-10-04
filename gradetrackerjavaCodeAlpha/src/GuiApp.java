import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * GuiApp.java  --  FRONT END #2 (Swing graphical window)
 *
 * NOTE: This file imports its data logic from NOTHING. It creates a
 * GradeManager and calls exactly the same methods ConsoleApp calls.
 * GuiApp.java would compile even if ConsoleApp.java were deleted.
 *
 * Flow: a couple of small dialog inputs set up the counts, then a form
 * with fields per student, then the report is shown in a JTable.
 */
public class GuiApp {

    private GradeManager manager;
    private int numStudents;
    private int numScores;
    private int current = 0;          // which student we are entering

    private JFrame frame;
    private JTextField nameField;
    private JTextField[] scoreFields;
    private JPanel scorePanel;
    private JLabel statusLabel;
    private JButton nextButton;

    public static void main(String[] args) {
        // Run the UI on the Swing event thread (best practice)
        SwingUtilities.invokeLater(() -> new GuiApp().start());
    }

    private void start() {
        // ---- Step 1: ask the two counts with simple dialogs ----
        numStudents = askInt("How many students do you want to enter?", 1);
        numScores   = askInt("How many scores (subjects) per student?", 1);

        // ---- Step 2: create the BACKEND, sized by the user ----
        manager = new GradeManager(numStudents);

        // ---- Step 3: build the window ----
        frame = new JFrame("Student Grade Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(460, 420);
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel header = new JLabel("Enter student details", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        root.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));

        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        JPanel nameRow = new JPanel(new BorderLayout(8, 0));
        nameRow.add(new JLabel("Name:"), BorderLayout.WEST);
        nameRow.add(nameField, BorderLayout.CENTER);
        form.add(nameRow);
        form.add(Box.createVerticalStrut(10));

        scorePanel = new JPanel(new GridLayout(numScores, 1, 5, 5));
        scoreFields = new JTextField[numScores];
        for (int i = 0; i < numScores; i++) {
            scoreFields[i] = new JTextField();
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.add(new JLabel("Score " + (i + 1) + ":"), BorderLayout.WEST);
            row.add(scoreFields[i], BorderLayout.CENTER);
            scorePanel.add(row);
        }
        form.add(scorePanel);

        root.add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(0, 8));
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(180, 30, 30));
        bottom.add(statusLabel, BorderLayout.NORTH);

        nextButton = new JButton("Add Student");
        nextButton.addActionListener(e -> onNext());
        bottom.add(nextButton, BorderLayout.SOUTH);

        root.add(bottom, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.setVisible(true);
        updateTitle();
    }

    /** Called when the user clicks "Add Student". */
    private void onNext() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            statusLabel.setText("Please enter a name.");
            return;
        }

        double[] scores = new double[numScores];
        for (int i = 0; i < numScores; i++) {
            String text = scoreFields[i].getText().trim();
            try {
                scores[i] = Double.parseDouble(text);
            } catch (NumberFormatException ex) {
                statusLabel.setText("Score " + (i + 1) + " is not a valid number.");
                return;
            }
        }

        // store through the BACKEND -- same call the console app makes
        manager.addStudent(new Student(name, scores));
        current++;

        // clear the form for the next student
        nameField.setText("");
        for (JTextField f : scoreFields) f.setText("");
        statusLabel.setText(" ");
        nameField.requestFocus();

        if (current >= numStudents) {
            showReport();
        } else {
            updateTitle();
        }
    }

    private void updateTitle() {
        frame.setTitle("Student Grade Tracker  -  Student " + (current + 1) + " of " + numStudents);
        nextButton.setText(current == numStudents - 1 ? "Add Student & Show Report" : "Add Student");
    }

    /** Show the backend's data in a table (front end only formats it). */
    private void showReport() {
        String[] columns = {"Student", "Average", "Highest", "Lowest", "Grade"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Student s : manager.getStudentsEntered()) {
            model.addRow(new Object[]{
                    s.getName(),
                    String.format("%.2f", s.getAverage()),
                    String.format("%.2f", s.getHighest()),
                    String.format("%.2f", s.getLowest()),
                    s.getLetterGrade()
            });
        }

        JTable table = new JTable(model);

        JTextArea summary = new JTextArea(
                "Students entered : " + manager.getCount() + " of " + manager.getCapacity() + "\n" +
                "Class average    : " + String.format("%.2f", manager.getClassAverage()) + "\n" +
                "Class highest    : " + String.format("%.2f", manager.getClassHighest()) + "\n" +
                "Class lowest     : " + String.format("%.2f", manager.getClassLowest()) + "\n" +
                "Top student      : " + manager.getTopStudentName() +
                        " (" + String.format("%.2f", manager.getTopStudentAverage()) + ")"
        );
        summary.setEditable(false);
        summary.setFont(new Font("Monospaced", Font.PLAIN, 13));
        summary.setBackground(new Color(245, 245, 245));
        summary.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(summary, BorderLayout.SOUTH);

        frame.setContentPane(panel);
        frame.setTitle("Summary Report");
        frame.revalidate();
        frame.repaint();
    }

    /** Tiny reusable integer dialog. */
    private int askInt(String message, int min) {
        while (true) {
            String input = JOptionPane.showInputDialog(null, message, "Setup",
                    JOptionPane.QUESTION_MESSAGE);
            if (input == null) {   // user closed the dialog
                System.exit(0);
            }
            try {
                int v = Integer.parseInt(input.trim());
                if (v >= min) return v;
                JOptionPane.showMessageDialog(null,
                        "Please enter a number >= " + min + ".", "Invalid",
                        JOptionPane.WARNING_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "That is not a whole number.", "Invalid",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
