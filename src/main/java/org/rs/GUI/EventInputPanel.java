package org.rs.GUI;

import org.rs.DAO.EventDAO;
import org.rs.DAO.LocationDAO;
import org.rs.DAO.PlaceDAO;
import org.rs.DAO.SectorDAO;
import org.rs.entity.*;
import org.rs.entity.Event;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventInputPanel {
    private JButton nazadButton;
    private JButton potvrdiButton;
    public JPanel unesi_lokaciju;
    private JTextField naziv;
    private JTextField opis;
    private JComboBox<String> comboBox1;
    private JTextField maxKarata;
    private JTextField datum;
    private JTextField vrijeme;
    private JComboBox<String> comboBox2;
    private JComboBox<String> comboBox3;
    private JPanel sectorsPanel;  // Panel to hold checkboxes for sectors
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JCheckBox checkBox4;
    private JCheckBox checkBox5;
    private JCheckBox checkBox6;
    private JTextField priceField1;
    private JRadioButton dozvoljenoRadioButton;
    private JRadioButton zabranjenoRadioButton;
    private JComboBox comboBox4;
    private JTextField textField1;
    public JFrame oldFrame;

    public User user;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public EventInputPanel(User user, JFrame oldFrame, Event selectedRequest) {
        this.oldFrame = oldFrame;
        this.user = user;
        ButtonGroup cancelPolicyGroup = new ButtonGroup();
        cancelPolicyGroup.add(dozvoljenoRadioButton);
        cancelPolicyGroup.add(zabranjenoRadioButton);
        JCheckBox[] checkboxes = {checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6};
        for (JCheckBox checkbox : checkboxes) {
            checkbox.setVisible(false);
        }

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchSubCategories((String) comboBox1.getSelectedItem());
            }
        });


        List<EventCategory> categories = EventDAO.getAllCategories();
        DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<>();
        model1.addElement("All"); // Add "All" option
        for (EventCategory eventCategory : categories) {
            model1.addElement(eventCategory.getCategoryName());
        }
        comboBox1.setModel(model1);

        List<Place> places = PlaceDAO.getAllPlaces();
        DefaultComboBoxModel<String> model4 = new DefaultComboBoxModel<>();
        for (Place place : places) {
            model4.addElement(place.getLocationName());
        }
        comboBox4.setModel(model4);


        DefaultComboBoxModel<String> modelL = new DefaultComboBoxModel<>();
        modelL.addElement("");
        comboBox3.setModel(modelL);

        comboBox4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Place selectedPlace = places.get(comboBox4.getSelectedIndex());
                modelL.removeAllElements();
                List<Location> availableLocations = LocationDAO.getLocationsForPlace(selectedPlace);
                for (Location location : availableLocations) {
                    modelL.addElement(location.getLocationName());
                }
                comboBox3.setModel(modelL);
            }
        });

        DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>();
        model2.addElement("All"); // Add "All" option
        comboBox2.setModel(model2);
        // Event listener for location selection
        comboBox3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLocationName = (String) comboBox3.getSelectedItem();
                if (selectedLocationName != null) {
                    Location location = LocationDAO.getLocationByName(selectedLocationName);
                    updateSectorCheckboxes(location,selectedRequest);
                }
            }
        });

        if (selectedRequest != null) {
            // Prepopulate fields with data from selectedRequest
            naziv.setText(selectedRequest.getEventName());
            opis.setText(selectedRequest.getDescription());
            datum.setText(selectedRequest.getEventDate().format(DATE_FORMATTER));
            vrijeme.setText(selectedRequest.getEventTime().format(TIME_FORMATTER));
            priceField1.setText(String.valueOf(selectedRequest.getPrice()));
            textField1.setText(String.valueOf(selectedRequest.getMaxTicketsUser()));

            if(selectedRequest.isCancelPolicy()){
                dozvoljenoRadioButton.setSelected(true);
            }else {
                zabranjenoRadioButton.setSelected(true);
            }

            // Set selected category
            if (selectedRequest.getCategory() != null) {
                comboBox1.setSelectedItem(selectedRequest.getCategory().getCategoryName());
            }

            // Set selected subcategory
            if (selectedRequest.getSubCategory() != null) {
                comboBox2.setSelectedItem(selectedRequest.getSubCategory().getSubCategoryName());
            }

            // Set selected location
            if (selectedRequest.getLocationEntity() != null) {

                comboBox4.setSelectedItem(selectedRequest.getLocationEntity().getPlace().getLocationName());
                Place selectedPlace = selectedRequest.getLocationEntity().getPlace();
                List<Location> availableLocations = LocationDAO.getLocationsForPlace(selectedPlace);
                DefaultComboBoxModel<String> modelL1 = new DefaultComboBoxModel<>();
                for (Location location : availableLocations) {
                    modelL1.addElement(location.getLocationName());
                }
                comboBox3.setModel(modelL1);

                comboBox3.setSelectedItem(selectedRequest.getLocationEntity().getLocationName());
            }

            // Set checkbox states based on the selected sectors

        }

        potvrdiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve data from form fields
                String eventName = naziv.getText();
                String description = opis.getText();
                String selectedCategory = (String) comboBox1.getSelectedItem();
                String selectedSubCategory = (String) comboBox2.getSelectedItem();
                String selectedLocationName = (String) comboBox3.getSelectedItem();
                Location location = LocationDAO.getLocationByName(selectedLocationName);
                String priceText = priceField1.getText();
                int maxKartiUser = Integer.parseInt(textField1.getText());


                if (eventName == null || eventName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Event name cannot be empty.");
                    return;
                }
                if (description == null || description.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Event description cannot be empty.");
                    return;
                }
                if (datum.getText() == null || datum.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Event date cannot be empty.");
                    return;
                }
                if (vrijeme.getText() == null || vrijeme.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Event time cannot be empty.");
                    return;
                }
                if (priceText == null || priceText.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Price cannot be empty.");
                    return;
                }
                if (selectedCategory == null || selectedCategory.trim().isEmpty() || "All".equals(selectedCategory)) {
                    JOptionPane.showMessageDialog(null, "Please select a valid category.");
                    return;
                }
                if (selectedSubCategory == null || selectedSubCategory.trim().isEmpty() || "All".equals(selectedSubCategory)) {
                    JOptionPane.showMessageDialog(null, "Please select a valid subcategory.");
                    return;
                }
                if (selectedLocationName == null || selectedLocationName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select a valid location.");
                    return;
                }

                if (location == null) {
                    JOptionPane.showMessageDialog(null, "Please select a valid location.");
                    return;
                }

                // Fetch selected sectors
                List<Sector> sectors = SectorDAO.getSectorsForLocation(location.getId());
                Set<Sector> checkedSectors = new HashSet<>();
                int totalCapacity = 0;
                for (int i = 0; i < sectors.size(); i++) {
                    if (checkboxes[i].isSelected()) {
                        totalCapacity += sectors.get(i).getCapacity();
                        checkedSectors.add(sectors.get(i));
                    }
                }

                if (totalCapacity <= 0) {
                    JOptionPane.showMessageDialog(null, "Please select at least one sector.");
                    return;
                }

                // Fetch category and subcategory entities
                EventCategory category1 = EventDAO.getCategoryByName(selectedCategory);
                EventSubCategory subCategory1 = EventDAO.getSubCategoryByName(selectedSubCategory);

                // Parse date and time
                LocalDate eventDate;
                LocalTime eventTime;
                try {
                    eventDate = LocalDate.parse(datum.getText(), DATE_FORMATTER);
                    eventTime = LocalTime.parse(vrijeme.getText(), TIME_FORMATTER);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid date or time format. Please use dd-MM-yyyy for date and HH:mm for time.");
                    return; // Exit if date or time parsing fails
                }

                if (selectedRequest == null) {
                    // Create a new Event object
                    Event event = new Event();
                    event.setEventName(eventName);
                    event.setDescription(description);
                    event.setEventDate(eventDate);
                    event.setEventTime(eventTime);
                    event.setMaxTickets(totalCapacity);
                    event.setCategory(category1);
                    event.setSubCategory(subCategory1);
                    event.setLocationEntity(location);
                    event.setOrganizer(user);
                    event.setMaxTicketsUser(maxKartiUser);
                    event.setPrice(Double.parseDouble(priceField1.getText()));
                    event.setCancelPolicy(!dozvoljenoRadioButton.isSelected());
                    event.setSectors(checkedSectors);
                    event.setActive(true);

                    // Persist the new event
                    boolean success = EventDAO.saveEventRequest(event);

                    if (success) {
                        JOptionPane.showMessageDialog(null, "Event created successfully!");
                        // Optionally reset the form fields or navigate to another screen
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to create event.");
                    }
                } else {
                    // Update the existing Event object
                    selectedRequest.setEventName(eventName);
                    selectedRequest.setDescription(description);
                    selectedRequest.setEventDate(eventDate);
                    selectedRequest.setEventTime(eventTime);
                    selectedRequest.setMaxTickets(totalCapacity);
                    selectedRequest.setCategory(category1);
                    selectedRequest.setSubCategory(subCategory1);
                    selectedRequest.setLocationEntity(location);
                    selectedRequest.setOrganizer(user);
                    selectedRequest.setCancelPolicy(dozvoljenoRadioButton.isSelected());
                    selectedRequest.setPrice(Double.parseDouble(priceField1.getText()));
                    selectedRequest.setSectors(checkedSectors);
                    selectedRequest.setMaxTicketsUser(maxKartiUser);

                    // Update the event in the database
                    boolean success = EventDAO.updateEventRequest(selectedRequest);

                    if (success) {
                        JOptionPane.showMessageDialog(null, "Event updated successfully!");
                        // Optionally reset the form fields or navigate to another screen
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to update event.");
                    }
                }
            }
        });

        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_organizer(user, oldFrame);
            }
        });

    }

    private void fetchSubCategories(String categoryName) {
        if ("All".equals(categoryName)) {
            // Clear subcategories if "All" is selected
            DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>();
            model2.addElement("All");
            comboBox2.setModel(model2);
        } else {
            List<EventSubCategory> subCategories = EventDAO.getSubCategoriesByCategory(categoryName);
            DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>();
            model2.addElement("All");
            for (EventSubCategory subCategory : subCategories) {
                model2.addElement(subCategory.getSubCategoryName());
            }
            comboBox2.setModel(model2);
        }
    }

    private void updateSectorCheckboxes(Location location, Event selectedRequest) {
        // Ensure sectorsPanel is not null
        if (sectorsPanel == null) {
            System.err.println("sectorsPanel is not initialized.");
            return;
        }

        // Retrieve sectors for the given location
        List<Sector> sectors = SectorDAO.getSectorsForLocation(location.getId());

        // Array of checkboxes to manage
        JCheckBox[] checkboxes = {checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6};

        // Get the selected sectors from the request (if any)
        Set<Sector> selectedSectors = selectedRequest != null ? selectedRequest.getSectors() : new HashSet<>();

        // Debugging: print selected sectors
        System.out.println("Selected Sectors: " + selectedSectors);

        // Update checkbox titles, visibility, and checked state based on the sectors
        for (int i = 0; i < checkboxes.length; i++) {
            if (i < sectors.size()) {
                Sector sector = sectors.get(i);

                // Set the checkbox title to the sector name
                checkboxes[i].setText(sector.getSectorName() + " (" + sector.getCapacity() + ")");
                checkboxes[i].setVisible(true);

                // Check the checkbox based on sector ID
                boolean isSelected = selectedSectors.stream()
                        .anyMatch(selectedSector -> selectedSector.getId().equals(sector.getId()));
                checkboxes[i].setSelected(isSelected);
            } else {
                // Hide the checkbox if there are fewer sectors than checkboxes
                checkboxes[i].setVisible(false);
            }
        }

        // Refresh the layout to reflect the changes
        sectorsPanel.revalidate();
        sectorsPanel.repaint();
    }


}
