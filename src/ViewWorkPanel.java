/*
Name: Malaravan.V
Date: June 1st 2026
Purpose: Panel for viewing the user's current workload, including assignments and tests. Allows users to see the AI prediction for each assignment and mark assignments as complete with hours spent and grade received to update the AI model.
*/


// Import Libraries
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import courseWork.Assignment;

public class ViewWorkPanel extends JPanel {

    private MainFrame frame;

    private AuthService authService;
    private JLabel titleLabel;
    private JButton backButton;

    private DefaultListModel<Assignment> assignmentModel;
    private JList<Assignment> assignmentList;
    private JScrollPane listScrollPane;
    private JTextArea detailsArea;
    private JScrollPane detailsScrollPane;
    private JButton completeButton;   
    
    // DEFAULT CONSTRUCTOR (Overloaded)

    public ViewWorkPanel(MainFrame frame, AuthService authService) {

      // Initialize references and set up the panel

      this.frame = frame;

      this.authService = authService;

      setLayout(null);

      setBackground(Color.WHITE);


        // Title label for the panel
      titleLabel = new JLabel("Your Work");

      titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

      titleLabel.setBounds(400, 30, 250, 40);

      add(titleLabel);


        // Text area to display the user's assignments and tests in a readable format
        
      assignmentModel = new DefaultListModel<>();
      assignmentList = new JList<>(assignmentModel);
      assignmentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      listScrollPane = new JScrollPane(assignmentList);

      listScrollPane.setBounds(50, 100, 250, 400);

      add(listScrollPane);

      detailsArea = new JTextArea();

      detailsArea.setEditable(false);

      detailsArea.setFont(
            new Font("Monospaced",
                  Font.PLAIN,
                  14));

      detailsScrollPane =
            new JScrollPane(detailsArea);

      detailsScrollPane.setBounds(
            330,
            100,
            550,
            400);

      add(detailsScrollPane);

      // Button to mark assignments as complete
      completeButton = new JButton("Mark Complete");

      completeButton.setBounds(
            500,
            520,
            180,
            35);

      add(completeButton);


      // Back button to return to the dashboard
      backButton = new JButton("Back");

      backButton.setBounds(430, 580, 120, 35);

      add(backButton);

      // Refresh the assignment details when returning to this panel
      assignmentList.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                  showSelectedAssignment();
            }
      });

      // Action listener for the complete button to mark the selected assignment as complete and input hours spent and grade received
      completeButton.addActionListener(e -> {
            markAssignmentComplete();
      });

      // Action listener for the back button to return to the dashboard
      backButton.addActionListener(e -> frame.showPanel("DASHBOARD"));
    }
    


    // Refresh the work area with the latest assignments from the user's AI
    public void refreshWork() {

      // Clear the current list and repopulate it with the latest assignments from the AI
      assignmentModel.clear();

      for (Assignment assignment : authService.getCurrentAI().getAssignments()) {

            assignmentModel.addElement(assignment);
      }
    }



    // Display the details of the selected assignment in the details area, including AI predictions for hours to reach the grade goal and recommended start date
    private void showSelectedAssignment() {

            Assignment assignment = assignmentList.getSelectedValue();

            if (assignment == null) {

                  return;
            }


            double hoursToReachGoal = authService.getCurrentAI().predictHoursToReachGoal(assignment);

            LocalDate recommendedStartDate = authService.getCurrentAI().getRecommendedStartDate(assignment);

            long daysUntilStart = java.time.temporal.ChronoUnit.DAYS.between( LocalDate.now(), recommendedStartDate);

            StringBuilder output = new StringBuilder();

            output.append("Assignment: ")
                  .append(assignment.getAssignmentName())
                  .append("\n\n");

            output.append("Course: ")
                  .append(assignment.getCourseName())
                  .append("\n");

            output.append("Difficulty: ")
                  .append(assignment.getDifficulty())
                  .append("\n");

            output.append("Grade Goal: ")
                  .append(String.format("%.1f", assignment.getGradeGoal()))
                  .append("%\n");

            output.append("Due Date: ")
                  .append(assignment.getDueDate())
                  .append("\n");

            output.append("Completed: ")
                  .append(assignment.isCompleted())
                  .append("\n\n");

            output.append("Hours to Reach Goal: ")
                  .append(String.format("%.2f", hoursToReachGoal))
                  .append(" hrs (to hit ")
                  .append(String.format("%.1f", assignment.getGradeGoal()))
                  .append("%)\n");

            output.append("Recommended Start Date: ")
                  .append(authService.getCurrentAI().getRecommendedStartDate(assignment))
                  .append("\n");

            // If the recommended start date is today or in the past, prompt the user to start immediately instead of showing a negative number of days until start
            if (daysUntilStart <= 0) {

                  output.append("Time left before starting: START NOW")
                        .append("\n");
            }

            else {

                  output.append("Time left before starting: ")
                        .append(daysUntilStart)
                        .append(" days")
                        .append("\n");
            }
            


            // If the assignment is completed, also show the hours spend and grade received.
            if (assignment.isCompleted()) {

                  output.append("\nHours Spent: ")
                        .append(assignment.getHoursSpent())
                        .append("\n");

                  output.append("Grade Received: ")
                        .append(assignment.getGradeReceived())
                        .append("\n");
            }

            detailsArea.setText(output.toString());
      }


      // Method to mark the selected assignment as complete, input hours spent and grade received, update the AI model with the new data, and refresh the display
      private void markAssignmentComplete() {

            Assignment assignment = assignmentList.getSelectedValue();

            // Validate that an assignment was selected and that all data is valid before marking the assignment as complete and updating the model. Show appropriate error messages if any checks fail.
            if (assignment == null) {

                  JOptionPane.showMessageDialog(this, "Please select an assignment first.");
                  return;
            }
            
            if (assignment.isCompleted()) {

                  JOptionPane.showMessageDialog(this, "This assignment is already completed.");
                  return;
            }

            String hoursInput = JOptionPane.showInputDialog( this, "Enter hours spent:");

            if (hoursInput == null) {
                  return;
            }

            String gradeInput = JOptionPane.showInputDialog( this, "Enter grade received:");

            if (gradeInput == null) {
                  return;
            }

            try {

                  double hoursSpent = Double.parseDouble(hoursInput);

                  double gradeReceived = Double.parseDouble(gradeInput);

                  // Validate hours is not negative
                  if (hoursSpent < 0) {
                        JOptionPane.showMessageDialog(this, "Hours spent cannot be negative.");
                        return;
                  }

                  // Validate grade is within 0–100
                  if (gradeReceived < 0 || gradeReceived > 100) {
                        JOptionPane.showMessageDialog(this, "Grade must be between 0 and 100.");
                        return;
                  }


                  // Assign values to the assignment and mark it complete, update data in the AI model and save the user's data
                  assignment.setHoursSpent(hoursSpent);

                  assignment.setGradeReceived(gradeReceived);

                  assignment.setCompleted(true);

                  authService.getCurrentAI().trainModel();

                  authService.saveUserData();

                  refreshWork();

                  showSelectedAssignment();

                  JOptionPane.showMessageDialog( this, "Assignment marked complete.");

            }

            catch (NumberFormatException e) {
                  // Show an error message if the user decided to be silly and not use numbers for there inputs
                  JOptionPane.showMessageDialog(this, "Please enter valid numbers."

                  );
            }
      }
}