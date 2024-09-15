package org.rs.GUI;

import org.rs.DAO.EventDAO;
import org.rs.DAO.LocationDAO;
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
import java.util.List;

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
    private JButton konfigurisiKarteButton;
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
    public JFrame oldFrame;

    public User user;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public EventInputPanel(User user, JFrame oldFrame, EventRequest selectedRequest) {
        this.oldFrame = oldFrame;
        this.user = user;

        JCheckBox[] checkboxes = {checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6};
        for (JCheckBox checkbox : checkboxes) {
            checkbox.setVisible(false);
        }
// Add sectorsPanel to your main panel or frame
        if (selectedRequest != null) {
            konfigurisiKarteButton.setEnabled(false);
        }

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchSubCategories((String) comboBox1.getSelectedItem());
            }
        });

        List<Location> locations = LocationDAO.findAllLocations();
        DefaultComboBoxModel<String> modelL = new DefaultComboBoxModel<>();
        for (Location location : locations) {
            modelL.addElement(location.getLocationName());
        }
        comboBox3.setModel(modelL);

        List<EventCategory> categories = EventDAO.getAllCategories();
        DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<>();
        model1.addElement("All"); // Add "All" option
        for (EventCategory eventCategory : categories) {
            model1.addElement(eventCategory.getCategoryName());
        }
        comboBox1.setModel(model1);

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
                    updateSectorCheckboxes(location);
                }
            }
        });

        if (selectedRequest != null) {
            // Prepopulate fields with data from selectedRequest
            naziv.setText(selectedRequest.getEventName());
            opis.setText(selectedRequest.getDescription());
            datum.setText(selectedRequest.getEventDate().format(DATE_FORMATTER));
            vrijeme.setText(selectedRequest.getEventTime().format(TIME_FORMATTER));
            maxKarata.setText(String.valueOf(selectedRequest.getMaxTickets()));

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
                comboBox3.setSelectedItem(selectedRequest.getLocationEntity().getLocationName());
            }
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
                int capacity = Integer.parseInt(maxKarata.getText()); // Assuming capacity is an integer

                // Fetch category and subcategory entities
                Location location = LocationDAO.getLocationByName(selectedLocationName);
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
                    // Create a new EventRequest object
                    EventRequest event = new EventRequest();
                    event.setEventName(eventName);
                    event.setDescription(description);
                    event.setEventDate(eventDate);
                    event.setEventTime(eventTime);
                    event.setMaxTickets(capacity);
                    event.setCategory(category1);
                    event.setSubCategory(subCategory1);
                    event.setLocationEntity(location);
                    event.setOrganizer(user);

                    // Persist the new event
                    boolean success = EventDAO.saveEventRequest(event);

                    if (success) {
                        JOptionPane.showMessageDialog(null, "Event created successfully!");
                        // Optionally reset the form fields or navigate to another screen
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to create event.");
                    }
                } else {
                    // Update the existing EventRequest object
                    selectedRequest.setEventName(eventName);
                    selectedRequest.setDescription(description);
                    selectedRequest.setEventDate(eventDate);
                    selectedRequest.setEventTime(eventTime);
                    selectedRequest.setMaxTickets(capacity);
                    selectedRequest.setCategory(category1);
                    selectedRequest.setSubCategory(subCategory1);
                    selectedRequest.setLocationEntity(location);
                    selectedRequest.setOrganizer(user);

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

        konfigurisiKarteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                String selectedLocationName = (String) comboBox3.getSelectedItem();
//                Location location = LocationDAO.getLocationByName(selectedLocationName);
//
//                if (location == null) {
//                    JOptionPane.showMessageDialog(null, "Please select a valid location.");
//                    return;
//                }
//
//                // Fetch selected sectors
//                List<Sector> sectors = SectorDAO.getSectorsForLocation(location.getId());
//                JCheckBox[] checkboxes = {checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6};
//
//                int totalCapacity = 0;
//                for (int i = 0; i < sectors.size(); i++) {
//                    if (checkboxes[i].isSelected()) {
//                        totalCapacity += sectors.get(i).getCapacity();
//                    }
//                }
//
//                if (totalCapacity <= 0) {
//                    JOptionPane.showMessageDialog(null, "Please select at least one sector.");
//                    return;
//                }
//
//                // Create tickets based on total capacity
//                boolean success = createTicketsForEvent(selectedRequest, totalCapacity);
//
//                if (success) {
//                    JOptionPane.showMessageDialog(null, totalCapacity + " tickets created successfully!");
//                } else {
//                    JOptionPane.showMessageDialog(null, "Failed to create tickets.");
//                }
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

    private void updateSectorCheckboxes(Location location) {
        // Ensure sectorsPanel is not null
        if (sectorsPanel == null) {
            System.err.println("sectorsPanel is not initialized.");
            return;
        }

        // Retrieve sectors for the given location
        List<Sector> sectors = SectorDAO.getSectorsForLocation(location.getId());

        // Array of checkboxes to manage
        JCheckBox[] checkboxes = {checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6};

        // Update checkbox titles and visibility based on the sectors
        for (int i = 0; i < checkboxes.length; i++) {
            if (i < sectors.size()) {
                // Set the checkbox title to the sector name
                checkboxes[i].setText(sectors.get(i).getSectorName() + " (" + sectors.get(i).getCapacity() + ")");
                checkboxes[i].setVisible(true);
            } else {
                // Hide the checkbox if there are fewer sectors than checkboxes
                checkboxes[i].setVisible(false);
            }
        }

        // Refresh the layout to reflect the changes
        sectorsPanel.revalidate();
        sectorsPanel.repaint();
    }
//    private boolean createTicketsForEvent(Event event, int totalCapacity) {
//        try {
//            for (int i = 1; i <= totalCapacity; i++) {
//                Ticket ticket = new Ticket();
//                ticket.setEvent(event);
//                ticket.setSeatNumber(i);
//                ticket.setStatus(3);
//                ticket.setPrice(Double.valueOf(priceField1.getText()));
//                ticket.set
//
//
//                // Save ticket to database
//                boolean success = EventDAO.saveTicket(ticket);
//
//                if (!success) {
//                    return false; // Return false if any ticket fails to be saved
//                }
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

}
