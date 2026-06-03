/*
Name: Malaravan.V
Date: May 11th 2026
Purpose: Panel for viewing the user's current workload, including assignments and tests
*/

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import courseWork.Assignment;
import courseWork.Test;

public class ViewWorkPanel extends JPanel {

    private MainFrame frame;
    private AuthService authService;

    private JLabel titleLabel;
    private JButton backButton;

    // List holds both Assignment and Test via their shared toString labels
    private DefaultListModel<Object> workModel;
    private JList<Object> workList;
    private JScrollPane listScrollPane;
    private JTextArea detailsArea;
    private JScrollPane detailsScrollPane;
    private JButton completeButton;

    public ViewWorkPanel(MainFrame frame, AuthService authService) {

        this.frame       = frame;
        this.authService = authService;

        setLayout(null);
        setBackground(Color.WHITE);

        titleLabel = new JLabel("Your Work");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setBounds(400, 30, 250, 40);
        add(titleLabel);

        workModel = new DefaultListModel<>();
        workList  = new JList<>(workModel);
        workList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        listScrollPane = new JScrollPane(workList);
        listScrollPane.setBounds(50, 100, 250, 400);
        add(listScrollPane);

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        detailsScrollPane = new JScrollPane(detailsArea);
        detailsScrollPane.setBounds(330, 100, 550, 400);
        add(detailsScrollPane);

        completeButton = new JButton("Mark Complete");
        completeButton.setBounds(500, 520, 180, 35);
        add(completeButton);

        backButton = new JButton("Back");
        backButton.setBounds(430, 580, 120, 35);
        add(backButton);

        workList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showSelectedWork();
        });

        completeButton.addActionListener(e -> markComplete());

        backButton.addActionListener(e -> frame.showPanel("DASHBOARD"));
    }

    // Refresh the list with all assignments then all tests
    public void refreshWork() {

        workModel.clear();

        for (Assignment assignment : authService.getCurrentAI().getAssignments()) {
            workModel.addElement(assignment);
        }

        for (Test test : authService.getCurrentAI().getTests()) {
            workModel.addElement(test);
        }
    }

    // Show details for whichever item (Assignment or Test) is selected
    private void showSelectedWork() {

        Object selected = workList.getSelectedValue();

        if (selected == null) return;

        if (selected instanceof Assignment) {
            showAssignmentDetails((Assignment) selected);
        } else if (selected instanceof Test) {
            showTestDetails((Test) selected);
        }
    }

    private void showAssignmentDetails(Assignment assignment) {

        AI ai = authService.getCurrentAI();

        double hoursToReachGoal   = ai.predictHoursToReachGoal(assignment);
        LocalDate recommendedStart = ai.getRecommendedStartDate(assignment);
        long daysUntilStart       = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), recommendedStart);

        StringBuilder output = new StringBuilder();

        output.append("Assignment: ").append(assignment.getAssignmentName()).append("\n\n");
        output.append("Course: ").append(assignment.getCourseName()).append("\n");
        output.append("Difficulty: ").append(assignment.getDifficulty()).append("\n");
        output.append("Grade Goal: ").append(String.format("%.1f", assignment.getGradeGoal())).append("%\n");
        output.append("Due Date: ").append(assignment.getDueDate()).append("\n");
        output.append("Completed: ").append(assignment.isCompleted()).append("\n\n");

        output.append("Hours to Reach Goal: ")
              .append(String.format("%.2f", hoursToReachGoal))
              .append(" hrs (to hit ").append(String.format("%.1f", assignment.getGradeGoal())).append("%)\n");

        if (daysUntilStart <= 0) {
            output.append("Time left before starting: START NOW\n");
        } else {
            output.append("Time left before starting: ").append(daysUntilStart).append(" days\n");
        }

        output.append("Recommended Start Date: ").append(recommendedStart).append("\n");

        if (assignment.isCompleted()) {
            output.append("\nHours Spent: ").append(assignment.getHoursSpent()).append("\n");
            output.append("Grade Received: ").append(assignment.getGradeReceived()).append("\n");
        }

        detailsArea.setText(output.toString());
    }

    private void showTestDetails(Test test) {

        AI ai = authService.getCurrentAI();

        double hoursToReachGoal   = ai.predictHoursToReachGoal(test);
        LocalDate recommendedStart = ai.getRecommendedStartDate(test);
        long daysUntilStart       = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), recommendedStart);

        StringBuilder output = new StringBuilder();

        output.append("Test: ").append(test.getTestName()).append("\n\n");
        output.append("Course: ").append(test.getCourseName()).append("\n");
        output.append("Difficulty: ").append(test.getDifficulty()).append("\n");
        output.append("Cumulative: ").append(test.isCumulative()).append("\n");
        output.append("Grade Goal: ").append(String.format("%.1f", test.getGradeGoal())).append("%\n");
        output.append("Due Date: ").append(test.getDueDate()).append("\n");
        output.append("Completed: ").append(test.isCompleted()).append("\n\n");

        output.append("Hours to Reach Goal: ")
              .append(String.format("%.2f", hoursToReachGoal))
              .append(" hrs (to hit ").append(String.format("%.1f", test.getGradeGoal())).append("%)\n");

        if (daysUntilStart <= 0) {
            output.append("Time left before starting: START NOW\n");
        } else {
            output.append("Time left before starting: ").append(daysUntilStart).append(" days\n");
        }

        output.append("Recommended Start Date: ").append(recommendedStart).append("\n");

        if (test.isCompleted()) {
            output.append("\nHours Spent: ").append(test.getHoursSpent()).append("\n");
            output.append("Grade Received: ").append(test.getGradeReceived()).append("\n");
        }

        detailsArea.setText(output.toString());
    }

    // Mark whichever item is selected as complete
    private void markComplete() {

        Object selected = workList.getSelectedValue();

        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select an item first.");
            return;
        }

        if (selected instanceof Assignment) {
            markAssignmentComplete((Assignment) selected);
        } else if (selected instanceof Test) {
            markTestComplete((Test) selected);
        }
    }

    private void markAssignmentComplete(Assignment assignment) {

        if (assignment.isCompleted()) {
            JOptionPane.showMessageDialog(this, "This assignment is already completed.");
            return;
        }

        String hoursInput = JOptionPane.showInputDialog(this, "Enter hours spent:");
        if (hoursInput == null) return;

        String gradeInput = JOptionPane.showInputDialog(this, "Enter grade received:");
        if (gradeInput == null) return;

        try {

            double hoursSpent    = Double.parseDouble(hoursInput);
            double gradeReceived = Double.parseDouble(gradeInput);

            if (hoursSpent < 0) {
                JOptionPane.showMessageDialog(this, "Hours spent cannot be negative.");
                return;
            }

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
            showSelectedWork();

            JOptionPane.showMessageDialog(this, "Assignment marked complete.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
        }
    }

    private void markTestComplete(Test test) {

        if (test.isCompleted()) {
            JOptionPane.showMessageDialog(this, "This test is already completed.");
            return;
        }

        String hoursInput = JOptionPane.showInputDialog(this, "Enter hours spent:");
        if (hoursInput == null) return;

        String gradeInput = JOptionPane.showInputDialog(this, "Enter grade received:");
        if (gradeInput == null) return;

        try {

            double hoursSpent    = Double.parseDouble(hoursInput);
            double gradeReceived = Double.parseDouble(gradeInput);

            if (hoursSpent < 0) {
                JOptionPane.showMessageDialog(this, "Hours spent cannot be negative.");
                return;
            }

            if (gradeReceived < 0 || gradeReceived > 100) {
                JOptionPane.showMessageDialog(this, "Grade must be between 0 and 100.");
                return;
            }

            test.setHoursSpent(hoursSpent);
            test.setGradeReceived(gradeReceived);
            test.setCompleted(true);

            authService.getCurrentAI().trainModel();
            authService.saveUserData();
            refreshWork();
            showSelectedWork();

            JOptionPane.showMessageDialog(this, "Test marked complete.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
        }
    }
}