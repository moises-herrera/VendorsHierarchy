package main.java.com.vendors.app.components;

import javax.swing.*;

public class Container extends JFrame {
    private JButton button1;
    private JPanel containerPanel;
    private JLabel title;

    public Container() {
        setContentPane(containerPanel);
        setTitle("Initial app");
        setSize(450, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
