/*
Name: Malaravan.V
Date: May 11th 2026
Purpose: AI class to manage the machine learning model for predicting optimal start times and workload management based on user data
*/


// Import Libraries
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
        weights = new double[4];
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
}