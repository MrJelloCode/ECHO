import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

import courseWork.Assignment;


public class AddWorkPanel extends JPanel {

    private MainFrame frame;

    private AuthService authService;

    private JLabel titleLabel;

    private JLabel nameLabel;
    private JTextField nameField;

    private JLabel courseLabel;
    private JTextField courseField;

    private JLabel difficultyLabel;
    private JTextField difficultyField;

    private JLabel topicLabel;
    private JTextField topicField;

    private JLabel dueDateLabel;
    private JTextField dueDateField;

    private JButton addButton;

    private JButton backButton;

    // DEFAULT CONSTRUCTOR

    public AddWorkPanel(MainFrame frame, AuthService authService) {

        this.frame = frame;

        this.authService = authService;

        setLayout(null);

        setBackground(Color.WHITE);

        // TITLE

        titleLabel = new JLabel("Add Assignment");

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        titleLabel.setBounds(390, 30, 250, 40);

        add(titleLabel);

        // NAME

        nameLabel = new JLabel("Assignment Name:");

        nameLabel.setBounds(250, 120, 150, 25);

        add(nameLabel);

        nameField = new JTextField();

        nameField.setBounds(420, 120, 220, 25);

        add(nameField);

        // COURSE

        courseLabel = new JLabel("Course:");

        courseLabel.setBounds(250, 170, 150, 25);

        add(courseLabel);

        courseField = new JTextField();

        courseField.setBounds(420, 170, 220, 25);

        add(courseField);

        // DIFFICULTY

        difficultyLabel = new JLabel("Difficulty (1-5):");

        difficultyLabel.setBounds(250, 220, 150, 25);

        add(difficultyLabel);

        difficultyField = new JTextField();

        difficultyField.setBounds(420, 220, 220, 25);

        add(difficultyField);

        // TOPIC COMPLEXITY

        topicLabel = new JLabel("Topic Complexity (1-5):");

        topicLabel.setBounds(250, 270, 170, 25);

        add(topicLabel);

        topicField = new JTextField();

        topicField.setBounds(420, 270, 220, 25);

        add(topicField);

        // DUE DATE

        dueDateLabel = new JLabel("Due Date (YYYY-MM-DD):");

        dueDateLabel.setBounds(250, 320, 170, 25);

        add(dueDateLabel);

        dueDateField = new JTextField();

        dueDateField.setBounds(420, 320, 220, 25);

        add(dueDateField);

        // ADD BUTTON

        addButton = new JButton("Add");

        addButton.setBounds(330, 420, 120, 35);

        add(addButton);

        // BACK BUTTON

        backButton = new JButton("Back");

        backButton.setBounds(500, 420, 120, 35);

        add(backButton);

        // BUTTON ACTIONS

        addButton.addActionListener(e -> addAssignment());

        backButton.addActionListener(e -> frame.showPanel("DASHBOARD"));
    }

    // ADD ASSIGNMENT METHOD

    private void addAssignment() {

        try {

            String assignmentName = nameField.getText().trim();

            String courseName = courseField.getText().trim();

            int difficulty = Integer.parseInt(difficultyField.getText().trim());

            int topicComplexity = Integer.parseInt(topicField.getText().trim());

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

    // CLEAR INPUT FIELDS

    private void clearFields() {

        nameField.setText("");

        courseField.setText("");

        difficultyField.setText("");

        topicField.setText("");

        dueDateField.setText("");
    }
}