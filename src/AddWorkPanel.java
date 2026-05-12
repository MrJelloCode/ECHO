/*
Name: Malaravan.V
Date: May 11th 2026
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

            int difficulty = Integer.parseInt(difficultyField.getText().trim());


            LocalDate dueDate = LocalDate.parse(dueDateField.getText().trim());

            Assignment assignment = new Assignment( assignmentName, courseName, difficulty,  0.0,  0.0,  dueDate, LocalDate.now(), false
            );

            authService.getCurrentAI().addAssignment(assignment);

            JOptionPane.showMessageDialog(frame, "Assignment added.");

            clearFields();

            frame.showPanel("DASHBOARD");

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

        dueDateField.setText("");
    }
}