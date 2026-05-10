

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel(MainFrame frame, AuthService authService) {

        setLayout(new BorderLayout());

        add(new JLabel("Dashboard", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}