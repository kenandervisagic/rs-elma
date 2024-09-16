package org.rs.util;

import org.rs.GUI.*;
import org.rs.entity.Event;
import org.rs.entity.User;

import javax.swing.*;

public class WindowHandler {

    public static void create_window_home(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            HomePanel homePanelPage = new HomePanel(oldFrame);
            oldFrame.setContentPane(homePanelPage.pocetnaPanel);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_admin(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            AdministratorPanel admin = new AdministratorPanel(oldFrame);
            oldFrame.setContentPane(admin.admin_panel);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_login(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            LoginPanel loginPanelForm = new LoginPanel(oldFrame);
            oldFrame.setContentPane(loginPanelForm.panel_login);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_unesi_korisnika(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            UserInputPanel unesiKorisnika = new UserInputPanel(oldFrame);
            oldFrame.setContentPane(unesiKorisnika.unesi_korisnika);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_pregled_zahtjeva(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            ReviewUserRequestsPanel pregledZahtjeva = new ReviewUserRequestsPanel(oldFrame);
            oldFrame.setContentPane(pregledZahtjeva.panel_zahtjevi);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_pregled_zahtjeva_org(User user, JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            ReviewEventRequestsOrgPanel pregledZahtjeva = new ReviewEventRequestsOrgPanel(user, oldFrame);
            oldFrame.setContentPane(pregledZahtjeva.panel_zahtjevi);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_unesi_mjesto(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            PlaceInputPanel unesiMjesto = new PlaceInputPanel(oldFrame);
            oldFrame.setContentPane(unesiMjesto.unesi_mjesto);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_unesi_lokaciju(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            LocationInputPanel unesiLokaciju = new LocationInputPanel(oldFrame);
            oldFrame.setContentPane(unesiLokaciju.unesi_lokaciju);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_pregled_zahtjeva_eventa(JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            ReviewEventRequestsPanel pregledZahtjevaEventa = new ReviewEventRequestsPanel(oldFrame);
            oldFrame.setContentPane(pregledZahtjevaEventa.panel_zahtjevi);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_user(JFrame oldFrame, User user) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            HomePanelUserSignedIn userPanel = new HomePanelUserSignedIn(oldFrame, user);
            oldFrame.setContentPane(userPanel.pocetnaPanel);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_korpa(JFrame oldFrame, User user) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            KorpaPanel korpaPanel = new KorpaPanel(oldFrame,user);
            oldFrame.setContentPane(korpaPanel.pocetnaPanel);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

    public static void create_window_pregled_eventa(JFrame oldFrame, User user, Event event) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            EventDetailsPanel EventDetailsPanel = new EventDetailsPanel(oldFrame, user, event);
            oldFrame.setContentPane(EventDetailsPanel.panel_login);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }
    public static void create_window_organizer(User user, JFrame oldFrame) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            organizator organizator = new organizator(user, oldFrame);
            oldFrame.setContentPane(organizator.organizator_panel);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }
    public static void create_window_unesi_događaj(User user, JFrame oldFrame, Event selectedRequest) {
        SwingUtilities.invokeLater(() -> {
            oldFrame.getContentPane().removeAll();
            EventInputPanel eventInputPanel = new EventInputPanel(user, oldFrame, selectedRequest);
            oldFrame.setContentPane(eventInputPanel.unesi_lokaciju);
            oldFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            oldFrame.pack();
            oldFrame.setVisible(true);
        });
    }

}
