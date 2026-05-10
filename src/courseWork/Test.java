package courseWork;
import java.time.LocalDate;

public class Test {

    private String testName;

    private String courseName;

    private int difficulty;

    private int topicComplexity;

    private boolean cumulative;

    private double predictedGrade;

    private double hoursSpent;

    private int daysWorked;

    private LocalDate dueDate;

    private LocalDate startDate;

    private boolean completed;

    public Test() {

    }

    public Test(String testName,
                String courseName,
                int difficulty,
                int topicComplexity,
                boolean cumulative,
                LocalDate dueDate) {

        this.testName = testName;

        this.courseName = courseName;

        this.difficulty = difficulty;

        this.topicComplexity = topicComplexity;

        this.cumulative = cumulative;

        this.dueDate = dueDate;

        completed = false;
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

    public int getTopicComplexity() {
        return topicComplexity;
    }

    public void setTopicComplexity(int topicComplexity) {
        this.topicComplexity = topicComplexity;
    }

    public boolean isCumulative() {
        return cumulative;
    }

    public void setCumulative(boolean cumulative) {
        this.cumulative = cumulative;
    }

    public double getPredictedGrade() {
        return predictedGrade;
    }

    public void setPredictedGrade(double predictedGrade) {
        this.predictedGrade = predictedGrade;
    }

    public double getHoursSpent() {
        return hoursSpent;
    }

    public void setHoursSpent(double hoursSpent) {
        this.hoursSpent = hoursSpent;
    }

    public int getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(int daysWorked) {
        this.daysWorked = daysWorked;
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