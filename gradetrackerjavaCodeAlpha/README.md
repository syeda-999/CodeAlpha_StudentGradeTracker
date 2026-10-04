# Student Grade Tracker (Java)

A console **and** GUI version of the Task 1 "Student Grade Tracker", built with a
clean **front end / back end split** so you can see exactly how the two sides talk
to each other.

- The user chooses **how many students** to enter → that number sizes the **array**.
- Students and their names are entered by the user.
- The program calculates **average, highest, and lowest** scores.
- A **summary report** of all students is displayed.

---

## 1. Front end vs. back end — the bit that confuses everyone

Think of a restaurant:

| Layer | Restaurant | Here | Files |
|-------|-----------|------|-------|
| **Back end** | Kitchen + recipe book | Stores students, does the math, builds the report | `Student.java`, `GradeManager.java` |
| **Front end** | Waiter taking your order | Reads keyboard/mouse input, prints/shows results | `ConsoleApp.java` *or* `GuiApp.java` |

**How the connection is made:** there is no network, no server, no database.
The connection is just a **normal Java method call**, because they are all in the
same program.

```
   FRONT END                                   BACK END
+------------------+        calls          +--------------------+
|  ConsoleApp.java |  -------------------> |  GradeManager.java |
|                  |   new GradeManager(n) |                    |
|  reads input     |   addStudent(...)     |  stores Student[]  |
|  prints output   |   buildReport()       |  computes averages |
+------------------+  <------------------  +--------------------+
        |
        |  creates
        v
+------------------+
|  Student.java    |  (data model: name + scores)
+------------------+
```

Same picture for `GuiApp.java` — it calls the **identical** back-end methods.
That is why `GradeManager.java` has **zero** `System.out.println` and **zero**
Swing popups in it: the back end must not care who is talking to it.

### The handshake, step by step

1. **Front end** asks the user "How many students?" → e.g. `3`
2. **Front end** calls the back end: `GradeManager manager = new GradeManager(3);`
   → the back end creates `new Student[3]` (the array sized by the user).
3. **Front end** loops, reads a name + scores, and calls
   `manager.addStudent(new Student(name, scores));`
4. **Front end** asks the back end for the finished report:
   `manager.buildReport()`
5. **Front end** displays that string (console prints it / GUI shows a table).

The back end never asks the user anything. The front end never does the math.

---

## 2. Files

```
gradetracker/
├── src/
│   ├── Student.java        BACK END  # data model (name + double[] scores + getAverage/Highest/Lowest)
│   ├── GradeManager.java   BACK END  # the user-sized Student[] array + all statistics + buildReport()
│   ├── ConsoleApp.java     FRONT END #1  console/terminal version
│   └── GuiApp.java         FRONT END #2  Swing window version (uses the SAME back end)
├── .vscode/                VS Code run/debug buttons
├── run-console.sh
├── run-gui.sh
└── README.md
```

**The key proof the split is real:** delete `ConsoleApp.java` and `GuiApp.java` will
still compile and run. Delete `GradeManager.java` and nothing runs.

---

## 3. Open it in Visual Studio Code (the blue one)

1. Install the **Extension Pack for Java** (by Microsoft) in VS Code.
2. `File > Open Folder...` → select the `gradetracker` folder.
3. Wait for the status bar to finish saying *"Java: Ready"*.

### Run the console version
- Open `src/ConsoleApp.java`.
- Click the small **▷ Run** link above `public static void main(String[] args)`, **or**
  press `F5` and choose **Console App**.
- Type the answers in the **Terminal** at the bottom, press Enter after each.

### Run the GUI version
- Open `src/GuiApp.java` → click **▷ Run** above `main` (or `F5` → **GUI App**).
- Two small pop-ups ask the counts; then a window collects each student; finally
  a table shows the report.

### No-fuss terminal alternative
```bash
./run-console.sh     # builds to out/ then runs the console app
./run-gui.sh         # builds to out/ then launches the window
```

Manual equivalent:
```bash
javac -d out src/*.java     # compile everything
java -cp out ConsoleApp     # run console version
java -cp out GuiApp         # run GUI version
```

---

## 4. Sample session (console)

```
How many students do you want to enter? 3
How many scores (subjects) per student? 3

--- Student 1 of 3 ---
Name: Alice
  Score 1: 88
  Score 2: 92
  Score 3: 79
... (Bob, Carol) ...

================ SUMMARY REPORT ================
--------------------------------------------------------------
STUDENT            AVERAGE    HIGHEST  LOWEST   SCORES   GRADE
--------------------------------------------------------------
Alice              86.33      92.00    79.00    3        B
Bob                74.67      81.00    68.00    3        C
Carol              95.00      99.00    91.00    3        A
--------------------------------------------------------------
Students entered : 3 of 3
Class average    : 85.33
Class highest    : 99.00
Class lowest     : 68.00
Top student      : Carol (95.00)
--------------------------------------------------------------
```

---

## 5. Where the "array sized by the user" actually lives

`GradeManager.java`:

```java
public GradeManager(int capacity) {
    this.students = new Student[capacity];   // <-- user's number becomes the array size
    this.count = 0;
}
```

`count` tracks how many slots are filled, so the array can stay fixed-size while
you still know how many real students exist (`getStudentsEntered()` copies out only
the filled slots). If you want the array to grow beyond what the user typed, swap
`Student[]` for `ArrayList<Student>` and use `students.add(...)` — every other
method stays the same.

---

## 6. Marking rubric coverage

| Requirement | Where |
|---|---|
| Input & manage student grades | `ConsoleApp` / `GuiApp` + `GradeManager.addStudent` |
| Average, highest, lowest | `Student` (per student) + `GradeManager` (class-wide) |
| Arrays / ArrayLists | `Student[]` inside `GradeManager` (sized by user) |
| Summary report | `GradeManager.buildReport()` |
| Console or GUI | Both provided |
