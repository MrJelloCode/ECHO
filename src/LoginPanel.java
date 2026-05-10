

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {

    private MainFrame frame;

    private AuthService authService;

    private JLabel titleLabel;

    private JLabel usernameLabel;

    private JTextField usernameField;

    private JButton loginButton;

    // DEFAULT CONSTRUCTOR

    public LoginPanel(MainFrame frame, AuthService authService) {

        this.frame = frame;

        this.authService = authService;

        setLayout(null);

        setBackground(Color.WHITE);

        titleLabel = new JLabel("ECHO");

        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));

        titleLabel.setBounds(430, 100, 200, 50);

        add(titleLabel);

        usernameLabel = new JLabel("Username:");

        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        usernameLabel.setBounds(330, 250, 120, 30);

        add(usernameLabel);

        usernameField = new JTextField();

        usernameField.setBounds(430, 250, 220, 30);

        add(usernameField);

        loginButton = new JButton("Login");

        loginButton.setBounds(430, 320, 120, 35);

        add(loginButton);

        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                String username = usernameField.getText().trim();

                if (username.length() == 0) {

                    JOptionPane.showMessageDialog(frame,
                            "Please enter a username.");

                    return;
                }

                // Store user info

                authService.setCurrentUsername(username);

                authService.setCurrentAI(new AI());

                authService.setLoggedIn(true);

                // SWITCH TO DASHBOARD

                frame.showPanel("DASHBOARD");
            }
        });
    }
}