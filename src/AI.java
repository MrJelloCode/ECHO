/*
Name: Malaravan.V
Date: June 1st 2026
Purpose: AI class to manage the machine learning model for predicting optimal start times and workload management based on user data
*/


// Import Libraries
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import courseWork.*;

public class AI {
    // Attributes to store user data, model parameters, and training information
    private ArrayList<Assignment> assignments;
    private ArrayList<Test> tests;

    //Base Attributes
    private double[] weights;
    private double learningRate;
    private int trainingCount;
    private double dailyCapacity;

    //Default Contructor (Overrided)
    public AI(){
        assignments = new ArrayList<>();
        tests = new ArrayList<>();

        // weights[0] = difficulty influence on hours  (data-fitted: 1.03 hrs per difficulty level)
        // weights[1] = historicalAverage influence    (starts at 0, AI learns from your data)
        // weights[2] = gradeGoal influence            (starts at 0, AI learns from your data)
        weights = new double[]{1.03, 0.0, 0.0};

        learningRate = 0.01;
        trainingCount = 0;
        dailyCapacity = 2.5;

    }

    // Overloaded Constructor
    public AI(ArrayList<Assignment> assignments, ArrayList<Test> tests, double[] weights, double learningRate, int trainingCount, double dailyCapacity){
        this.assignments = assignments;
        this.tests = tests;
        this.weights = weights;
        this.learningRate = learningRate;
        this.trainingCount = trainingCount;
        this.dailyCapacity = dailyCapacity;
    }

    //Getters and Setters for all attributes

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(ArrayList<Assignment> assignments) {
        this.assignments = assignments;
    }

    public ArrayList<Test> getTests() {
        return tests;
    }

    public void setTests(ArrayList<Test> tests) {
        this.tests = tests;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public int getTrainingCount() {
        return trainingCount;
    }

    public void setTrainingCount(int trainingCount) {
        this.trainingCount = trainingCount;
    }

    public double getDailyCapacity() {
        return dailyCapacity;
    }

    public void setDailyCapacity(double dailyCapacity) {
        this.dailyCapacity = dailyCapacity;
    }

    // Methods to manage assignments and tests

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public void removeAssignment(Assignment assignment) {
        assignments.remove(assignment);
    }

    public void addTest(Test test) {
        tests.add(test);
    }

    public void removeTest(Test test) {
        tests.remove(test);
    }

    // Core prediction method: estimates hours needed to reach the grade goal based on assignment difficulty, historical average, and grade goal
    public double predictHoursToReachGoal(Assignment assignment) {

        double baseHours = weights[0] * assignment.getDifficulty();
        double ambitionBonus = (assignment.getGradeGoal() - 80.0) * weights[2];
        double historyBonus = calculateAverageHours() * weights[1];

        double hoursNeeded = baseHours + ambitionBonus + historyBonus;

        hoursNeeded = Math.max(0.5, hoursNeeded);
        hoursNeeded = Math.min(50.0, hoursNeeded);

        return hoursNeeded;
    }

    // Convert the hours into days until the assignment should be started, based on the predicted hours and the user's daily capacity.
    public int calculateProcrastinationDays(Assignment assignment) {

        double predictedHours = predictHoursToReachGoal(assignment);

        // Use ceil so a partial day still counts as a full day of work needed
        long daysNeeded = (long) Math.ceil(predictedHours / dailyCapacity);

        LocalDate today = LocalDate.now();

        long daysUntilDue = ChronoUnit.DAYS.between(today, assignment.getDueDate());

        return (int)(daysUntilDue - daysNeeded);
    }

    // Get the recommended start date by counting back from the due date based on the predicted hours and daily capacity
    public LocalDate getRecommendedStartDate(Assignment assignment) {

        double predictedHours = predictHoursToReachGoal(assignment);

        long daysNeeded = (long) Math.ceil(predictedHours / dailyCapacity);

        // Count back from the due date — this is the last safe day to start
        return assignment.getDueDate().minusDays(daysNeeded);
    }

    // Returns the average hours spent across all completed assignments. Falls back to a neutral 10.0 hours if no completed assignments exist yet.
    public double calculateAverageHours() {

        double total = 0;
        int count = 0;

        for (Assignment assignment : assignments) {

            if (assignment.isCompleted()) {

                total += assignment.getHoursSpent();
                count++;
            }
        }

        if (count == 0) {

            return 10.0;
        }

        return total / count;
    }

    //Train the model by adjusting weights based on the error between predicted hours and actual hours spent on completed assignments since the last training run. 
    public void trainModel() {

        int completedCount = 0;

        for (Assignment assignment : assignments) {

            if (assignment.isCompleted()) {

                completedCount++;
            }
        }

        // Can only start training once we have at least 2 completed assignment to compare against.
        if (completedCount < 2) {

            return;
        }

        // Only train on assignments completed since the last training run. That way early assignments don't have an outsized influence on the model as we gather more data.
        int newlyCompleted = completedCount - trainingCount;

        if (newlyCompleted <= 0) {

            return;
        }

        // Per-weight error accumulators: how much each input feature contributed
        // to the gap between predicted hours and actual hours spent
        double totalDifficultyError = 0;
        double totalHistoricalError = 0;
        double totalGradeGoalError = 0;

        int seen = 0;

        for (int i = assignments.size() - 1; i >= 0 && seen < newlyCompleted; i--) {

            Assignment assignment = assignments.get(i);

            if (assignment.isCompleted()) {

                // hoursError > 0 means we under-predicted (student needed more time)
                // hoursError < 0 means we over-predicted (student needed less time)
                double predictedHours = predictHoursToReachGoal(assignment);
                double hoursError = assignment.getHoursSpent() - predictedHours;

                // Nudge each weight in the direction that reduces future error.
                // w0 is driven by difficulty; w1/w2 are for averageHours/gradeGoal.
                totalDifficultyError += hoursError * assignment.getDifficulty();
                totalHistoricalError += hoursError * calculateAverageHours();
                totalGradeGoalError  += hoursError * (assignment.getGradeGoal() - 80.0);

                seen++;
            }
        }

        // Adapt weights — divide by newlyCompleted to get average gradient
        weights[0] += (totalDifficultyError / newlyCompleted) * learningRate;
        weights[1] += (totalHistoricalError / newlyCompleted) * learningRate;
        weights[2] += (totalGradeGoalError  / newlyCompleted) * learningRate;

        // Clamp weights to reasonable ranges
        weights[0] = Math.max(0.5, Math.min(5.0, weights[0])); // difficulty:        0.5–5.0 hrs per level
        weights[1] = Math.max(0.0, Math.min(0.2, weights[1])); // historicalAverage: 0.0–0.2 hrs per grade point
        weights[2] = Math.max(0.0, Math.min(0.1, weights[2])); // gradeGoal:         0.0–0.1 hrs per grade point

        trainingCount = completedCount;
    }

    // Method to count how many assignments have been completed. Used to determine when we have enough data to start training the model.
    public int getCompletedAssignmentCount() {

        int count = 0;

        for (Assignment assignment : assignments) {

            if (assignment.isCompleted()) {

                count++;
            }
        }

        return count;
    }
}