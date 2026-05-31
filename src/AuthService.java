import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.time.LocalDate;

import courseWork.Assignment;
import courseWork.Test;

public class AuthService {
    private String currentUsername;
    private AI currentAI;
    private boolean loggedIn;
    
    public AuthService(){
        currentUsername = "";
        currentAI = new AI();
        loggedIn  = false;
    }

    public AuthService(String currentUsername, AI currentAI, boolean loggedIn){
        this.currentUsername = currentUsername;
        this.currentAI = currentAI;
        this.loggedIn = loggedIn;
    }

    public String getCurrentUsername(){
        return currentUsername;
    }

    public void setCurrentUsername(String currentUsername){
        this.currentUsername = currentUsername;
    }

    public AI getCurrentAI() {
        return currentAI;
    }

    public void setCurrentAI(AI currentAI){
        this.currentAI = currentAI;
    }

    public boolean isLoggedIn(){
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn){
        this.loggedIn = loggedIn;
    }


    // Delimiter used to separate fields — pipe avoids clashing with commas in user text
    private static final String DELIM = "|";
    private static final String DELIM_REGEX = "\\|";

    // Main saving logic for the AuthService class

    public void saveUserData() {

        File folder = new File("data/users");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File userFile = new File(folder, currentUsername + ".txt");

        // Try-with-resources ensures the writer is always closed, even if an exception occurs
        try (PrintWriter writer = new PrintWriter(userFile)) {

            // SAVE AI settings (learning rate, trainingCount, weights)
            writer.println(
                "AI" + DELIM +
                currentAI.getLearningRate() + DELIM +
                currentAI.getTrainingCount() + DELIM +
                currentAI.getWeights()[0] + DELIM +
                currentAI.getWeights()[1] + DELIM +
                currentAI.getWeights()[2]
            );

            // SAVE ASSIGNMENTS
            for (Assignment assignment : currentAI.getAssignments()) {

                writer.println(
                        "ASSIGNMENT" + DELIM +
                        assignment.getAssignmentName() + DELIM +
                        assignment.getCourseName() + DELIM +
                        assignment.getDifficulty() + DELIM +
                        assignment.getGradeGoal() + DELIM +
                        assignment.getHoursSpent() + DELIM +
                        assignment.getDueDate() + DELIM +
                        assignment.getStartDate() + DELIM +
                        assignment.isCompleted() + DELIM +
                        assignment.getGradeReceived()
                );
            }

            // SAVE TESTS
            for (Test test : currentAI.getTests()) {

                writer.println(
                        "TEST" + DELIM +
                        test.getTestName() + DELIM +
                        test.getCourseName() + DELIM +
                        test.getDifficulty() + DELIM +
                        test.getTopicComplexity() + DELIM +
                        test.isCumulative() + DELIM +
                        test.getDueDate()
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void loadUserData() {

        currentAI = new AI();

        File userFile = new File("data/users/" + currentUsername + ".txt");

        if (!userFile.exists()) {
            return;
        }

        // Try-with-resources ensures the reader is always closed
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(DELIM_REGEX);

                // Guard: skip any line that is too short to be valid
                if (data.length < 2) {
                    continue;
                }

                if (data[0].equals("AI")) {

                    // Expect 6 fields: AI | learningRate | trainingCount | w0 | w1 | w2
                    if (data.length < 6) {
                        System.out.println("Warning: AI line malformed, skipping: " + line);
                        continue;
                    }

                    currentAI.setLearningRate(Double.parseDouble(data[1]));
                    currentAI.setTrainingCount(Integer.parseInt(data[2]));

                    double[] loadedWeights = new double[]{
                        Double.parseDouble(data[3]),
                        Double.parseDouble(data[4]),
                        Double.parseDouble(data[5])
                    };
                    currentAI.setWeights(loadedWeights);
                }

                else if (data[0].equals("ASSIGNMENT")) {

                    // Expect 10 fields: ASSIGNMENT | name | course | diff | gradeGoal | hours | dueDate | startDate | completed | gradeReceived
                    if (data.length < 10) {
                        System.out.println("Warning: ASSIGNMENT line malformed, skipping: " + line);
                        continue;
                    }

                    Assignment assignment = new Assignment(
                            data[1],
                            data[2],
                            Integer.parseInt(data[3]),
                            Double.parseDouble(data[4]),
                            Double.parseDouble(data[5]),
                            LocalDate.parse(data[6]),
                            LocalDate.parse(data[7]),
                            Boolean.parseBoolean(data[8]),
                            Double.parseDouble(data[9])
                    );

                    currentAI.addAssignment(assignment);
                }

                else if (data[0].equals("TEST")) {

                    // Expect 7 fields: TEST | name | course | diff | topicComplexity | cumulative | dueDate
                    if (data.length < 7) {
                        System.out.println("Warning: TEST line malformed, skipping: " + line);
                        continue;
                    }

                    Test test = new Test(
                            data[1],
                            data[2],
                            Integer.parseInt(data[3]),
                            Integer.parseInt(data[4]),
                            Boolean.parseBoolean(data[5]),
                            LocalDate.parse(data[6])
                    );

                    currentAI.addTest(test);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}