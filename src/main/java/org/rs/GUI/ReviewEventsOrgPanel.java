package org.rs.GUI;

import org.rs.DAO.EventDAO;
import org.rs.entity.Event;
import org.rs.entity.User;
import org.rs.util.WindowHandler;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ReviewEventsOrgPanel {
    private JList<String> list1;
    private JButton odbijButton;
    private JButton prihvatiButton;
    private JButton nazadButton;
    public JPanel panel_zahtjevi;
    private JList<String> list2;
    private List<Event> activeRequests;
    private List<Event> inactiveRequests;
    private Event selectedRequest;
    private int selectedIndex;
    public User user;

    public ReviewEventsOrgPanel(User user, JFrame oldFrame) {
        this.user = user;

        // Fetch events for list1 and list2
        activeRequests = EventDAO.getActiveApprovedEvents();
        inactiveRequests = EventDAO.getInactiveApprovedEvents();

        // Setup list1
        DefaultListModel<String> activeListModel = new DefaultListModel<>();
        for (Event request : activeRequests) {
            activeListModel.addElement(request.toString());
        }
        list1.setModel(activeListModel);

        // Setup list2
        DefaultListModel<String> inactiveListModel = new DefaultListModel<>();
        for (Event request : inactiveRequests) {
            inactiveListModel.addElement(request.toString());
        }
        list2.setModel(inactiveListModel);

        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_organizer(user, oldFrame);
            }
        });

        odbijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedRequest != null && list1.getSelectedIndex() != -1) {
                    EventDAO.deactivateEvent(selectedRequest);
                    // Update the lists
                    refreshLists();
                    selectedRequest = null;
                }
            }
        });

        prihvatiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedRequest != null) {
                    WindowHandler.create_window_unesi_događaj(user, oldFrame, selectedRequest);
                }
            }
        });

        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                int selectedIndex = list1.getSelectedIndex();
                if (selectedIndex != -1) {
                    selectedRequest = activeRequests.get(selectedIndex);
                }
            }
        });

        list2.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                int selectedIndex = list2.getSelectedIndex();
                if (selectedIndex != -1) {
                    selectedRequest = inactiveRequests.get(selectedIndex);
                }
            }
        });
    }

    private void refreshLists() {
        // Refresh the lists
        activeRequests = EventDAO.getActiveApprovedEvents();
        inactiveRequests = EventDAO.getInactiveApprovedEvents();

        DefaultListModel<String> activeListModel = new DefaultListModel<>();
        for (Event request : activeRequests) {
            activeListModel.addElement(request.toString());
        }
        list1.setModel(activeListModel);

        DefaultListModel<String> inactiveListModel = new DefaultListModel<>();
        for (Event request : inactiveRequests) {
            inactiveListModel.addElement(request.toString());
        }
        list2.setModel(inactiveListModel);
    }
}
