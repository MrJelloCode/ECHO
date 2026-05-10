import javax.swing.*;
import java.awt.*;

import panels.AddWorkPanel;
import panels.DashboardPanel;
import panels.LoginPanel;
import panels.ViewWorkPanel;

public class MainFrame extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private AuthService authService;

    private LoginPanel loginPanel;
    private DashboardPanel dashboardPanel;
    private AddWorkPanel addWorkPanel;
    private ViewWorkPanel viewWorkPanel;

    public MainFrame(){
        authService = new AuthService();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this, authService);
        dashboardPanel = new DashboardPanel(this, authService);
        addWorkPanel = new AddWorkPanel(this, authService);
        viewWorkPanel = new ViewWorkPanel(this, authService);

        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(dashboardPanel, "DASHBOARD");
        mainPanel.add(addWorkPanel, "ADD_WORK");
        mainPanel.add(viewWorkPanel, "VIEW_WORK");

        add(mainPanel);

        setTitle("ECHO");

        setSize(1000, 700);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        setResizable(false);

        showPanel("LOGIN");

        setVisible(true);
    }
    public void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
    public AuthService getAuthService() {
        return authService;
    }
}
