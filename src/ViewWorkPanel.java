/*
Name: Malaravan.V
Date: May 11th 2026
Purpose: Panel for viewing the user's current workload, including assignments and tests
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

        // Action listener for the back button to navigate back to the dashboard and refresh the view when returning

      assignmentList.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                  showSelectedAssignment();
            }
      });

      completeButton.addActionListener(e -> {
            markAssignmentComplete();
      });


      backButton.addActionListener(e -> frame.showPanel("DASHBOARD"));
    }
    


    // Refresh the work area with the latest assignments from the user's AI
    public void refreshWork() {

      assignmentModel.clear();

      for (Assignment assignment : authService.getCurrentAI().getAssignments()) {

            assignmentModel.addElement(assignment);
      }
    }




    private void showSelectedAssignment() {

            Assignment assignment =
                        assignmentList.getSelectedValue();

            if (assignment == null) {

                  return;
            }

            double hoursToReachGoal =
                        authService.getCurrentAI()
                              .predictHoursToReachGoal(
                                          assignment);

            LocalDate recommendedStartDate =
                        authService.getCurrentAI()
                              .getRecommendedStartDate(assignment);

            long daysUntilStart =
                        java.time.temporal.ChronoUnit.DAYS.between(
                              LocalDate.now(), recommendedStartDate);

            StringBuilder output =
                        new StringBuilder();

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

            if (daysUntilStart <= 0) {

                  output.append(
                        "Time left before starting: START NOW")
                        .append("\n");
            }

            else {

                  output.append(
                        "Time left before starting: ")
                        .append(daysUntilStart)
                        .append(" days")
                        .append("\n");
            }
            


            if (assignment.isCompleted()) {

                  output.append("\nHours Spent: ")
                        .append(assignment.getHoursSpent())
                        .append("\n");

                  output.append("Grade Received: ")
                        .append(assignment.getGradeReceived())
                        .append("\n");
            }

            output.append("Recommended Start Date: ")
                  .append(authService.getCurrentAI()
                                    .getRecommendedStartDate(assignment))
                  .append("\n");

            detailsArea.setText(output.toString());
      }


      private void markAssignmentComplete() {

            Assignment assignment = assignmentList.getSelectedValue();

            if (assignment == null) {

                  JOptionPane.showMessageDialog(
                        this,
                        "Please select an assignment first."
                  );

                  return;
            }
            
            if (assignment.isCompleted()) {

                  JOptionPane.showMessageDialog(
                              this,
                              "This assignment is already completed."
                  );

                  return;
            }

            String hoursInput =
                        JOptionPane.showInputDialog(
                              this,
                              "Enter hours spent:"
                        );

            if (hoursInput == null) {
                  return;
            }

            String gradeInput =
                        JOptionPane.showInputDialog(
                              this,
                              "Enter grade received:"
                        );

            if (gradeInput == null) {
                  return;
            }

            try {

                  double hoursSpent =
                        Double.parseDouble(hoursInput);

                  double gradeReceived =
                        Double.parseDouble(gradeInput);

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

                  assignment.setHoursSpent(hoursSpent);

                  assignment.setGradeReceived(gradeReceived);

                  assignment.setCompleted(true);

                  authService.getCurrentAI().trainModel();

                  authService.saveUserData();

                  refreshWork();

                  showSelectedAssignment();

                  JOptionPane.showMessageDialog(
                        this,
                        "Assignment marked complete."
                  );

            }

            catch (NumberFormatException e) {

                  JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid numbers."
                  );
            }
      }
}