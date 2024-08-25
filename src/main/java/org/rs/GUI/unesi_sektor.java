package org.rs.GUI;

import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class unesi_sektor {
    private JTextField ime;
    private JTextField kapacitet;
    private JButton nazadButton;
    private JButton potvrdiButton;
    public JPanel unesi_sektor;

    public unesi_sektor(JFrame oldFrame) {
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_unesi_lokaciju(oldFrame);
            }
        });
        potvrdiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }
}
