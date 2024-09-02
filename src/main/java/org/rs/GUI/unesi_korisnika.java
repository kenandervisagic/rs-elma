package org.rs.GUI;

import org.rs.DAO.UserDAO;
import org.rs.entity.User;
import org.rs.entity.UserRequest;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class unesi_korisnika {
    private JButton nazadButton;
    private JButton potvrdiButton;
    public JPanel unesi_korisnika;
    private JRadioButton korisnikRadioButton;
    private JRadioButton organizatorRadioButton;
    private JPasswordField passwordField1;
    private JTextField username;
    private JTextField imePrezime;
    private JTextField email;

    public unesi_korisnika(JFrame oldFrame) {
        ButtonGroup role = new ButtonGroup();
        role.add(korisnikRadioButton);
        role.add(organizatorRadioButton);
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_login(oldFrame);
            }
        });
        potvrdiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Get user input
                String name = imePrezime.getText();
                String username = unesi_korisnika.this.username.getText();
                String password = new String(passwordField1.getPassword());
                String email = unesi_korisnika.this.email.getText();

                // Validate inputs
                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }

                // Create a new user object
                String role = korisnikRadioButton.isSelected() ? "Korisnik" : "Organizator";
                UserRequest userRequest = new UserRequest(name, username, password, email, role);

                // Save user to the database
                try {
                    UserDAO.addUserRequest(userRequest);
                    JOptionPane.showMessageDialog(null, "User added successfully.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error adding user: " + e.getMessage());
                }
            }
        });
    }

}
