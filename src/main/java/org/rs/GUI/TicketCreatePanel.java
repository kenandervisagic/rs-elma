package org.rs.GUI;

import org.rs.DAO.SectorDAO;
import org.rs.entity.Sector;
import org.rs.entity.User;
import org.rs.entity.Event;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TicketCreatePanel {
    public JPanel unesi_korisnika;
    private JPanel sectorsPanel; // This panel will hold dynamically created checkboxes
    private JButton potvrdiButton;
    private JButton nazadButton;

    public TicketCreatePanel(User user, JFrame oldFrame, Event eventRequest) {
        // Initialize the sectorsPanel
        sectorsPanel = new JPanel();
        sectorsPanel.setLayout(new BoxLayout(sectorsPanel, BoxLayout.Y_AXIS));
        unesi_korisnika.add(sectorsPanel, BorderLayout.CENTER);

        // Fetch and display sectors
        List<Sector> sectors = SectorDAO.getSectorsForLocation(eventRequest.getLocationEntity().getId());
        for (Sector sector : sectors) {
            JCheckBox checkBox = new JCheckBox(sector.getSectorName());
            sectorsPanel.add(checkBox);
        }

        // Optionally add a scroll pane if the list is long
        JScrollPane scrollPane = new JScrollPane(sectorsPanel);
        unesi_korisnika.add(scrollPane, BorderLayout.CENTER);

        // Example ActionListener for a checkbox
        for (Component component : sectorsPanel.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Handle checkbox action here
                        System.out.println("Checkbox " + checkBox.getText() + " selected: " + checkBox.isSelected());
                    }
                });
            }
        }
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_unesi_događaj(user, oldFrame, eventRequest);
            }
        });
    }
}
