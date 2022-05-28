package main.java.com.vendors.app.components;

import main.java.com.vendors.enums.Rank;
import main.java.com.vendors.models.Vendor;
import main.java.com.vendors.tree.Tree;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Container extends JFrame {
    private JButton button1;
    private JPanel containerPanel;
    private JLabel title;
    public Tree vendors;

    public Container() {
        setContentPane(containerPanel);
        setTitle("Initial app");
        setSize(450, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        vendors = new Tree();
        setLocationRelativeTo(null);
        setVisible(true);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int eventReturned = fileChooser.showOpenDialog(containerPanel);
                if (eventReturned == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String line = "";
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new FileReader(selectedFile));
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }

                    while (true) {
                        try {
                            if ((line = reader.readLine()) == null) break;
                            String[] attributes = line.trim().split("\t");

                            long cedula = Long.parseLong(attributes[0]);
                            String name = attributes[1];
                            Rank currentRank = Rank.valueOf(attributes[2].toUpperCase());
                            double salesMonthly = Double.parseDouble(attributes[3]);
                            long parentId = 0;
                            if (attributes.length == 5)
                                parentId = Long.parseLong(attributes[4]);

                            Vendor person = new Vendor(cedula, name, currentRank, salesMonthly);
                            vendors.insert(person, parentId);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    vendors.assignRanks(vendors.getRoot());
                    vendors.assignCommissions(vendors.getRoot());
                }
            }
        });
    }

}
