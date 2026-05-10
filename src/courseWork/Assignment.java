package courseWork;
import java.time.LocalDate;

public class Assignment{

    private String assignmentName;
    private String courseName;

    private int difficulty;

    private double hoursSpent;

    private double predictedGrade;

    private LocalDate dueDate;
    private LocalDate startDate;

    private boolean completed;

    public Assignment(String assignmentName, String courseName, int difficulty, double hoursSpent, double predictedGrade, LocalDate dueDate, LocalDate startDate, boolean completed){
        this.assignmentName = assignmentName;
        this.courseName = courseName;
        
        this.difficulty = difficulty;
        
        this.hoursSpent = hoursSpent;

        this.predictedGrade = predictedGrade;
        
        this.dueDate = dueDate;
        this.startDate = startDate;

        this.completed = completed;

    }

    public Assignment(){
        assignmentName = "";
        courseName = "";

        difficulty = 1;

        hoursSpent = 0.0;


        predictedGrade = 0.0;

        dueDate = LocalDate.now();
        startDate = LocalDate.now();

        completed = false;
    }

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


    public double getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(double hoursSpent) {
        this.hoursSpent = hoursSpent;
    }


    public double getPredictedGrade() {
        return predictedGrade;
    }

    public void setPredictedGrade(double predictedGrade) {
        this.predictedGrade = predictedGrade;
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
}

