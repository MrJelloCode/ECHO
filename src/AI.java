/*
Name: Malaravan.V
Date: May 11th 2026
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

    public void addTest(Test test) {
        tests.add(test);
    }


    // Predicts hours needed to reach the student's grade goal.
    // Formula: hours = (w0 * difficulty) + (gradeGoal - 80) * w2 + avgHours * w1
    // Returns at least 0.5 hours, capped at 50 hours.
    public double predictHoursToReachGoal(Assignment assignment) {

        double baseHours     = weights[0] * assignment.getDifficulty();
        double ambitionBonus = (assignment.getGradeGoal() - 80.0) * weights[2];
        double historyBonus  = calculateAverageHours() * weights[1];

        double hoursNeeded = baseHours + ambitionBonus + historyBonus;

        hoursNeeded = Math.max(0.5, hoursNeeded);
        hoursNeeded = Math.min(50.0, hoursNeeded);

        return hoursNeeded;
    }

    public double predictHoursToReachGoal(Test test) {

        // If the test is cumulative, the effective difficulty is the average difficult of all tests in the same course, including this one, since the student needs to review material from every prior test as well
        double effectiveDifficulty;

        if (test.isCumulative()) {
            effectiveDifficulty = calculateCumulativeDifficulty(test);
        } else {
            effectiveDifficulty = test.getDifficulty();
        }

        double baseHours     = weights[0] * effectiveDifficulty;
        double ambitionBonus = (test.getGradeGoal() - 80.0) * weights[2];
        double historyBonus  = calculateAverageHours() * weights[1];

        double hoursNeeded = baseHours + ambitionBonus + historyBonus;

        hoursNeeded = Math.max(0.5, hoursNeeded);
        hoursNeeded = Math.min(50.0, hoursNeeded);

        return hoursNeeded;
    }

    // Calculates the average difficulty of all tests with the same course name.
    // Used when a test is marked cumulative — the student has to study everything covered in that course so far, not just the current test's material.
    private double calculateCumulativeDifficulty(Test currentTest) {

        double total = 0;
        int count = 0;

        for (Test t : tests) {

            if (t.getCourseName().equalsIgnoreCase(currentTest.getCourseName())) {

                total += t.getDifficulty();
                count++;
            }
        }

        // Fallback to the test's own difficulty if it's the only one in the course
        if (count == 0) {
            return currentTest.getDifficulty();
        }

        return currentTest.getDifficulty() + ((total / count) * 0.25);
    }

    // Methods for calculating procrastination and recommended start dates based on the model's predictions
    public int calculateProcrastinationDays(Assignment assignment) {

        double predictedHours = predictHoursToReachGoal(assignment);
        long daysNeeded = (long) Math.ceil(predictedHours / dailyCapacity);
        long daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), assignment.getDueDate());

        return (int)(daysUntilDue - daysNeeded);
    }

    // Similar to the above method but for tests instead of assignments
    public int calculateProcrastinationDays(Test test) {

        double predictedHours = predictHoursToReachGoal(test);
        long daysNeeded = (long) Math.ceil(predictedHours / dailyCapacity);
        long daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), test.getDueDate());

        return (int)(daysUntilDue - daysNeeded);
    }

    // Recommended start date is simply the due date minus the days needed to complete the work based on the model's predictions
    public LocalDate getRecommendedStartDate(Assignment assignment) {

        double predictedHours = predictHoursToReachGoal(assignment);
        long daysNeeded = (long) Math.ceil(predictedHours / dailyCapacity);

        return assignment.getDueDate().minusDays(daysNeeded);
    }

    // Similar to the above method but for tests instead of assignments
    public LocalDate getRecommendedStartDate(Test test) {

        double predictedHours = predictHoursToReachGoal(test);
        long daysNeeded = (long) Math.ceil(predictedHours / dailyCapacity);

        return test.getDueDate().minusDays(daysNeeded);
    }

    // Returns the average hours spent across all completed assignments AND tests. Falls back to a neutral 2.5 hours if no completed work exists yet.
    public double calculateAverageHours() {

        double total = 0;
        int count = 0;

        for (Assignment assignment : assignments) {

            if (assignment.isCompleted()) {

                total += assignment.getHoursSpent();
                count++;
            }
        }

        for (Test test : tests) {

            if (test.isCompleted()) {

                total += test.getHoursSpent();
                count++;
            }
        }

        if (count == 0) {

            return 2.5;
        }

        return total / count;
    }

    // Training method to adjust the model's weights based on the user's completed work and how well the model's predictions matched reality. Uses a simple form of gradient descent to minimize prediction errors over time as more data is collected.
    public void trainModel() {

        // Count all completed work — both assignments and tests
        int completedCount = getCompletedWorkCount();

        if (completedCount < 5) {

            return;
        }

        int newlyCompleted = completedCount - trainingCount;

        if (newlyCompleted <= 0) {

            return;
        }

        double totalDifficultyError  = 0;
        double totalHistoricalError  = 0;
        double totalGradeGoalError   = 0;

        // Build a combined list of all completed work to train on
        ArrayList<Object> allWork = new ArrayList<>();

        for (Assignment a : assignments) {
            if (a.isCompleted()) allWork.add(a);
        }

        for (Test t : tests) {
            if (t.isCompleted()) allWork.add(t);
        }

        int seen = 0;

        // Iterate backwards through completed work to find the most recent ones that haven't been used for training yet, and calculate the prediction errors for those items to adjust the model's weights accordingly. More recent items are more relevant for training since they reflect the user's current habits and performance, so we prioritize those when updating the model.
        for (int i = allWork.size() - 1; i >= 0 && seen < newlyCompleted; i--) {

            Object item = allWork.get(i);

            double predictedHours, hoursError, difficulty, gradeGoal;

            // Calculate the prediction error for this item based on its actual hours spent vs the model's predicted hours needed to reach the grade goal, and how that error relates to the item's difficulty, the user's historical average hours, and the grade goal itself. This error will be used to adjust the model's weights to improve future predictions.
            if (item instanceof Assignment) {
                Assignment a = (Assignment) item;
                predictedHours = predictHoursToReachGoal(a);
                hoursError = a.getHoursSpent() - predictedHours;
                difficulty = a.getDifficulty();
                gradeGoal = a.getGradeGoal();
            } else {
                Test t = (Test) item;
                predictedHours = predictHoursToReachGoal(t);
                hoursError = t.getHoursSpent() - predictedHours;
                difficulty = t.getDifficulty();
                gradeGoal = t.getGradeGoal();
            }

            totalDifficultyError += hoursError * difficulty;
            totalHistoricalError += hoursError * calculateAverageHours();
            totalGradeGoalError += hoursError * (gradeGoal - 80.0);

            seen++;
        }

        // Update weights based on the average error across all newly completed items since the last training session, multiplied by the learning rate to control how quickly the model adapts to new data.
        weights[0] += (totalDifficultyError / newlyCompleted) * learningRate;
        weights[1] += (totalHistoricalError / newlyCompleted) * learningRate;
        weights[2] += (totalGradeGoalError  / newlyCompleted) * learningRate;

        weights[0] = Math.max(0.5, Math.min(5.0, weights[0]));
        weights[1] = Math.max(0.0, Math.min(0.2, weights[1]));
        weights[2] = Math.max(0.0, Math.min(0.1, weights[2]));

        trainingCount = completedCount;
    }

    // Returns total completed count across both assignments and tests
    public int getCompletedWorkCount() {

        int count = 0;

        for (Assignment assignment : assignments) {

            if (assignment.isCompleted()) count++;
        }

        for (Test test : tests) {

            if (test.isCompleted()) count++;
        }

        return count;
    }
}