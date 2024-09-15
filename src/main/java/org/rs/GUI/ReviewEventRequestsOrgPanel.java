package org.rs.GUI;


import org.rs.DAO.EventDAO;
import org.rs.entity.EventRequest;
import org.rs.entity.User;
import org.rs.util.WindowHandler;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ReviewEventRequestsOrgPanel {
    private JList<String> list1;
    private JButton odbijButton;
    private JButton prihvatiButton;
    private JButton nazadButton;
    public JPanel panel_zahtjevi;
    private List<EventRequest> requests;
    private EventRequest selectedRequest;
    private int selectedIndex;
    public User user;
    public ReviewEventRequestsOrgPanel(User user, JFrame oldFrame) {
        this.user = user;
        requests = EventDAO.getAllEventRequest();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (EventRequest request : requests) {
            listModel.addElement(request.toString());
        }
        list1.setModel(listModel); // Ensure the JList uses the DefaultListModel

        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_organizer(user, oldFrame);
            }
        });
        odbijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedRequest != null) {
                    EventDAO.rejectRequest(selectedRequest);
                    listModel.remove(selectedIndex); // Remove the selected item from the list
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
                    selectedRequest = requests.get(selectedIndex);
                }
            }
        });
    }
}
