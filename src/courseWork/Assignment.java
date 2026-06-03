/*
Name: Malaravan.V
Date: June 1st 2026
Purpose: Represents an assignment with its details
*/

// Libraries and packages
package courseWork;
import java.time.LocalDate;
public class Assignment{

    // Attributes of the Assignment class
    private String assignmentName;

    private String courseName;

    private int difficulty;

    private double gradeGoal;

    private double hoursSpent;

    private double gradeReceived;

    private LocalDate dueDate;
    private LocalDate startDate;

    private boolean completed;

    // Constructor to initialize an Assignment object with all attributes (Overloaded)
    public Assignment(String assignmentName, String courseName, int difficulty, double gradeGoal, double hoursSpent, LocalDate dueDate, LocalDate startDate, boolean completed, double gradeReceived) {
        this.assignmentName = assignmentName;
        
        this.courseName = courseName;

        this.difficulty = difficulty;

        this.gradeGoal = gradeGoal;

        this.hoursSpent = hoursSpent;

        this.dueDate = dueDate;

        this.startDate = startDate;

        this.completed = completed;

        this.gradeReceived = gradeReceived;
    }

    // Default constructor to initialize an Assignment object with default values (Overrided)
    public Assignment(){
        assignmentName = "";
        courseName = "";

        difficulty = 1;

        gradeGoal = 80.0;

        hoursSpent = 0.0;

        dueDate = LocalDate.now();
        startDate = LocalDate.now();

        completed = false;
    }

    // Getters and setters for all attributes of the Assignment class
    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
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

    public double getGradeReceived() {
        return gradeReceived;
    }

    // Returns assignment name and assignment tag to display in view work panels
    @Override
    public String toString() {
        return "[Assignment] " + assignmentName;
    }
}