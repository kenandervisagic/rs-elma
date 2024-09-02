package org.rs.GUI;


import org.rs.DAO.UserDAO;
import org.rs.entity.UserRequest;
import org.rs.util.WindowHandler;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ReviewUserRequestsPanel {
    private JList<String> list1;
    private JButton odbijButton;
    private JButton prihvatiButton;
    private JButton nazadButton;
    public JPanel panel_zahtjevi;
    private List<UserRequest> requests;
    private UserRequest selectedRequest;
    private int selectedIndex;

    public ReviewUserRequestsPanel(JFrame oldFrame) {
        requests = UserDAO.getAllRequests();

        DefaultListModel<String> listModel = new DefaultListModel<>();


        for (UserRequest request : requests) {
            listModel.addElement(request.getId() + ": " + request.getUsername() + " (" + request.getRole() + ")");
        }

        list1.setModel(listModel); // Ensure the JList uses the DefaultListModel

        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_admin(oldFrame);
            }
        });
        odbijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedRequest != null) {
                    UserDAO.rejectRequest(selectedRequest);
                    listModel.remove(selectedIndex); // Remove the selected item from the list
                    selectedRequest = null;
                }
            }
        });
        prihvatiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedRequest != null) {
                    UserDAO.approveRequest(selectedRequest);
                    listModel.remove(selectedIndex); // Remove the selected item from the list
                    selectedRequest = null;
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
