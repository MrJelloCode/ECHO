/*
Name: Malaravan.V
Date: June 1st 2026
Purpose: Panel for adding new assignments to the user's workload
*/


// Import Libraries
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import courseWork.Assignment;

public class AddWorkPanel extends JPanel {

    // Reference to the main frame and authentication service
    private MainFrame frame;

    private AuthService authService;

    // UI Components
    private JLabel titleLabel;

    private JLabel nameLabel;
    private JTextField nameField;

    private JLabel courseLabel;
    private JTextField courseField;

    private JLabel difficultyLabel;
    private JTextField difficultyField;

    private JLabel gradeGoalLabel;
    private JTextField gradeGoalField;

    private JLabel dueDateLabel;
    private JTextField dueDateField;

    private JButton addButton;

    private JButton backButton;

    // DEFAULT CONSTRUCTOR (Overloaded)
    public AddWorkPanel(MainFrame frame, AuthService authService) {

        // Initialize references and set up the panel
        this.frame = frame;

        this.authService = authService;

        setLayout(null);

        setBackground(Color.WHITE);

        // Title Label

        titleLabel = new JLabel("Add Assignment");

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        titleLabel.setBounds(390, 30, 250, 40);

        add(titleLabel);

        // Name label and field for the assignment

        nameLabel = new JLabel("Assignment Name:");

        nameLabel.setBounds(250, 120, 150, 25);

        add(nameLabel);

        nameField = new JTextField();

        nameField.setBounds(420, 120, 220, 25);

        add(nameField);

        // Course label and field for the assignment

        courseLabel = new JLabel("Course:");

        courseLabel.setBounds(250, 170, 150, 25);

        add(courseLabel);

        courseField = new JTextField();

        courseField.setBounds(420, 170, 220, 25);

        add(courseField);

        // Difficulty label and field for the assignment

        difficultyLabel = new JLabel("Difficulty (1-5):");

        difficultyLabel.setBounds(250, 220, 150, 25);

        add(difficultyLabel);

        difficultyField = new JTextField();

        difficultyField.setBounds(420, 220, 220, 25);

        add(difficultyField);

        // Grade goal label and field for the assignment

        gradeGoalLabel = new JLabel("Grade Goal (0-100):");

        gradeGoalLabel.setBounds(250, 270, 160, 25);

        add(gradeGoalLabel);

        gradeGoalField = new JTextField();

        gradeGoalField.setBounds(420, 270, 220, 25);

        add(gradeGoalField);

        // Due Date label and field for the assignment

        dueDateLabel = new JLabel("Due Date (YYYY-MM-DD):");

        dueDateLabel.setBounds(250, 320, 170, 25);

        add(dueDateLabel);

        dueDateField = new JTextField();

        dueDateField.setBounds(420, 320, 220, 25);

        add(dueDateField);

        // Add button for the assignment

        addButton = new JButton("Add");

        addButton.setBounds(330, 420, 120, 35);

        add(addButton);

        // Back button for the assignment

        backButton = new JButton("Back");

        backButton.setBounds(500, 420, 120, 35);

        add(backButton);

        // Action listeners for the buttons

        addButton.addActionListener(e -> addAssignment());

        backButton.addActionListener(e -> frame.showPanel("DASHBOARD"));
    }

    // Method to add an assignment based on user input and handle any exceptions

    private void addAssignment() {

        try {

            String assignmentName = nameField.getText().trim();

            String courseName = courseField.getText().trim();

            // Check that name and course are not blank
            if (assignmentName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter an assignment name.");
                return;
            }

            if (courseName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter a course name.");
                return;
            }

            int difficulty = Integer.parseInt(difficultyField.getText().trim());

            // Check that difficulty is within the valid range
            if (difficulty < 1 || difficulty > 5) {
                JOptionPane.showMessageDialog(frame, "Difficulty must be between 1 and 5.");
                return;
            }

            double gradeGoal = Double.parseDouble(gradeGoalField.getText().trim());

            // Check that grade goal is within 0-100
            if (gradeGoal < 0 || gradeGoal > 100) {
                JOptionPane.showMessageDialog(frame, "Grade goal must be between 0 and 100.");
                return;
            }

            LocalDate dueDate = LocalDate.parse(dueDateField.getText().trim());

            // Check that the due date is strictly after today (today itself is not valid — there would be zero days to work on it)
            if (!dueDate.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(frame, "Due date must be after today.");
                return;
            }

            // If all inputs are valid, create a new Assignment object and add it to the user's AI, then save the data and clear the fields
            Assignment assignment = new Assignment(
                assignmentName,
                courseName,
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

            clearFields();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(frame,
                    "Invalid input. Please check your fields.");
        }
    }

    // Clear input fields after adding an assignment or when needed

    private void clearFields() {

        nameField.setText("");

        courseField.setText("");

        difficultyField.setText("");

        gradeGoalField.setText("");

        dueDateField.setText("");
    }
}