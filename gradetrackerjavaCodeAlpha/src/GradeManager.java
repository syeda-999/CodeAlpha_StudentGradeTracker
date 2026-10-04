/**
 * GradeManager.java  --  BACK END (the business logic / "the brain")
 *
 * Holds an ARRAY of Student objects whose size is decided by the user.
 * This class does no printing, no System.out, no Swing popups. It only
 * computes and stores. Both front ends call into this.
 */
public class GradeManager {

    private Student[] students;   // the array, size fixed by the user
    private int count;            // how many slots are actually filled

    /**
     * @param capacity how many students the user said they want to enter
     */
    public GradeManager(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.students = new Student[capacity];   // <-- array sized BY THE USER
        this.count = 0;
    }

    /** Add one student. Returns false if the array is already full. */
    public boolean addStudent(Student s) {
        if (count >= students.length) {
            return false;
        }
        students[count] = s;
        count++;
        return true;
    }

    public int getCount() {
        return count;
    }

    public int getCapacity() {
        return students.length;
    }

    public Student getStudent(int index) {
        if (index < 0 || index >= count) return null;
        return students[index];
    }

    /** All the students actually entered (no null slots). */
    public Student[] getStudentsEntered() {
        Student[] copy = new Student[count];
        System.arraycopy(students, 0, copy, 0, count);
        return copy;
    }

    // ---------- class-wide statistics ----------

    /** Average of every score of every student. */
    public double getClassAverage() {
        if (count == 0) return 0;
        double total = 0;
        int n = 0;
        for (int i = 0; i < count; i++) {
            for (double s : students[i].getScores()) {
                total += s;
                n++;
            }
        }
        return n == 0 ? 0 : total / n;
    }

    /** Highest single score in the whole class. */
    public double getClassHighest() {
        if (count == 0) return 0;
        double max = students[0].getHighest();
        for (int i = 1; i < count; i++) {
            if (students[i].getHighest() > max) {
                max = students[i].getHighest();
            }
        }
        return max;
    }

    /** Lowest single score in the whole class. */
    public double getClassLowest() {
        if (count == 0) return 0;
        double min = students[0].getLowest();
        for (int i = 1; i < count; i++) {
            if (students[i].getLowest() < min) {
                min = students[i].getLowest();
            }
        }
        return min;
    }

    /** Name of the student with the best average. */
    public String getTopStudentName() {
        if (count == 0) return "-";
        Student best = students[0];
        for (int i = 1; i < count; i++) {
            if (students[i].getAverage() > best.getAverage()) {
                best = students[i];
            }
        }
        return best.getName();
    }

    public double getTopStudentAverage() {
        if (count == 0) return 0;
        double best = students[0].getAverage();
        for (int i = 1; i < count; i++) {
            if (students[i].getAverage() > best) {
                best = students[i].getAverage();
            }
        }
        return best;
    }

    /**
     * Builds the summary report as a String.
     * The front ends just display whatever this returns -- the backend
     * decides WHAT the report says, the front end decides HOW it looks.
     */
    public String buildReport() {
        StringBuilder sb = new StringBuilder();
        String line = "--------------------------------------------------------------";
        sb.append(line).append("\n");
        sb.append(String.format("%-18s %-10s %-8s %-8s %-8s %-5s%n",
                "STUDENT", "AVERAGE", "HIGHEST", "LOWEST", "SCORES", "GRADE"));
        sb.append(line).append("\n");

        for (int i = 0; i < count; i++) {
            Student s = students[i];
            sb.append(String.format("%-18s %-10.2f %-8.2f %-8.2f %-8d %-5s%n",
                    s.getName(),
                    s.getAverage(),
                    s.getHighest(),
                    s.getLowest(),
                    s.getScores().length,
                    s.getLetterGrade()));
        }

        sb.append(line).append("\n");
        sb.append(String.format("Students entered : %d of %d%n", count, students.length));
        sb.append(String.format("Class average    : %.2f%n", getClassAverage()));
        sb.append(String.format("Class highest    : %.2f%n", getClassHighest()));
        sb.append(String.format("Class lowest     : %.2f%n", getClassLowest()));
        if (count > 0) {
            sb.append(String.format("Top student      : %s (%.2f)%n",
                    getTopStudentName(), getTopStudentAverage()));
        }
        sb.append(line).append("\n");
        return sb.toString();
    }
}
