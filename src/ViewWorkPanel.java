
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

    // DEFAULT CONSTRUCTOR

    public ViewWorkPanel(MainFrame frame, AuthService authService) {

        this.frame = frame;

        this.authService = authService;

        setLayout(null);

        setBackground(Color.WHITE);


        titleLabel = new JLabel("Your Work");

        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));

        titleLabel.setBounds(400, 30, 250, 40);

        add(titleLabel);


        workArea = new JTextArea();

        workArea.setEditable(false);

        workArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        scrollPane = new JScrollPane(workArea);

        scrollPane.setBounds(100, 100, 800, 450);

        add(scrollPane);


        backButton = new JButton("Back");

        backButton.setBounds(430, 580, 120, 35);

        add(backButton);

        backButton.addActionListener(e -> frame.showPanel("DASHBOARD"));
    }

    // REFRESH PANEL DATA

    public void refreshWork() {

        StringBuilder output = new StringBuilder();

        output.append("Logged in as: ")
              .append(authService.getCurrentUsername())
              .append("\n\n");

        output.append("ASSIGNMENTS\n");
        output.append("---------------------------------------------\n");

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

        workArea.setText(output.toString());
    }
}