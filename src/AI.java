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
    private double baseLineGrade;

    //Default Contructor (Overrided)
    public AI(){
        assignments = new ArrayList<>();
        tests = new ArrayList<>();

        // weights[0] = difficulty influence on hours
        // weights[1] = historicalAverage influence on hours
        // weights[2] = gradeGoal influence on hours
        weights = new double[]{2.0, 0.05, 0.03};

        learningRate = 0.01;
        trainingCount = 0;
        dailyCapacity = 3.0;
        baseLineGrade  = 85.0;
    }

    // Overloaded Constructor to initialize the AI with specific data and parameters
    public AI(ArrayList<Assignment> assignments, ArrayList<Test> tests, double[] weights, double learningRate, int trainingCount, double dailyCapacity, double baseLineGrade){
        this.assignments = assignments;
        this.tests = tests;
        this.weights = weights;
        this.learningRate = learningRate;
        this.trainingCount = trainingCount;
        this.dailyCapacity = dailyCapacity;
        this.baseLineGrade = baseLineGrade;
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

    public double getBaselineGrade() {
        return baseLineGrade;
    }

    public void setBaselineGrade(double baselineGrade) {
        this.baseLineGrade = baselineGrade;
    }

    // Methods to manage assignments and tests, allowing for adding and removing items from the user's workload.

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

    // Estimates hours needed based on the student's habits and assignment properties.
    // Uses the adaptive weighted formula — weights improve over time via trainModel().
    // This is used for scheduling (procrastination days, recommended start date).
    // predictedHours = (weights[0] * difficulty)
    //                + (weights[1] * historicalAverage)
    //                + (weights[2] * gradeGoal)
    public double estimateWorkloadHours(Assignment assignment) {

        double historicalAverage = calculateAverageGrade();

        double estimatedHours = (weights[0] * assignment.getDifficulty())
                              + (weights[1] * historicalAverage)
                              + (weights[2] * assignment.getGradeGoal());

        // Floor at 0.5 hours — no assignment should ever predict zero or negative time
        return Math.max(0.5, estimatedHours);
    }

    // Calculates the exact hours the student needs to put in to reach their grade goal.
    // Derived by reversing the predictGrade formula:
    //   gradeGoal = studentAverage - difficultyPenalty + (hours * 0.4)
    //   hours     = (gradeGoal - studentAverage + difficultyPenalty) / 0.4
    // Returns at least 0.5 hours, and caps at a reasonable ceiling of 50 hours.
    public double predictHoursToReachGoal(Assignment assignment) {

        double studentAverage   = calculateAverageGrade();
        double difficultyPenalty = assignment.getDifficulty() * 2.5;

        double hoursNeeded = (assignment.getGradeGoal() - studentAverage + difficultyPenalty) / 0.4;

        hoursNeeded = Math.max(0.5, hoursNeeded);
        hoursNeeded = Math.min(50.0, hoursNeeded);

        return hoursNeeded;
    }

    public int calculateProcrastinationDays(Assignment assignment) {

        double predictedHours = estimateWorkloadHours(assignment);

        double daysNeeded = predictedHours / dailyCapacity;

        LocalDate today = LocalDate.now();

        long daysUntilDue = ChronoUnit.DAYS.between( today, assignment.getDueDate() );

        int procrastinationDays = (int)(daysUntilDue - daysNeeded);

        return procrastinationDays;
    }
    public LocalDate getRecommendedStartDate(Assignment assignment) {

        double predictedHours = estimateWorkloadHours(assignment);

        long daysNeeded = (long) Math.ceil(predictedHours / dailyCapacity);

        return LocalDate.now().plusDays(daysNeeded);
    }

        public double calculateAverageGrade() {

            double total = 0;
            int count = 0;

            for (Assignment assignment : assignments) {

                if (assignment.isCompleted()) {

                    total += assignment.getGradeReceived();
                    count++;
                }
            }

            if (count == 0) {

                return baseLineGrade;
            }

            return total / count;
        }

        public double predictGrade(Assignment assignment) {

            double studentAverage = calculateAverageGrade();

            double difficultyPenalty = assignment.getDifficulty() * 2.5;

            double predictedHours = estimateWorkloadHours(assignment);

            double effortBonus = predictedHours * 0.4;

            double predictedGrade = studentAverage - difficultyPenalty + effortBonus;

            predictedGrade = Math.max(50, predictedGrade);

            predictedGrade = Math.min(100, predictedGrade);

            return predictedGrade;
        }

        // Recalculates the predicted grade with a penalty for each day the student
        // has gone past the recommended start date without beginning.
        // Each overdue day adds a 0.5% grade penalty on top of the base prediction.
        // Returns the updated grade so the caller can store it back on the assignment.
        public double predictGradeWithProcrastinationPenalty(Assignment assignment) {

            double basePrediction = predictGrade(assignment);

            int procrastinationDays = calculateProcrastinationDays(assignment);

            // procrastinationDays > 0 means they still have buffer time — no penalty yet.
            // procrastinationDays <= 0 means they are past the safe start window.
            if (procrastinationDays >= 0) {

                return basePrediction;
            }

            // daysOverdue is how many days past the deadline they have waited
            int daysOverdue = Math.abs(procrastinationDays);

            double penaltyPerDay = 0.5;

            double totalPenalty = daysOverdue * penaltyPerDay;

            double penalizedGrade = basePrediction - totalPenalty;

            penalizedGrade = Math.max(50, penalizedGrade);

            return penalizedGrade;
        }

        public void trainModel() {

            int completedCount = 0;

            for (Assignment assignment : assignments) {

                if (assignment.isCompleted()) {

                    completedCount++;
                }
            }

            if (completedCount < 5) {

                return;
            }

            // Only train on assignments completed since the last training run
            // trainingCount tracks how many assignments were trained on last time
            int newlyCompleted = completedCount - trainingCount;

            if (newlyCompleted <= 0) {

                return;
            }

            double totalGradeError = 0;

            // Per-weight error accumulators: how much each input feature contributed
            // to the gap between predicted hours and actual hours spent
            double totalDifficultyError    = 0;
            double totalHistoricalError    = 0;
            double totalGradeGoalError     = 0;

            int seen = 0;

            for (int i = assignments.size() - 1; i >= 0 && seen < newlyCompleted; i--) {

                Assignment assignment = assignments.get(i);

                if (assignment.isCompleted()) {

                    // --- Grade error (for baselineGrade adaptation) ---
                    totalGradeError +=
                            assignment.getGradeReceived()
                            - assignment.getPredictedGrade();

                    // --- Hours error (for weight adaptation) ---
                    // hoursError > 0 means we under-predicted (student needed more time)
                    // hoursError < 0 means we over-predicted (student needed less time)
                    double predictedHours = estimateWorkloadHours(assignment);
                    double hoursError = assignment.getHoursSpent() - predictedHours;

                    double historicalAverage = calculateAverageGrade();

                    // Each weight's contribution to the error is proportional to its input value.
                    // Nudge weights in the direction that reduces future error.
                    totalDifficultyError  += hoursError * assignment.getDifficulty();
                    totalHistoricalError  += hoursError * historicalAverage;
                    totalGradeGoalError   += hoursError * assignment.getGradeGoal();

                    seen++;
                }
            }

            // --- Adapt baselineGrade ---
            double averageGradeError = totalGradeError / newlyCompleted;

            baseLineGrade += averageGradeError * learningRate;
            baseLineGrade = Math.max(50, baseLineGrade);
            baseLineGrade = Math.min(100, baseLineGrade);

            // --- Adapt weights ---
            // Divide by newlyCompleted to get average gradient, then step by learningRate.
            // Each weight is clamped so it can't go negative (hours can't decrease
            // because a factor gets larger) or grow unreasonably large.
            weights[0] += (totalDifficultyError  / newlyCompleted) * learningRate;
            weights[1] += (totalHistoricalError  / newlyCompleted) * learningRate;
            weights[2] += (totalGradeGoalError   / newlyCompleted) * learningRate;

            // Clamp weights to reasonable ranges so one outlier can't blow up the model
            weights[0] = Math.max(0.5, Math.min(5.0,  weights[0])); // difficulty:        0.5–5.0 hrs per level
            weights[1] = Math.max(0.0, Math.min(0.2,  weights[1])); // historicalAverage: 0.0–0.2 hrs per grade point
            weights[2] = Math.max(0.0, Math.min(0.1,  weights[2])); // gradeGoal:         0.0–0.1 hrs per grade point

            trainingCount = completedCount;

        }

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