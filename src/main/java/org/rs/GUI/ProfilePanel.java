package org.rs.GUI;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import org.rs.DAO.TicketDAO;
import org.rs.entity.Ticket;
import org.rs.entity.User;
import org.rs.util.WindowHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private JLabel event2Label4;
    private JLabel event2Label5;
    private JLabel event3Label4;
    private JLabel event3Label5;
    private JLabel event4Label4;
    private JLabel event4Label5;
    private JLabel event1Label4;
    private JLabel event1Label5;
    private JButton nazadButton;
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
        nazadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowHandler.create_window_user(oldFrame, user);
            }
        });
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
        JLabel[] eventLabels = {
                event1Label1, event1Label2, event1Label3, event1Label4, event1Label5,
                event2Label1, event2Label2, event2Label3, event2Label4, event2Label5,
                event3Label1, event3Label2, event3Label3, event3Label4, event3Label5,
                event4Label1, event4Label2, event4Label3, event4Label4, event4Label5,
        };
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
                eventLabels[i * 5].setText(ticket.getEvent().getEventName());
                eventLabels[i * 5 + 1].setText(ticket.getEvent().getLocationEntity().getLocationName());
                eventLabels[i * 5 + 2].setText(ticket.getEvent().getEventDate().toString());
                eventLabels[i * 5 + 3].setText("Sektor: " + ticket.getSector().getSectorName());
                eventLabels[i * 5 + 4].setText("Sjediste: " + ticket.getSeatNumber());

                JButton button = pregledajButtons[i];
                button.setVisible(true);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Handle button click for this ticket
                        // For example, open event details or ticket details window
                        try {
                            createPdfForTicket(ticket);
                        } catch (FileNotFoundException ex) {
                            throw new RuntimeException(ex);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        }
    }

    public void createPdfForTicket(Ticket ticket) throws IOException {
        // Define the file path where the PDF will be saved
        String filePath = System.getProperty("user.home") + "/Documents/Ticket_" + ticket.getId() + ".pdf";

        // Initialize the PDF document
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Define fonts
        PdfFont titleFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
        PdfFont labelFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
        PdfFont valueFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);
        PdfFont footerFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_OBLIQUE);

        // Add ticket title
        Paragraph title = new Paragraph("EVENT TICKET")
                .setFont(titleFont)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setUnderline()
                .setMarginBottom(20);
        document.add(title);

        // Add ticket details in a table format
        Table table = new Table(2);

        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        // Event name
        table.addCell(new Cell().add(new Paragraph("Event:").setFont(labelFont)));
        table.addCell(new Cell().add(new Paragraph(ticket.getEvent().getEventName()).setFont(valueFont)));

        // Location
        table.addCell(new Cell().add(new Paragraph("Location:").setFont(labelFont)));
        table.addCell(new Cell().add(new Paragraph(ticket.getEvent().getLocationEntity().getLocationName()).setFont(valueFont)));

        // Date
        table.addCell(new Cell().add(new Paragraph("Date:").setFont(labelFont)));
        table.addCell(new Cell().add(new Paragraph(ticket.getEvent().getEventDate().toString()).setFont(valueFont)));
        //Time
        table.addCell(new Cell().add(new Paragraph("Time:").setFont(labelFont)));
        table.addCell(new Cell().add(new Paragraph(ticket.getEvent().getEventTime().toString()).setFont(valueFont)));

        // Sector
        table.addCell(new Cell().add(new Paragraph("Sector:").setFont(labelFont)));
        table.addCell(new Cell().add(new Paragraph(ticket.getSector().getSectorName()).setFont(valueFont)));

        // Seat
        table.addCell(new Cell().add(new Paragraph("Seat:").setFont(labelFont)));
        table.addCell(new Cell().add(new Paragraph(ticket.getSeatNumber().toString()).setFont(valueFont)));

        // Price
        table.addCell(new Cell().add(new Paragraph("Price:").setFont(labelFont)));
        table.addCell(new Cell().add(new Paragraph(ticket.getPrice() + " KM").setFont(valueFont)));

        // Add the table to the document
        document.add(table);

        // Add a decorative line (optional)
        LineSeparator line = new LineSeparator(new SolidLine());
        line.setMarginTop(10);
        document.add(line);

        // Add footer information (optional)
        Paragraph footer = new Paragraph("Hvala na kupovini!")
                .setFont(footerFont)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10);
        document.add(footer);

        // Close the document
        document.close();

        System.out.println("PDF created at: " + filePath);
    }
}
