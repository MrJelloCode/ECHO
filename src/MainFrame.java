/*
Name: Malaravan.V
Date: May 11th 2026
Purpose: MainFrame class to manage the main application window and navigation between different panels
*/


// Import libraries
import javax.swing.*;

import java.awt.*;

// MainFrame class to manage the main application window and navigation between different panels
public class MainFrame extends JFrame{
    // CardLayout to manage different panels and references to the panels and authentication service
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private AuthService authService;

    private LoginPanel loginPanel;
    private DashboardPanel dashboardPanel;
    private AddWorkPanel addWorkPanel;
    private ViewWorkPanel viewWorkPanel;

    // DEFAULT CONSTRUCTOR (Overloaded) to set up the main frame and initialize all panels and services
    public MainFrame(){
        authService = new AuthService();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize all panels with references to the main frame and authentication service for data and frame access
        loginPanel = new LoginPanel(this, authService);
        dashboardPanel = new DashboardPanel(this, authService);
        addWorkPanel = new AddWorkPanel(this, authService);
        viewWorkPanel = new ViewWorkPanel(this, authService);

        // Add all panels to the main panel with unique identifiers for navigation
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(dashboardPanel, "DASHBOARD");
        mainPanel.add(addWorkPanel, "ADD_WORK");
        mainPanel.add(viewWorkPanel, "VIEW_WORK");

        add(mainPanel);

        // Set up the main frame properties
        setTitle("ECHO");

        setSize(1000, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setResizable(false);

        showPanel("LOGIN");

        setVisible(true);
    }


    // Show the specified panel and refresh its content if necessary
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);

        if (panelName.equals("VIEW_WORK")) {
            viewWorkPanel.refreshWork();
        }

    }

    //Main method to launch the application  
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainFrame();
            }
        });
    }
}
