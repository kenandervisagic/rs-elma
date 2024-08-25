package org.rs;

import org.rs.util.WindowHandler;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        WindowHandler.create_window_login(frame);
    }
}