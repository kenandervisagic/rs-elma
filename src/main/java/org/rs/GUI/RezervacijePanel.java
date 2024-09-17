package org.rs.GUI;

import org.rs.DAO.TicketDAO;
import org.rs.DAO.UserDAO;
import org.rs.entity.Event;
import org.rs.entity.Ticket;
import org.rs.entity.User;
import org.rs.util.WindowHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RezervacijePanel {
    private JButton korpaButton;
    private JButton odjavaButton;
    public JPanel pocetnaPanel;
    private JButton profilButton;
    private JButton rezervacijeButton;
    private JLabel welcomeLabel;
    private JLabel balanceLabel;
    private JTable table1;
    private JButton izbaciButton;
    private JButton nazadButton;
    private JButton kupiButton;
    public User user;
    private List<Ticket> tickets;

    public RezervacijePanel(JFrame oldFrame, User user) {
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

        // Set up the table model and call the method to fill it
        setupTable();
        fillTicketTable();

        izbaciButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedTicket();
            }
        });

        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_user(oldFrame, user);
            }
        });
        kupiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseSelectedTicket();
            }
        });
        profilButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_profile(oldFrame,user);
            }
        });
    }

    private void setupTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Event Name");
        model.addColumn("Event Location");
        model.addColumn("Event Date");
        model.addColumn("Cancelation fee");
        model.addColumn("Ticket Price");

        table1.setModel(model);
        table1.setAutoCreateRowSorter(true); // Optional: enables sorting
        table1.setFillsViewportHeight(true); // Ensure table fills the viewport height
    }

    private void fillTicketTable() {
        tickets = TicketDAO.getUserTickets(user, 1);
        DefaultTableModel model = (DefaultTableModel) table1.getModel();

        double total = tickets.stream()
                .mapToDouble(Ticket::getPrice)
                .sum();

        balanceLabel.setText("Balance: " + user.getBalance() + "KM");

        // Clear the table before adding new rows
        model.setRowCount(0);

        // Iterate over the tickets and fill the table
        for (Ticket ticket : tickets) {
            Event event = ticket.getEvent();

            model.addRow(new Object[]{
                    event.getEventName(),
                    event.getLocationEntity().getLocationName(),
                    event.getEventDate().toString(),
                    ticket.getCancellationPolicy() ? "Bez naknade" : "Naknada 10%",
                    ticket.getPrice() * 0.9 + " KM"
            });
        }
    }

    private void deleteSelectedTicket() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow != -1) {
            Ticket selectedTicket = tickets.get(selectedRow);
            if(selectedTicket.getCancellationPolicy()){
                user.setBalance(user.getBalance() + selectedTicket.getPrice() * 0.1);
                UserDAO.changeBalance(user, selectedTicket.getPrice() * 0.1, true);
            }

            boolean success = TicketDAO.deleteTicket(selectedTicket);

            if (success) {
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                model.removeRow(selectedRow);

                fillTicketTable(); // Refresh the ticket list
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete the ticket.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a ticket to delete.");
        }
    }

    private void purchaseSelectedTicket() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow != -1) {
            Ticket selectedTicket = tickets.get(selectedRow);
            int userPurchasedTickets = TicketDAO.getPurchasedUserTicketsForEvent(user, selectedTicket.getEvent());
            int maxUserTickets = selectedTicket.getEvent().getMaxTicketsUser();

            if (userPurchasedTickets >= maxUserTickets) {
                JOptionPane.showMessageDialog(null, "You have already purchased the maximum number of tickets for this event.");
                return;
            }
            selectedTicket.setStatus(0);
            if (user.getBalance() < tickets.get(selectedRow).getPrice()) {
                JOptionPane.showMessageDialog(null, "Not enough money");
                return;
            }
            user.setBalance(user.getBalance() - tickets.get(selectedRow).getPrice() * 0.9);
            UserDAO.changeBalance(user, tickets.get(selectedRow).getPrice(), false);
            balanceLabel.setText("Balance: " + user.getBalance() + "KM");

            boolean success = TicketDAO.updateTicketStatus(selectedTicket);

            if (success) {
                fillTicketTable(); // Refresh the ticket list
            } else {
                JOptionPane.showMessageDialog(null, "Failed to purchase the ticket.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a ticket to purchase.");
        }
    }
}
