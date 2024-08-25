package org.rs.GUI;

import org.rs.DAO.UserDAO;
import org.rs.entity.Location;
import org.rs.entity.Place;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class unesi_lokaciju {
    private JButton nazadButton;
    private JButton potvrdiButton;
    public JPanel unesi_lokaciju;
    private JTextField lokacija;
    private JTextField kapacitet;
    private JTextField tipLokacije;
    private JComboBox comboBox1;

    public unesi_lokaciju(JFrame oldFrame) {
        List<Place> places = UserDAO.getAllPlaces();

        // Populate JComboBox with place names
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Place place : places) {
            model.addElement(place.getLocationName());
        }
        comboBox1.setModel(model);
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_admin(oldFrame);
            }
        });
        potvrdiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String selectedPlaceName = (String) comboBox1.getSelectedItem();
                String capacityStr = kapacitet.getText();
                String locationType = tipLokacije.getText();

                // Validate input
                if (selectedPlaceName == null || capacityStr.isEmpty() || locationType.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                    return;
                }

                int capacity;
                try {
                    capacity = Integer.parseInt(capacityStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Capacity must be a number.");
                    return;
                }

                // Retrieve Place entity based on selected name
                Place selectedPlace = places.stream()
                        .filter(p -> p.getLocationName().equals(selectedPlaceName))
                        .findFirst()
                        .orElse(null);

                if (selectedPlace == null) {
                    JOptionPane.showMessageDialog(null, "Selected place not found.");
                    return;
                }

                // Create new Location object
                Location newLocation = new Location();
                newLocation.setCapacity(capacity);
                newLocation.setType(locationType);
                newLocation.setPlace(selectedPlace);
                newLocation.setLocationName(lokacija.getText());

                // Add location to database
                UserDAO.addLocation(newLocation);

                // Optionally, show a confirmation message
                JOptionPane.showMessageDialog(null, "Location added successfully.");

                // Clear input fields
                kapacitet.setText("");
                tipLokacije.setText("");
                comboBox1.setSelectedIndex(-1); // Clear the selection
            }
        });
    }

}
