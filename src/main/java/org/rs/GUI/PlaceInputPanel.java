package org.rs.GUI;

import org.rs.DAO.PlaceDAO;
import org.rs.entity.Place;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PlaceInputPanel {
    private JTextField textField1;
    private JButton nazadButton;
    private JButton potvrdiButton;
    public JPanel unesi_mjesto;
    private JList list1;

    public PlaceInputPanel(JFrame oldFrame) {
        loadPlaces();

        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                WindowHandler.create_window_admin(oldFrame);
            }
        });
        potvrdiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Place place = new Place();
                place.setLocationName(textField1.getText());
                try {
                    PlaceDAO.addPlace(place);
                    JOptionPane.showMessageDialog(null, "Place added successfully.");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error adding place: " + e.getMessage());
                }
                loadPlaces();
            }
        });
    }

    private void loadPlaces() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        List<Place> places = PlaceDAO.getAllPlaces();
        for (Place place : places) {
            listModel.addElement(place.getLocationName());
        }
        list1.setModel(listModel);
    }
}
