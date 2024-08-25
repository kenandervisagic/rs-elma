package org.rs.util;

import org.rs.GUI.*;

import javax.swing.*;

public class WindowHandler {

    public static void create_window_admin(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            administrator admin = new administrator(oldFrame);
            oldFrame.setContentPane(admin.admin_panel);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }
    public static void create_window_login(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            login loginForm = new login(oldFrame);
            oldFrame.setContentPane(loginForm.panel_login);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
            SwingUtilities.invokeLater(() -> {
                oldFrame.getContentPane().removeAll();
                login loginForm = new login(oldFrame);
                oldFrame.setContentPane(loginForm.panel_login);
                oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                oldFrame.pack();
                oldFrame.setVisible(true);
            });
    }

    public static void create_window_unesi_korisnika(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            unesi_korisnika unesiKorisnika = new unesi_korisnika(oldFrame);
            oldFrame.setContentPane(unesiKorisnika.unesi_korisnika);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_pregled_zahtjeva(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            pregled_zahtjeva pregledZahtjeva = new pregled_zahtjeva(oldFrame);
            oldFrame.setContentPane(pregledZahtjeva.panel_zahtjevi);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_unesi_mjesto(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            unesi_mjesto unesiMjesto = new unesi_mjesto(oldFrame);
            oldFrame.setContentPane(unesiMjesto.unesi_mjesto);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_unesi_lokaciju(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            unesi_lokaciju unesiLokaciju = new unesi_lokaciju(oldFrame);
            oldFrame.setContentPane(unesiLokaciju.unesi_lokaciju);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }
}
