package org.rs.GUI;

import org.rs.DAO.PlaceDAO;
import org.rs.DAO.UserDAO;
import org.rs.entity.Location;
import org.rs.entity.Place;
import org.rs.entity.Sector;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocationInputPanel {
    private JButton nazadButton;
    private JButton potvrdiButton;
    public JPanel unesi_lokaciju;
    private JTextField lokacija;
    private JTextField kapacitet;
    private JComboBox comboBox1;
    private JButton unosSektoraButton;
    private JTextField nazivSektora;
    private JTextField kapacitetSektora;
    private JList<String> list1;
    private Set<Sector> sectors = new HashSet<>();
    private DefaultListModel<String> sectorListModel = new DefaultListModel<>();

    public LocationInputPanel(JFrame oldFrame) {
        List<Place> places = PlaceDAO.getAllPlaces();

        // Populate JComboBox with place names
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Place place : places) {
            model.addElement(place.getLocationName());
        }
        comboBox1.setModel(model);

        // Set the model for the JList
        list1.setModel(sectorListModel);

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

                // Validate input
                if (selectedPlaceName == null || capacityStr.isEmpty() ||sectors.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in all fields and add at least one sector.");
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
                newLocation.setPlace(selectedPlace);
                newLocation.setLocationName(lokacija.getText());

                // Link sectors to this location
                for (Sector sector : sectors) {
                    sector.setLocation(newLocation);
                }

                // Add location and sectors to database
                UserDAO.addLocationWithSectors(newLocation, sectors);

                // Show a confirmation message
                JOptionPane.showMessageDialog(null, "Location and sectors added successfully.");

                // Clear input fields
                lokacija.setText("");
                kapacitet.setText("");
                nazivSektora.setText("");
                kapacitetSektora.setText("");
                sectorListModel.clear();  // Clear the sector list display
                comboBox1.setSelectedIndex(-1); // Clear the selection
                sectors.clear(); // Clear the sectors set
            }
        });

        unosSektoraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sectorName = nazivSektora.getText();
                String sectorCapacityStr = kapacitetSektora.getText();

                if (sectorName.isEmpty() || sectorCapacityStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill in both sector name and capacity.");
                    return;
                }

                int sectorCapacity;
                try {
                    sectorCapacity = Integer.parseInt(sectorCapacityStr);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Sector capacity must be a number.");
                    return;
                }

                // Create a new Sector object
                Sector sector = new Sector();
                sector.setSectorName(sectorName);
                sector.setCapacity(sectorCapacity);

                // Add the sector to the set and update the JList
                sectors.add(sector);
                sectorListModel.addElement(sectorName + " (" + sectorCapacity + ")");

                // Clear the sector input fields
                nazivSektora.setText("");
                kapacitetSektora.setText("");
            }
        });
    }
}
