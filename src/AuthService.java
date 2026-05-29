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


    // Main saving loggic for the AuthService class

    public void saveUserData() {

        try {

            File folder = new File("data/users");

            if (!folder.exists()) {
                folder.mkdirs();
            }

            File userFile = new File(folder, currentUsername + ".txt");

            PrintWriter writer = new PrintWriter(userFile);

            // SAVE ASSIGNMENTS

            for (Assignment assignment :
                    currentAI.getAssignments()) {

                writer.println(
                        "ASSIGNMENT," +
                        assignment.getAssignmentName() + "," +
                        assignment.getCourseName() + "," +
                        assignment.getDifficulty() + "," +
                        assignment.getHoursSpent() + "," +
                        assignment.getPredictedGrade() + "," +
                        assignment.getDueDate() + "," +
                        assignment.getStartDate() + "," +
                        assignment.isCompleted()
                );
            }

            // SAVE TESTS

            for (Test test : currentAI.getTests()) {

                writer.println(
                        "TEST," +
                        test.getTestName() + "," +
                        test.getCourseName() + "," +
                        test.getDifficulty() + "," +
                        test.getTopicComplexity() + "," +
                        test.isCumulative() + "," +
                        test.getDueDate()
                );
            }

            writer.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void loadUserData() {

        currentAI = new AI();

        try {

            File userFile = new File("data/users/" + currentUsername +".txt");

            if (!userFile.exists()) {
                return;
            }

            BufferedReader reader =
                    new BufferedReader(new FileReader(userFile)
                    );

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                if (data[0].equals("ASSIGNMENT")) {

                    Assignment assignment = new Assignment(
                                    data[1],
                                    data[2],
                                    Integer.parseInt(data[3]),
                                    Double.parseDouble(data[4]),
                                    Double.parseDouble(data[5]),
                                    LocalDate.parse(data[6]),
                                    LocalDate.parse(data[7]),
                                    Boolean.parseBoolean(data[8])
                            );

                    currentAI.addAssignment(assignment);
                }

                else if (data[0].equals("TEST")) {

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

            reader.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
}
}
