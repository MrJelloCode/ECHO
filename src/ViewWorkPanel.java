/*
Name: Malaravan.V
Date: May 11th 2026
Purpose: Panel for viewing the user's current workload, including assignments and tests
*/


// Import Libraries
import javax.swing.*;
import java.awt.*;

import courseWork.Assignment;
import courseWork.Test;
public class ViewWorkPanel extends JPanel {

    private MainFrame frame;

    private AuthService authService;

    private JLabel titleLabel;

    private JTextArea workArea;

    private JScrollPane scrollPane;

    private JButton backButton;

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
        workArea = new JTextArea();

        workArea.setEditable(false);

        workArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        scrollPane = new JScrollPane(workArea);

        scrollPane.setBounds(100, 100, 800, 450);

        add(scrollPane);


        // Back button to return to the dashboard
        backButton = new JButton("Back");

        backButton.setBounds(430, 580, 120, 35);

        add(backButton);

        // Action listener for the back button to navigate back to the dashboard and refresh the view when returning

        backButton.addActionListener(e -> frame.showPanel("DASHBOARD"));
    }


    // Refresh the work area with the latest assignments and tests from the user's AI, formatted for readability
    public void refreshWork() {

      // Use StringBuilder for efficient string concatenation when building the display output
        StringBuilder output = new StringBuilder();

        // Display the current username at the top of the work area for context
        output.append("Logged in as: ")
              .append(authService.getCurrentUsername())
              .append("\n\n");

        output.append("ASSIGNMENTS\n");
        output.append("---------------------------------------------\n");

        // Check if there are any assignments and display them, otherwise show a message indicating no assignments found
        if (authService.getCurrentAI().getAssignments().size() == 0) {

            output.append("No assignments found.\n");
        }
        for (Assignment assignment : authService.getCurrentAI().getAssignments()) {

            output.append("Name: ")
                  .append(assignment.getAssignmentName())
                  .append("\n");

            output.append("Course: ")
                  .append(assignment.getCourseName())
                  .append("\n");

            output.append("Due Date: ")
                  .append(assignment.getDueDate())
                  .append("\n");

            output.append("Completed: ")
                  .append(assignment.isCompleted())
                  .append("\n");

            output.append("---------------------------------------------\n");
        }

        // Display the tests in a similar format, checking for the presence of tests and formatting their details for readability
        output.append("\nTESTS\n");
        output.append("---------------------------------------------\n");

        if (authService.getCurrentAI().getTests().size() == 0) {

            output.append("No tests found.\n");
        }


        for (Test test : authService.getCurrentAI().getTests()) {

            output.append("Name: ")
                  .append(test.getTestName())
                  .append("\n");

            output.append("Course: ")
                  .append(test.getCourseName())
                  .append("\n");

            output.append("Due Date: ")
                  .append(test.getDueDate())
                  .append("\n");

            output.append("Completed: ")
                  .append(test.isCompleted())
                  .append("\n");

            output.append("---------------------------------------------\n");
        }

        // Set the text of the work area to the formatted output string, allowing the user to see their current workload
        workArea.setText(output.toString());
    }
}