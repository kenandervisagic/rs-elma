package org.rs.GUI;

import org.rs.entity.User;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class korisnik {
    private JButton unesiLokacijuButton;
    private JButton PregledZahtjevaButton;
    private JButton nazadButton;
    public JPanel userPanel;
    private JButton pregledDogadjajaButton;
    private User user;

    public korisnik(JFrame oldFrame, User user) {
        this.user = user;
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_login(oldFrame);
            }
        });
        PregledZahtjevaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_pregled_zahtjeva(oldFrame);

            }
        });
        unesiLokacijuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_unesi_lokaciju(oldFrame);
            }
        });
        pregledDogadjajaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               WindowHandler.create_window_unesi_mjesto(oldFrame);
            }
        });
    }
}
