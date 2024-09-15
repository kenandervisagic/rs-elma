package org.rs.GUI;

import org.rs.entity.User;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class organizator {
    public JPanel organizator_panel;
    private JButton pregledDogađajaButton;
    private JButton PregledZahtjevaButton;
    private JButton nazadButton;
    private JButton unosDogađajaButton;
    public User user;

    public organizator(User user, JFrame oldFrame) {
        this.user = user;
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_login(oldFrame);
            }
        });
        unosDogađajaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_unesi_događaj(user, oldFrame);
            }
        });
        pregledDogađajaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_unesi_mjesto(oldFrame);
            }
        });
    }
}
