package org.rs.GUI;

import org.rs.DAO.UserDAO;
import org.rs.entity.Place;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class unesi_mjesto {
    private JTextField textField1;
    private JButton nazadButton;
    private JButton potvrdiButton;
    public JPanel unesi_mjesto;

    public unesi_mjesto(JFrame oldFrame) {
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_admin(oldFrame);
            }
        });
        potvrdiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Place place = new Place();
                place.setLocationName(textField1.getText());
                try {
                    UserDAO.addPlace(place);
                    JOptionPane.showMessageDialog(null, "Place added successfully.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error adding place: " + e.getMessage());
                }
                WindowHandler.create_window_admin(oldFrame);
            }
        });
    }
}
