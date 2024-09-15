package org.rs.GUI;

import org.rs.DAO.EventDAO;
import org.rs.entity.Event;
import org.rs.entity.EventCategory;
import org.rs.entity.EventSubCategory;
import org.rs.entity.User;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class HomePanelUserSignedIn {
    private JTextField textField1;
    private JButton traziButton;
    private JButton pregledajButton;
    private JButton pregledajButton3;
    private JButton pregledajButton2;
    private JButton pregledajButton1;
    private JLabel event1Label1;
    private JLabel event1Label2;
    private JLabel event1Label3;
    private JLabel event2Label1;
    private JLabel event2Label2;
    private JLabel event2Label3;
    private JLabel event3Label1;
    private JLabel event3Label2;
    private JLabel event3Label3;
    private JLabel event4Label1;
    private JLabel event4Label2;
    private JLabel event4Label3;
    public JPanel pocetnaPanel;
    private JButton prethodnaButton;
    private JButton sljedecaButton;
    private JPanel event2Panel;
    private JPanel event4Panel;
    private JPanel event1Panel;
    private JPanel event3Panel;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JButton primijeniButton;
    private JComboBox comboBox3;
    private JLabel welcomeLabel;
    private JButton odjavaButton;
    private JButton korpaButton;
    private JLabel balanceLabel;
    private JButton profilButton;
    private JButton rezervacijeButton;
    private int currentPage = 0;
    private int pageSize = 4;
    private int totalEvents;
    private List<Event> displayedEvents;
    private String selectedCategory;
    private String selectedSubCategory;
    public JFrame oldFrame;
    public User user;


    public HomePanelUserSignedIn(JFrame oldFrame, User user) {
        this.oldFrame = oldFrame;
        this.user = user;
        welcomeLabel.setText("Dobrodosli " + user.getFullName());
        balanceLabel.setText("Balance: " + user.getBalance() + "KM");
        korpaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_korpa(oldFrame, user);
            }
        });
        odjavaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_login(oldFrame);

            }
        });
        DefaultComboBoxModel<String> sortingModel = new DefaultComboBoxModel<>();
        sortingModel.addElement("Naziv");
        sortingModel.addElement("Vrsta");
        sortingModel.addElement("Podvrsta");
        sortingModel.addElement("Lokacija");
        sortingModel.addElement("Mjesto");
        comboBox3.setModel(sortingModel);
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

        DefaultComboBoxModel<String> model2 = new DefaultComboBoxModel<>();
        model2.addElement("All"); // Add "All" option
        comboBox2.setModel(model2);

        traziButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchCriterion = (String) comboBox3.getSelectedItem();
                String searchValue = textField1.getText(); // Assuming textField1 is used for the search input

                // Fetch events based on the selected search criterion
                List<Event> events = EventDAO.searchEventsByCriterion(searchCriterion, searchValue, currentPage, pageSize);

                // Calculate the total number of filtered events
                totalEvents = EventDAO.getTotalNumberOfFilteredEventsByCriterion(searchCriterion, searchValue);

                // Update the UI with the filtered events
                updateEventDisplay(events);

                // Enable or disable navigation buttons based on the page
                prethodnaButton.setEnabled(currentPage > 0);
                sljedecaButton.setEnabled((currentPage + 1) * pageSize < totalEvents);
            }
        });
        primijeniButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected category and subcategory
                selectedCategory = (String) comboBox1.getSelectedItem();
                selectedSubCategory = (String) comboBox2.getSelectedItem();

                // If "All" is selected, set to null or empty string
                if ("All".equals(selectedCategory)) {
                    selectedCategory = null;
                }
                if ("All".equals(selectedSubCategory)) {
                    selectedSubCategory = null;
                }
                currentPage = 0;
                // Fetch events based on selected filters
                List<Event> events = EventDAO.getEventsByFilters(selectedCategory, selectedSubCategory, currentPage, pageSize);

                // Calculate the total number of filtered events
                totalEvents = EventDAO.getTotalNumberOfFilteredEvents(selectedCategory, selectedSubCategory);

                // Update the UI with the filtered events
                updateEventDisplay(events);

                // Enable or disable navigation buttons based on the page
                prethodnaButton.setEnabled(currentPage > 0);
                sljedecaButton.setEnabled((currentPage + 1) * pageSize < totalEvents);
            }
        });
        prethodnaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 0) {
                    currentPage--;
                    fetchAndDisplayEvents();
                }
            }
        });


        sljedecaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((currentPage + 1) * pageSize < totalEvents) {
                    currentPage++;
                    fetchAndDisplayEvents();
                }
            }
        });

        fetchAndDisplayEvents();
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

    private void fetchAndDisplayEvents() {
        // Fetch events based on the filter criteria and current page
        List<Event> events = EventDAO.getEventsByFilters(selectedCategory, selectedSubCategory, currentPage, pageSize);

        totalEvents = EventDAO.getTotalNumberOfFilteredEvents(selectedCategory, selectedSubCategory);
        // Update the UI with the filtered events
        updateEventDisplay(events);

        // Enable or disable navigation buttons based on the page
        prethodnaButton.setEnabled(currentPage > 0);
        sljedecaButton.setEnabled((currentPage + 1) * pageSize < totalEvents);
    }

    private void updateEventDisplay(List<Event> events) {
        this.displayedEvents = events;

        JPanel[] eventPanels = {event1Panel, event2Panel, event3Panel, event4Panel};
        for (JPanel panel : eventPanels) {
            panel.setVisible(false);
        }

        JLabel[] eventLabels = {event1Label1, event1Label2, event1Label3,
                event2Label1, event2Label2, event2Label3,
                event3Label1, event3Label2, event3Label3,
                event4Label1, event4Label2, event4Label3};

        JButton[] pregledajButtons = {pregledajButton, pregledajButton1, pregledajButton2, pregledajButton3};

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            eventPanels[i].setVisible(true);
            eventLabels[i * 3].setText(event.getEventName());
            eventLabels[i * 3 + 1].setText(event.getLocationEntity().getLocationName());
            eventLabels[i * 3 + 2].setText(event.getEventDate().toString());

            int finalI = i; // Need to make 'i' final or effectively final to use in lambda
            pregledajButtons[i].setVisible(true);
            pregledajButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Event selectedEvent = displayedEvents.get(finalI);
                    WindowHandler.create_window_pregled_eventa(oldFrame, user, selectedEvent);
                }
            });
        }
    }

}
