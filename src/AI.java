// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
import courseWork.Assignment;
import courseWork.Test;
import java.util.ArrayList;

public class AI {
   private ArrayList<Assignment> assignments;
   private ArrayList<Test> tests;
   private double[] weights;
   private double learningRate;
   private int trainingCount;
   private double dailyCapacity;
   private double baseLineGrade;

   public AI() {
      this.assignments = new ArrayList();
      this.tests = new ArrayList();
      this.weights = new double[4];
      this.learningRate = 0.01;
      this.trainingCount = 0;
      this.dailyCapacity = (double)3.0F;
      this.baseLineGrade = (double)85.0F;
   }

   public AI(ArrayList<Assignment> var1, ArrayList<Test> var2, double[] var3, double var4, int var6, double var7, double var9) {
      this.assignments = var1;
      this.tests = var2;
      this.weights = var3;
      this.learningRate = var4;
      this.trainingCount = var6;
      this.dailyCapacity = var7;
      this.baseLineGrade = var9;
   }

   public ArrayList<Assignment> getAssignments() {
      return this.assignments;
   }

   public void setAssignments(ArrayList<Assignment> var1) {
      this.assignments = var1;
   }

   public ArrayList<Test> getTests() {
      return this.tests;
   }

   public void setTests(ArrayList<Test> var1) {
      this.tests = var1;
   }

   public double[] getWeights() {
      return this.weights;
   }

   public void setWeights(double[] var1) {
      this.weights = var1;
   }

   public double getLearningRate() {
      return this.learningRate;
   }

   public void setLearningRate(double var1) {
      this.learningRate = var1;
   }

   public int getTrainingCount() {
      return this.trainingCount;
   }

   public void setTrainingCount(int var1) {
      this.trainingCount = var1;
   }

   public double getDailyCapacity() {
      return this.dailyCapacity;
   }

   public void setDailyCapacity(double var1) {
      this.dailyCapacity = var1;
   }

   public double getBaselineGrade() {
      return this.baseLineGrade;
   }

   public void setBaselineGrade(double var1) {
      this.baseLineGrade = var1;
   }

   public void addAssignment(Assignment var1) {
      this.assignments.add(var1);
   }

   public void removeAssignment(Assignment var1) {
      this.assignments.remove(var1);
   }

   public void addTest(Test var1) {
      this.tests.add(var1);
   }

   public void removeTest(Test var1) {
      this.tests.remove(var1);
   }
}
