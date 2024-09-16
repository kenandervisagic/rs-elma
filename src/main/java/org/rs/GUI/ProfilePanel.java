package org.rs.GUI;

import org.rs.DAO.TicketDAO;
import org.rs.entity.Ticket;
import org.rs.entity.User;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProfilePanel {
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
    private JLabel welcomeLabel;
    private JButton odjavaButton;
    private JButton korpaButton;
    private JLabel balanceLabel;
    private JButton profilButton;
    private JButton rezervacijeButton;
    private int currentPage = 0;
    private int pageSize = 4;
    private int totalTickets;
    private List<Ticket> displayedTickets;
    private User user;

    public ProfilePanel(JFrame oldFrame, User user) {
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

        prethodnaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 0) {
                    currentPage--;
                    fetchAndDisplayTickets();
                }
            }
        });

        sljedecaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((currentPage + 1) * pageSize < totalTickets) {
                    currentPage++;
                    fetchAndDisplayTickets();
                }
            }
        });

        rezervacijeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_rezervacije(oldFrame, user);
            }
        });

        fetchAndDisplayTickets();
    }

    private void fetchAndDisplayTickets() {
        // Fetch tickets with status 0 and apply pagination
        List<Ticket> tickets = TicketDAO.getTicketsByStatus0(user, currentPage, pageSize);
        totalTickets = TicketDAO.getTotalNumberOfTicketsByStatus0(user);

        // Update the UI with the filtered tickets
        updateTicketDisplay(tickets);

        // Enable or disable navigation buttons based on the page
        prethodnaButton.setEnabled(currentPage > 0);
        sljedecaButton.setEnabled((currentPage + 1) * pageSize < totalTickets);
    }

    private void updateTicketDisplay(List<Ticket> tickets) {
        this.displayedTickets = tickets;

        JPanel[] eventPanels = {event1Panel, event2Panel, event3Panel, event4Panel};
        JLabel[] eventLabels = {event1Label1, event1Label2, event1Label3,
                event2Label1, event2Label2, event2Label3,
                event3Label1, event3Label2, event3Label3,
                event4Label1, event4Label2, event4Label3};
        JButton[] pregledajButtons = {pregledajButton, pregledajButton1, pregledajButton2, pregledajButton3};

        // Hide all panels initially
        for (JPanel panel : eventPanels) {
            panel.setVisible(false);
        }

        // Hide all buttons initially
        for (JButton button : pregledajButtons) {
            button.setVisible(false);
        }

        // Display the tickets
        for (int i = 0; i < tickets.size(); i++) {
            Ticket ticket = tickets.get(i);
            if (i < eventPanels.length) {
                JPanel panel = eventPanels[i];
                panel.setVisible(true);
                eventLabels[i * 3].setText(ticket.getEvent().getEventName());
                eventLabels[i * 3 + 1].setText(ticket.getEvent().getLocationEntity().getLocationName());
                eventLabels[i * 3 + 2].setText(ticket.getEvent().getEventDate().toString());

                JButton button = pregledajButtons[i];
                button.setVisible(true);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Handle button click for this ticket
                        // For example, open event details or ticket details window
                        JOptionPane.showMessageDialog(null, "Details for event: " + ticket.getEvent().getEventName());
                    }
                });
            }
        }
    }
}
