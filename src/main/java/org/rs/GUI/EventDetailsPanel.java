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
    public User user;
    public Event event;

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
        dostupnoLabel.setText("Dostupno: " + (event.getMaxTickets() - TicketDAO.getSoldEventTicketsNumber(event)));
        cijena.setText((event.getPrice()) + "KM");


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
                    Ticket ticket = new Ticket();
                    ticket.setEvent(event);
                    ticket.setSector(sectors.get(comboBox1.getSelectedIndex()));
                    ticket.setPrice(event.getPrice()); // Example price, can be dynamic
                    ticket.setUser(user);
                    ticket.setPurchaseStartDate(LocalDate.now());
                    ticket.setPurchaseEndDate(LocalDate.now().plusDays(30)); // Example purchase window
                    ticket.setCancellationPolicy(event.isCancelPolicy()); // Example cancellation policy

                    if (user.getBalance() < ticket.getPrice()) {
                        JOptionPane.showMessageDialog(null, "Not enough money");
                        return;
                    }
                    // Set status for reservation (status = 1)
                    user.setBalance(user.getBalance() - ticket.getPrice());
                    UserDAO.changeBalance(user, ticket.getPrice());
                    TicketDAO.addTicket(ticket, 0);

                    JOptionPane.showMessageDialog(null, "Ticket purchased successfully!");

                }
            });
            rezervisiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Ticket ticket = new Ticket();
                    ticket.setEvent(event);
                    ticket.setSector(sectors.get(comboBox1.getSelectedIndex()));
                    ticket.setPrice(event.getPrice()); // Example price, can be dynamic
                    ticket.setUser(user);
                    ticket.setPurchaseStartDate(LocalDate.now());
                    ticket.setPurchaseEndDate(LocalDate.now().plusDays(30)); // Example purchase window
                    ticket.setCancellationPolicy(event.isCancelPolicy()); // Example cancellation policy

                    // Set status for reservation (status = 1)

                    int izbor = JOptionPane.showConfirmDialog(null, "Pay reservation fee now with online balance");

                    if (izbor == JOptionPane.YES_OPTION) {
                        if (user.getBalance() < ticket.getPrice() * 0.1) {
                            JOptionPane.showMessageDialog(null, "Not enough money");
                            return;
                        }
                        // Set status for reservation (status = 1)
                        user.setBalance(user.getBalance() - ticket.getPrice() * 0.1);
                        UserDAO.changeBalance(user, ticket.getPrice() * 0.1);
                        TicketDAO.addTicket(ticket, 1);
                    } else {
                        TicketDAO.addTicket(ticket, 1);
                    }

                    JOptionPane.showMessageDialog(null, "Ticket reserved successfully!");
                }
            });

            dodajUKorpuButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    Ticket ticket = new Ticket();
                    ticket.setEvent(event);
                    ticket.setSector(sectors.get(comboBox1.getSelectedIndex()));
                    ticket.setPrice(event.getPrice()); // Example price, can be dynamic
                    ticket.setPurchaseStartDate(LocalDate.now());
                    ticket.setUser(user);
                    ticket.setPurchaseEndDate(LocalDate.now().plusDays(30)); // Example purchase window
                    ticket.setCancellationPolicy(event.isCancelPolicy()); // Example cancellation policy

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
