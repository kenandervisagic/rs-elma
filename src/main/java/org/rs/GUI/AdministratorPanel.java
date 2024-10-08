package org.rs.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.rs.util.WindowHandler;

public class AdministratorPanel {
    private JButton unesiLokacijuButton;
    private JButton PregledZahtjevaButton;
    private JButton nazadButton;
    public JPanel admin_panel;
    private JButton unosMjestaButton;
    private JButton pregledZahtjevaEventa;

    public AdministratorPanel(JFrame oldFrame) {
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
        pregledZahtjevaEventa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            WindowHandler.create_window_pregled_zahtjeva_eventa(oldFrame);
            }
        });
        unesiLokacijuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_unesi_lokaciju(oldFrame);
            }
        });
        unosMjestaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
               WindowHandler.create_window_unesi_mjesto(oldFrame);
            }
        });
    }
}
