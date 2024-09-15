package org.rs.GUI;

import org.rs.DAO.EventDAO;
import org.rs.DAO.LocationDAO;
import org.rs.entity.*;

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
    private JComboBox comboBox1;
    private JButton unosSektoraButton;
    private JTextField nazivSektora;
    private JTextField maxKarata;
    private JList<String> list1;
    private JTextField datum;
    private JTextField vrijeme;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    public JFrame oldFrame;

    public User user;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public EventInputPanel(User user, JFrame oldFrame) {
        this.oldFrame = oldFrame;
        this.user = user;

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

                // Fetch location entity
                Location location = LocationDAO.getLocationByName(selectedLocationName);
                EventCategory category1 = EventDAO.getCategoryByName(selectedCategory);
                EventSubCategory subCategory1 = EventDAO.getSubCategoryByName(selectedSubCategory);

                // Create a new Event object
                EventRequest event = new EventRequest();
                try {
                    LocalDate eventDate = LocalDate.parse(datum.getText(), DATE_FORMATTER);
                    LocalTime eventTime = LocalTime.parse(vrijeme.getText(), TIME_FORMATTER);
                    event.setEventDate(eventDate);
                    event.setEventTime(eventTime);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Invalid date or time format. Please use dd-MM-yyyy for date and HH:mm for time.");
                    return; // Exit if date or time parsing fails
                }
                event.setEventName(eventName);
                event.setDescription(description);
                event.setMaxTickets(capacity);
                event.setCategory(category1);
                event.setSubCategory(subCategory1);
                event.setLocationEntity(location);
                event.setOrganizer(user);

                // Persist the event
                boolean success = EventDAO.saveEventRequest(event);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Event created successfully!");
                    // Optionally reset the form fields or navigate to another screen
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to create event.");
                }
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
}
