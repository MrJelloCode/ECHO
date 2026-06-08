/*
Name: Malaravan.V
Date: May 11th 2026
Purpose: Panel for adding new assignments or tests to the user's workload
*/

// Libraries and packages
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import courseWork.Assignment;
import courseWork.Test;

public class AddWorkPanel extends JPanel {

    // Attributes
    private MainFrame frame;
    private AuthService authService;

    // Toggle buttons to switch between Assignment and Test forms
    private JButton assignmentToggle;
    private JButton testToggle;

    // Panels one for each form, swapped in/out by the toggle
    private JPanel assignmentForm;
    private JPanel testForm;

    // Shared: the currently visible form
    private JPanel currentForm;

    // Assignment form fields
    private JTextField aNameField;
    private JTextField aCourseField;
    private JTextField aDifficultyField;
    private JTextField aGradeGoalField;
    private JTextField aDueDateField;

    // Test form fields
    private JTextField tNameField;
    private JTextField tCourseField;
    private JTextField tDifficultyField;
    private JTextField tGradeGoalField;
    private JTextField tDueDateField;
    private JCheckBox  tCumulativeCheckbox;

    private JButton addButton;
    private JButton backButton;

    public AddWorkPanel(MainFrame frame, AuthService authService) {

        this.frame       = frame;
        this.authService = authService;

        setLayout(null);
        setBackground(Color.WHITE);

        // Title
        JLabel titleLabel = new JLabel("Add Work");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(390, 20, 200, 40);
        add(titleLabel);

        // Toggle buttons
        assignmentToggle = new JButton("Assignment");
        assignmentToggle.setBounds(270, 70, 150, 30);
        add(assignmentToggle);

        testToggle = new JButton("Test");
        testToggle.setBounds(440, 70, 150, 30);
        add(testToggle);

        // Build both forms
        assignmentForm = buildAssignmentForm();
        testForm       = buildTestForm();

        // Show assignment form by default
        currentForm = assignmentForm;
        add(currentForm);

        // Add and Back buttons
        addButton = new JButton("Add");
        addButton.setBounds(330, 490, 120, 35);
        add(addButton);

        backButton = new JButton("Back");
        backButton.setBounds(500, 490, 120, 35);
        add(backButton);

        // Listeners
        assignmentToggle.addActionListener(e -> showForm(assignmentForm));
        testToggle.addActionListener(e -> showForm(testForm));
        addButton.addActionListener(e -> {
            if (currentForm == assignmentForm) addAssignment();
            else                               addTest();
        });
        backButton.addActionListener(e -> frame.showPanel("DASHBOARD"));
    }

    // Swap the visible form
    private void showForm(JPanel form) {
        remove(currentForm);
        currentForm = form;
        add(currentForm);
        revalidate();
        repaint();
    }

    // Assignment form builder 
    private JPanel buildAssignmentForm() {

        JPanel p = new JPanel(null);
        p.setBackground(Color.WHITE);
        p.setBounds(0, 110, 950, 370);

        p.add(makeLabel("Assignment Name:", 250, 10));
        aNameField = makeField(420, 10); p.add(aNameField);

        p.add(makeLabel("Course:", 250, 60));
        aCourseField = makeField(420, 60); p.add(aCourseField);

        p.add(makeLabel("Difficulty (1-5):", 250, 110));
        aDifficultyField = makeField(420, 110); p.add(aDifficultyField);

        p.add(makeLabel("Grade Goal (0-100):", 250, 160));
        aGradeGoalField = makeField(420, 160); p.add(aGradeGoalField);

        p.add(makeLabel("Due Date (YYYY-MM-DD):", 250, 210));
        aDueDateField = makeField(420, 210); p.add(aDueDateField);

        return p;
    }

    // Test form builder 
    private JPanel buildTestForm() {

        JPanel p = new JPanel(null);
        p.setBackground(Color.WHITE);
        p.setBounds(0, 110, 950, 370);

        p.add(makeLabel("Test Name:", 250, 10));
        tNameField = makeField(420, 10); p.add(tNameField);

        p.add(makeLabel("Course:", 250, 60));
        tCourseField = makeField(420, 60); p.add(tCourseField);

        p.add(makeLabel("Difficulty (1-5):", 250, 110));
        tDifficultyField = makeField(420, 110); p.add(tDifficultyField);

        p.add(makeLabel("Grade Goal (0-100):", 250, 160));
        tGradeGoalField = makeField(420, 160); p.add(tGradeGoalField);

        p.add(makeLabel("Due Date (YYYY-MM-DD):", 250, 210));
        tDueDateField = makeField(420, 210); p.add(tDueDateField);

        tCumulativeCheckbox = new JCheckBox("Cumulative");
        tCumulativeCheckbox.setBackground(Color.WHITE);
        tCumulativeCheckbox.setBounds(250, 260, 150, 25);
        p.add(tCumulativeCheckbox);

        return p;
    }

    // Helpers methods for formating
    private JLabel makeLabel(String text, int x, int y) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, 170, 25);
        return l;
    }

    private JTextField makeField(int x, int y) {
        JTextField f = new JTextField();
        f.setBounds(x, y, 220, 25);
        return f;
    }

    // Add Assignment logic 
    private void addAssignment() {


        //Validate all inputs and show error messages if invalid, otherwise create the assignment and save it to the user's data
        try {

            String name   = aNameField.getText().trim();
            String course = aCourseField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter an assignment name.");
                return;
            }

            if (course.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a course name.");
                return;
            }

            int difficulty = Integer.parseInt(aDifficultyField.getText().trim());

            if (difficulty < 1 || difficulty > 5) {
                JOptionPane.showMessageDialog(frame, "Difficulty must be between 1 and 5.");
                return;
            }

            double gradeGoal = Double.parseDouble(aGradeGoalField.getText().trim());

            if (gradeGoal < 0 || gradeGoal > 100) {
                JOptionPane.showMessageDialog(frame, "Grade goal must be between 0 and 100.");
                return;
            }

            LocalDate dueDate = LocalDate.parse(aDueDateField.getText().trim());

            if (!dueDate.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(frame, "Due date must be after today.");
                return;
            }

            Assignment assignment = new Assignment(
                name,
                course,
                difficulty,
                gradeGoal,
                0.0,
                dueDate,
                LocalDate.now(),
                false,
                0.0);

            authService.getCurrentAI().addAssignment(assignment);
            authService.saveUserData();

            JOptionPane.showMessageDialog(frame, "Assignment added.");
            clearAssignmentFields();
            frame.showPanel("DASHBOARD");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please check your fields.");
        }
    }

    // Add Test logic
    private void addTest() {

        //Validate all inputs and show error messages if invalid, otherwise create the test and save it to the user's data
        try {

            String name   = tNameField.getText().trim();
            String course = tCourseField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a test name.");
                return;
            }

            if (course.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a course name.");
                return;
            }

            int difficulty = Integer.parseInt(tDifficultyField.getText().trim());

            if (difficulty < 1 || difficulty > 5) {
                JOptionPane.showMessageDialog(frame, "Difficulty must be between 1 and 5.");
                return;
            }

          
            double gradeGoal = Double.parseDouble(tGradeGoalField.getText().trim());

            if (gradeGoal < 0 || gradeGoal > 100) {
                JOptionPane.showMessageDialog(frame, "Grade goal must be between 0 and 100.");
                return;
            }

            LocalDate dueDate = LocalDate.parse(tDueDateField.getText().trim());

            if (!dueDate.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(frame, "Due date must be after today.");
                return;
            }

            boolean cumulative = tCumulativeCheckbox.isSelected();

            Test test = new Test(
                name, course, difficulty,cumulative, gradeGoal, dueDate, LocalDate.now(), false, 0.0);

            authService.getCurrentAI().addTest(test);
            authService.saveUserData();

            JOptionPane.showMessageDialog(frame, "Test added.");
            clearTestFields();
            frame.showPanel("DASHBOARD");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input. Please check your fields.");
        }
    }

    //Clear form fields for assignments and tests
    private void clearAssignmentFields() {
        aNameField.setText("");
        aCourseField.setText("");
        aDifficultyField.setText("");
        aGradeGoalField.setText("");
        aDueDateField.setText("");
    }

    private void clearTestFields() {
        tNameField.setText("");
        tCourseField.setText("");
        tDifficultyField.setText("");

        tGradeGoalField.setText("");
        tDueDateField.setText("");
        tCumulativeCheckbox.setSelected(false);
    }
}