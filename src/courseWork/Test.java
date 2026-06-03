/*
Name: Malaravan.V
Date: May 11th 2026
Purpose: Represents a test with its details
*/
// Libraries and packages
package courseWork;
import java.time.LocalDate;

public class Test {
    // Attributes of the Test class
    private String testName;

    private String courseName;

    private int difficulty;

    private boolean cumulative;

    private double gradeGoal;

    private double hoursSpent;

    private double gradeReceived;

    private LocalDate dueDate;

    private LocalDate startDate;

    private boolean completed;

    // Default constructor
    public Test() {
        testName = "";
        courseName = "";
        difficulty = 1;
        cumulative = false;
        gradeGoal = 80.0;
        hoursSpent = 0.0;
        gradeReceived = 0.0;
        dueDate = LocalDate.now();
        startDate = LocalDate.now();
        completed = false;
    }

    // Overloaded constructor
    public Test(String testName,
                String courseName,
                int difficulty,
                boolean cumulative,
                double gradeGoal,
                LocalDate dueDate,
                LocalDate startDate,
                boolean completed,
                double gradeReceived) {

        this.testName = testName;
        this.courseName = courseName;
        this.difficulty = difficulty;
        this.cumulative = cumulative;
        this.gradeGoal  = gradeGoal;
        this.hoursSpent = hoursSpent;
        this.gradeReceived = gradeReceived;
        this.dueDate = dueDate;
        this.startDate = startDate;
        this.completed = completed;
    }

    // GETTERS & SETTERS

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

 

    public boolean isCumulative() {
        return cumulative;
    }

    public void setCumulative(boolean cumulative) {
        this.cumulative = cumulative;
    }

    public double getGradeGoal() {
        return gradeGoal;
    }

    public void setGradeGoal(double gradeGoal) {
        this.gradeGoal = gradeGoal;
    }

    public double getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(double hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

    public double getGradeReceived() {
        return gradeReceived;
    }

    public void setGradeReceived(double gradeReceived) {
        this.gradeReceived = gradeReceived;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // toString method to return the name of the test and the test tag to display in view work panels
    public String toString() {
        return "[Test] " + testName;
    }
}