package org.rs.GUI;

import org.rs.DAO.TicketDAO;
import org.rs.DAO.UserDAO;
import org.rs.entity.Event;
import org.rs.entity.Sector;
import org.rs.entity.Ticket;
import org.rs.entity.User;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class EventDetailsPanel {
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
    private JButton dodajUKorpuButton;
    private JLabel dostupnoLabel;
    private JLabel sektorLabel;
    private JLabel cancel;
    public User user;
    public Event event;
    private int available;

    public EventDetailsPanel(JFrame oldFrame, User user, Event event) {

        this.event = event;
        this.user = user;

        if (user == null) {
            kupiButton.setVisible(false);
            rezervisiButton.setVisible(false);
            dodajUKorpuButton.setVisible(false);
            comboBox1.setVisible(false);
            sektorLabel.setVisible(false);
        }
        naslov.setText(event.getEventName());
        datum.setText(event.getEventDate().toString() + "   ");
        lokacija.setText(event.getLocationEntity().getLocationName());
        opis.setText(event.getDescription());
        vrijeme.setText(event.getEventTime().toString());
        available = (event.getMaxTickets() - TicketDAO.getSoldEventTicketsNumber(event));
        dostupnoLabel.setText("Dostupno: " + available + " (Max karti: " + event.getMaxTicketsUser() + ")");
        cijena.setText((event.getPrice()) + "KM");
        cancel.setText(!event.isCancelPolicy() ? "Naknada za otkazivanje 10%" : "");


        if (user != null) {
            List<Sector> sectors = new ArrayList<>(event.getSectors());

            // Populate JComboBox with place names
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            for (Sector sector : sectors) {
                model.addElement(sector.getSectorName());
            }
            comboBox1.setModel(model);

            kupiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    // Check if the user has reached the max number of tickets for this event
                    int userPurchasedTickets = TicketDAO.getUserTicketsForEvent(user, event);
                    int maxUserTickets = event.getMaxTicketsUser();

                    if (userPurchasedTickets >= maxUserTickets) {
                        JOptionPane.showMessageDialog(null, "You have already purchased the maximum number of tickets for this event.");
                        return;
                    }

                    Ticket ticket = new Ticket();
                    ticket.setEvent(event);
                    ticket.setSector(sectors.get(comboBox1.getSelectedIndex()));
                    ticket.setPrice(event.getPrice()); // Example price, can be dynamic
                    ticket.setUser(user);
                    ticket.setPurchaseStartDate(LocalDate.now());
                    ticket.setPurchaseEndDate(LocalDate.now().plusDays(30)); // Example purchase window
                    ticket.setCancellationPolicy(event.isCancelPolicy()); // Example cancellation policy
                    ticket.setSeatNumber(available);

                    if (user.getBalance() < ticket.getPrice()) {
                        JOptionPane.showMessageDialog(null, "Not enough money");
                        return;
                    }
                    // Set status for purchase (status = 0)
                    user.setBalance(user.getBalance() - ticket.getPrice());
                    UserDAO.changeBalance(user, ticket.getPrice(), false);
                    TicketDAO.addTicket(ticket, 0);

                    JOptionPane.showMessageDialog(null, "Ticket purchased successfully!");

                    available = (event.getMaxTickets() - TicketDAO.getSoldEventTicketsNumber(event));
                    dostupnoLabel.setText("Dostupno: " + available + " (Max karti: " + event.getMaxTicketsUser() + ")");
                }
            });

            rezervisiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Check if the user has reached the max number of tickets for this event
                    int userReservedTickets = TicketDAO.getUserTicketsForEvent(user, event);
                    int maxUserTickets = event.getMaxTicketsUser();

                    if (userReservedTickets >= maxUserTickets) {
                        JOptionPane.showMessageDialog(null, "You have already reserved the maximum number of tickets for this event.");
                        return;
                    }

                    Ticket ticket = new Ticket();
                    ticket.setEvent(event);
                    ticket.setSector(sectors.get(comboBox1.getSelectedIndex()));
                    ticket.setPrice(event.getPrice()); // Example price, can be dynamic
                    ticket.setUser(user);
                    ticket.setSeatNumber(available);
                    ticket.setPurchaseStartDate(LocalDate.now());
                    ticket.setPurchaseEndDate(LocalDate.now().plusDays(30)); // Example reservation window
                    ticket.setCancellationPolicy(event.isCancelPolicy()); // Example cancellation policy

                    if (user.getBalance() < ticket.getPrice() * 0.1) {
                        JOptionPane.showMessageDialog(null, "Not enough money");
                        return;
                    }
                    // Set status for reservation (status = 1)
                    user.setBalance(user.getBalance() - ticket.getPrice() * 0.1);
                    UserDAO.changeBalance(user, ticket.getPrice() * 0.1, false);
                    TicketDAO.addTicket(ticket, 1);

                    JOptionPane.showMessageDialog(null, "Ticket reserved successfully!");

                    available = (event.getMaxTickets() - TicketDAO.getSoldEventTicketsNumber(event));
                    dostupnoLabel.setText("Dostupno: " + available + " (Max karti: " + event.getMaxTicketsUser() + ")");
                }
            });

            dodajUKorpuButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int userBasketTickets = TicketDAO.getBasketUserTicketsForEvent(user,event);
                    int maxUserTickets = event.getMaxTicketsUser();

                    if (userBasketTickets >= maxUserTickets) {
                        JOptionPane.showMessageDialog(null, "You have already added to basket the maximum number of tickets for this event.");
                        return;
                    }
                    Ticket ticket = new Ticket();
                    ticket.setEvent(event);
                    ticket.setSector(sectors.get(comboBox1.getSelectedIndex()));
                    ticket.setPrice(event.getPrice()); // Example price, can be dynamic
                    ticket.setPurchaseStartDate(LocalDate.now());
                    ticket.setUser(user);
                    ticket.setPurchaseEndDate(LocalDate.now().plusDays(30)); // Example purchase window
                    ticket.setCancellationPolicy(event.isCancelPolicy()); // Example cancellation policy
                    ticket.setSeatNumber(available);
                    // Set status for reservation (status = 1)
                    TicketDAO.addTicket(ticket, 2);

                    JOptionPane.showMessageDialog(null, "Ticket added to basket successfully!");

                }
            });
        }
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user == null) {
                    WindowHandler.create_window_home(oldFrame);
                } else {
                    WindowHandler.create_window_user(oldFrame, user);
                }
            }
        });
    }


}
