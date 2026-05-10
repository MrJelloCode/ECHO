import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private MainFrame frame;

    private AuthService authService;

    private JLabel titleLabel;

    private JButton addWorkButton;

    private JButton viewWorkButton;

    private JButton logoutButton;

    // DEFAULT CONSTRUCTOR

    public DashboardPanel(MainFrame frame, AuthService authService) {

        this.frame = frame;

        this.authService = authService;

        setLayout(null);

        setBackground(Color.WHITE);

        // TITLE

        titleLabel = new JLabel("Dashboard");

        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));

        titleLabel.setBounds(400, 80, 250, 40);

        add(titleLabel);

        // ADD WORK BUTTON

        addWorkButton = new JButton("Add Work");

        addWorkButton.setBounds(390, 220, 200, 45);

        add(addWorkButton);

        // VIEW WORK BUTTON

        viewWorkButton = new JButton("View Work");

        viewWorkButton.setBounds(390, 300, 200, 45);

        add(viewWorkButton);

        // LOGOUT BUTTON

        logoutButton = new JButton("Logout");

        logoutButton.setBounds(390, 380, 200, 45);

        add(logoutButton);

        // BUTTON ACTIONS

        addWorkButton.addActionListener(e -> frame.showPanel("ADD_WORK"));

        viewWorkButton.addActionListener(e -> frame.showPanel("VIEW_WORK"));

        logoutButton.addActionListener(e -> {

            authService.setCurrentUsername("");

            authService.setCurrentAI(new AI());

            authService.setLoggedIn(false);

            frame.showPanel("LOGIN");
        });
    }
}