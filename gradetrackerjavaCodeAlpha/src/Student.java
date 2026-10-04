/**
 * Student.java  --  BACK END (data model / "the thing being managed")
 *
 * This is a plain data holder. It knows NOTHING about input, output,
 * console or GUI. That is what makes it reusable: the console front end
 * and the Swing GUI front end BOTH use this exact same class.
 */
public class Student {

    private String name;
    private double[] scores;   // scores for this one student (fixed size)

    /**
     * @param name   student's name
     * @param scores the scores entered for this student
     */
    public Student(String name, double[] scores) {
        this.name = name;
        this.scores = scores;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getScores() {
        return scores;
    }

    public void setScores(double[] scores) {
        this.scores = scores;
    }

    /** Average of this student's scores (0 if there are none). */
    public double getAverage() {
        if (scores == null || scores.length == 0) return 0;
        double total = 0;
        for (double s : scores) {
            total += s;
        }
        return total / scores.length;
    }

    /** Highest score for this student. */
    public double getHighest() {
        if (scores == null || scores.length == 0) return 0;
        double max = scores[0];
        for (double s : scores) {
            if (s > max) max = s;
        }
        return max;
    }

    /** Lowest score for this student. */
    public double getLowest() {
        if (scores == null || scores.length == 0) return 0;
        double min = scores[0];
        for (double s : scores) {
            if (s < min) min = s;
        }
        return min;
    }

    /** Letter grade based on the average. */
    public String getLetterGrade() {
        double avg = getAverage();
        if (avg >= 90) return "A";
        if (avg >= 80) return "B";
        if (avg >= 70) return "C";
        if (avg >= 60) return "D";
        return "F";
    }

    @Override
    public String toString() {
        return name + " (avg " + String.format("%.2f", getAverage()) + ")";
    }
}
