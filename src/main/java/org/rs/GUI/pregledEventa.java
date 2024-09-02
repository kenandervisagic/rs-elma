package org.rs.GUI;

import org.rs.DAO.SectorDAO;
import org.rs.entity.Event;
import org.rs.entity.Sector;
import org.rs.entity.User;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class pregledEventa {
    public JPanel panel_login;
    private JButton kupiButton;
    private JButton rezervisiButton;
    private JLabel naslov;
    private JLabel datum;
    private JLabel lokacija;
    private JLabel vrijeme;
    private JLabel cijena;
    private JComboBox comboBox1;
    private JButton nazadButton;
    private JLabel opis;
    public User user;
    public Event event;

    public pregledEventa(JFrame oldFrame, User user, Event event) {

        this.event = event;
        this.user = user;

        naslov.setText(event.getEventName());
        datum.setText(event.getEventDate().toString() + "   ");
        lokacija.setText(event.getLocationEntity().getLocationName());
        opis.setText(event.getDescription());
        vrijeme.setText(event.getEventTime().toString());


        List<Sector> sectors = SectorDAO.getSectorsForLocation(event.getLocationEntity().getId());
        // Populate JComboBox with place names
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Sector sector : sectors) {
            model.addElement(sector.getSectorName());
        }
        comboBox1.setModel(model);

        kupiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
        rezervisiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_home(oldFrame);
            }
        });
    }

}
