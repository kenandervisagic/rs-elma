package org.rs.GUI;

import org.rs.entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.rs.DAO.UserDAO;
import org.rs.util.WindowHandler;


public class LoginPanel {
    private JTextField textField1;
    public JPanel panel_login;
    private JButton prijaviSeButton;
    private JPasswordField passwordField1;
    private JButton registracijaButton;

    public JPasswordField getPasswordField1() {
        return passwordField1;
    }

    public JTextField getTextField1() {
        return textField1;
    }

    public LoginPanel(JFrame oldFrame) {

    prijaviSeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
           handleLogin(oldFrame);
        }
    });
    passwordField1.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            handleLogin(oldFrame);
        }
    });

        registracijaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_home(oldFrame);
            }
        });
    }

    public void handleLogin(JFrame oldFrame) {
        String username = textField1.getText();
        String password = new String(passwordField1.getPassword());
        User korisnik = UserDAO.getUserByUsernameAndPass(username, password);
        if(username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(null, "Empty field", "Login Error", JOptionPane.WARNING_MESSAGE);
            return;
        }else if (korisnik == null){
            JOptionPane.showMessageDialog(null, "User not found", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        switch (korisnik.getRole()) {
            case "Administrator":
                WindowHandler.create_window_admin(oldFrame);
                break;
            case "Korisnik":
                WindowHandler.create_window_user(oldFrame, korisnik);
                break;
            case "Nastavnik":
                break;
            case "Student":
                break;
            default:
                JOptionPane.showMessageDialog(oldFrame, "Invalid role", "Login Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

}
