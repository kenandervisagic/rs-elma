package org.rs.GUI;

import org.rs.entity.Event;
import org.rs.util.WindowHandler;
import org.rs.DAO.UserDAO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class pocetna {
    private JButton prijavaButton;
    private JButton registracijaButton;
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

    public pocetna(JFrame oldFrame) {
        prijavaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_login(oldFrame);
            }
        });
        registracijaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_unesi_korisnika(oldFrame);

            }
        });
        fetchAndDisplayEvents();
    }

    private void fetchAndDisplayEvents() {
        // Fetch the top 5 events from the database
        List<Event> events = UserDAO.getTop4Events();

        // Assuming each JLabel is used to display one event's details
        JLabel[] eventLabels = {event1Label1, event1Label2, event1Label3,
                event2Label1, event2Label2, event2Label3,
                event3Label1, event3Label2, event3Label3,
                event4Label1, event4Label2, event4Label3};

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);
            eventLabels[i * 3].setText(event.getEventName()); // Name
            eventLabels[i * 3 + 1].setText(event.getLocationEntity().getLocationName()); // Location name
            eventLabels[i * 3 + 2].setText(event.getEventDate().toString()); // Date
        }
    }
}
