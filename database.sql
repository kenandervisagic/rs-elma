CREATE TABLE roles (
                       role_id INT AUTO_INCREMENT PRIMARY KEY,
                       role_name VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       full_name VARCHAR(100),
                       role_id INT,
                       FOREIGN KEY (role_id) REFERENCES roles(role_id)
) ENGINE=InnoDB;

CREATE TABLE event_categories (
                                  category_id INT AUTO_INCREMENT PRIMARY KEY,
                                  category_name VARCHAR(50) NOT NULL UNIQUE
)ENGINE=InnoDB;

CREATE TABLE events (
                        event_id INT AUTO_INCREMENT PRIMARY KEY,
                        organizer_id INT NOT NULL,
                        category_id INT NOT NULL,
                        event_name VARCHAR(100) NOT NULL,
                        description TEXT,
                        location VARCHAR(255) NOT NULL,
                        event_date DATE NOT NULL,
                        event_time TIME NOT NULL,
                        image_url VARCHAR(255),
                        max_tickets INT NOT NULL,
                        FOREIGN KEY (organizer_id) REFERENCES users(user_id),
                        FOREIGN KEY (category_id) REFERENCES event_categories(category_id)
)ENGINE=InnoDB;

CREATE TABLE tickets (
                         ticket_id INT AUTO_INCREMENT PRIMARY KEY,
                         event_id INT NOT NULL,
                         seat_location VARCHAR(50),
                         price DECIMAL(10, 2) NOT NULL,
                         purchase_start_date DATE,
                         purchase_end_date DATE,
                         cancellation_policy TEXT,
                         max_purchase_per_user INT,
                         FOREIGN KEY (event_id) REFERENCES events(event_id)
)ENGINE=InnoDB;

CREATE TABLE reservations (
                              reservation_id INT AUTO_INCREMENT PRIMARY KEY,
                              user_id INT NOT NULL,
                              ticket_id INT NOT NULL,
                              reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              payment_status VARCHAR(50) NOT NULL,
                              payment_amount DECIMAL(10, 2) NOT NULL,
                              discount_applied DECIMAL(10, 2) DEFAULT 0,
                              pdf_ticket_url VARCHAR(255),
                              FOREIGN KEY (user_id) REFERENCES users(user_id),
                              FOREIGN KEY (ticket_id) REFERENCES tickets(ticket_id)
)ENGINE=InnoDB;

CREATE TABLE locations (
                           location_id INT AUTO_INCREMENT PRIMARY KEY,
                           location_name VARCHAR(100) NOT NULL UNIQUE,
                           address VARCHAR(255),
                           capacity INT NOT NULL,
                           image_url VARCHAR(255)
)ENGINE=InnoDB;

ALTER TABLE locations
    ADD COLUMN place_id INT;
ALTER TABLE locations
    ADD CONSTRAINT fk_place
        FOREIGN KEY (place_id) REFERENCES Place(town_id);