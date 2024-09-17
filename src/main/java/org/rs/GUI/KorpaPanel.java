package org.rs.GUI;

import org.rs.DAO.EventDAO;
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

public class KorpaPanel {
    private JButton korpaButton;
    private JButton odjavaButton;
    public JPanel pocetnaPanel;
    private JButton profilButton;
    private JButton rezervacijeButton;
    private JLabel welcomeLabel;
    private JLabel balanceLabel;
    private JTable table1;
    private JButton izbaciButton;
    private JButton kupiButton;
    private JButton nazadButton;
    private JButton ocistiKorpuButton;
    private JButton kupiSveButton;
    private JLabel totalLabel;
    public User user;
    private List<Ticket> tickets;

    public KorpaPanel(JFrame oldFrame, User user) {
        this.user = user;
        welcomeLabel.setText("Dobrodosli " + user.getFullName());
        balanceLabel.setText("Balance: " + user.getBalance() + "KM");

        korpaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        // Add action listener for "Kupi" button
        kupiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseSelectedTicket();
            }
        });
        ocistiKorpuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TicketDAO.deleteTicketsByStatus2(user);
                fillTicketTable();
            }
        });
        kupiSveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double total = tickets.stream()
                        .mapToDouble(Ticket::getPrice) // Extract prices and convert to a stream of doubles
                        .sum(); // Sum the prices

                // First, check if the user has enough balance
                if (user.getBalance() < total) {
                    JOptionPane.showMessageDialog(null, "Not enough money");
                    return;
                }

                boolean allPurchasesSuccessful = true;
                for (Ticket ticket : tickets) {
                    int userPurchasedTickets = TicketDAO.getPurchasedUserTicketsForEvent(user, ticket.getEvent());
                    int maxUserTickets = ticket.getEvent().getMaxTicketsUser();

                    // Check if the user has already purchased the maximum number of tickets for the event
                    if (userPurchasedTickets >= maxUserTickets) {
                        JOptionPane.showMessageDialog(null, "You have already purchased the maximum number of tickets for: " + ticket.getEvent().getEventName());
                        allPurchasesSuccessful = false;
                        break;
                    }
                }

                // If all ticket checks passed, proceed with purchasing
                if (allPurchasesSuccessful) {
                    // Deduct total price from user balance
                    user.setBalance(user.getBalance() - total);
                    UserDAO.changeBalance(user, total, false);
                    balanceLabel.setText("Balance: " + user.getBalance() + "KM");
                    // Purchase each ticket individually
                    for (Ticket ticket : tickets) {
                        int sold =  TicketDAO.getSoldEventTicketsNumber(ticket.getEvent());
                        ticket.setSeatNumber(ticket.getEvent().getMaxTickets() -sold);
                        purchaseTicket(ticket);
                    }

//                    TicketDAO.purchaseUserTickets(user); // Optional if you need to update other ticket statuses
                    fillTicketTable(); // Refresh the ticket table
                    JOptionPane.showMessageDialog(null, "All tickets purchased successfully!");
                }
            }
        });

        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_user(oldFrame, user);
            }
        });
        rezervacijeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_rezervacije(oldFrame, user);
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
        // Set up the table with column names
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Event Name");
        model.addColumn("Event Location");
        model.addColumn("Event Date");
        model.addColumn("Ticket Price");

        table1.setModel(model);  // Apply the model to the table
    }

    private void fillTicketTable() {
        // Fetch the tickets for the current user
        tickets = TicketDAO.getUserTickets(user,2);
        DefaultTableModel model = (DefaultTableModel) table1.getModel();


        double total = tickets.stream()
                .mapToDouble(Ticket::getPrice) // Extract prices and convert to a stream of doubles
                .sum(); // Sum the prices

        // Update the label with the total price
        totalLabel.setText(String.format("Total: %.2f KM", total));
        // Clear the table before adding new rows (in case this is called again)
        model.setRowCount(0);

        // Iterate over the tickets and fill the table
        for (Ticket ticket : tickets) {
            Event event = ticket.getEvent();  // Get event associated with the ticket

            // Add a row with event name, location, price, and date
            model.addRow(new Object[]{
                    event.getEventName(),                   // Event Name
                    event.getLocationEntity().getLocationName(),  // Event Location
                    event.getEventDate().toString(),         // Event Date
                    ticket.getPrice() + " KM"              // Ticket Price
            });
        }
    }

    private void deleteSelectedTicket() {
        int selectedRow = table1.getSelectedRow();  // Get the selected row index
        if (selectedRow != -1) {
            // Get the ticket corresponding to the selected row
            Ticket selectedTicket = tickets.get(selectedRow);

            // Call the DAO method to delete the ticket from the database
            boolean success = TicketDAO.deleteTicket(selectedTicket);

            if (success) {
                // Remove the row from the table model
                DefaultTableModel model = (DefaultTableModel) table1.getModel();
                model.removeRow(selectedRow);

                // Refresh the ticket list
                fillTicketTable();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete the ticket.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a ticket to delete.");
        }
    }

    private void purchaseSelectedTicket() {
        int selectedRow = table1.getSelectedRow();  // Get the selected row index
        if (selectedRow != -1) {
            // Get the ticket corresponding to the selected row
            Ticket selectedTicket = tickets.get(selectedRow);
            int userPurchasedTickets = TicketDAO.getPurchasedUserTicketsForEvent(user, selectedTicket.getEvent());
            int maxUserTickets = selectedTicket.getEvent().getMaxTicketsUser();

            if (userPurchasedTickets >= maxUserTickets) {
                JOptionPane.showMessageDialog(null, "You have already purchased the maximum number of tickets for this event.");
                return;
            }

            // Set the ticket status to 0 (indicating it has been purchased)
            selectedTicket.setStatus(0);
            if(user.getBalance() < tickets.get(selectedRow).getPrice()) {
                JOptionPane.showMessageDialog(null, "Not enough money");
                return;
            }
            user.setBalance(user.getBalance() - tickets.get(selectedRow).getPrice());
            // Call the DAO method to update the ticket status in the database
            UserDAO.changeBalance(user, tickets.get(selectedRow).getPrice(), false);
            balanceLabel.setText("Balance: " + user.getBalance() + "KM");
            boolean success = TicketDAO.updateTicketStatus(selectedTicket);

            if (success) {
                // Refresh the ticket list
                fillTicketTable();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to purchase the ticket.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a ticket to purchase.");
        }
    }
    private void purchaseTicket(Ticket ticket) {
        if (ticket != null) {
            int userPurchasedTickets = TicketDAO.getPurchasedUserTicketsForEvent(user, ticket.getEvent());
            int maxUserTickets = ticket.getEvent().getMaxTicketsUser();

            if (userPurchasedTickets >= maxUserTickets) {
                JOptionPane.showMessageDialog(null, "You have already purchased the maximum number of tickets for this event.");
                return;
            }

            // Set the ticket status to 0 (indicating it has been purchased)
            ticket.setStatus(0);
            ticket.setSeatNumber(ticket.getEvent().getMaxTickets() - TicketDAO.getSoldEventTicketsNumber(ticket.getEvent()));

            if (user.getBalance() < ticket.getPrice()) {
                JOptionPane.showMessageDialog(null, "Not enough money");
                return;
            }

            user.setBalance(user.getBalance() - ticket.getPrice());
            UserDAO.changeBalance(user, ticket.getPrice(), false);
            balanceLabel.setText("Balance: " + user.getBalance() + "KM");

            boolean success = TicketDAO.updateTicketStatus(ticket);

            if (success) {
                // Refresh the ticket list
                fillTicketTable();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to purchase the ticket.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "No ticket provided for purchase.");
        }
    }

}
