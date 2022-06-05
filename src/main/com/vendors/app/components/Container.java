package main.com.vendors.app.components;

import main.com.vendors.enums.Rank;
import main.com.vendors.list.List;
import main.com.vendors.list.ListNode;
import main.com.vendors.models.Vendor;
import main.com.vendors.tree.Tree;
import main.com.vendors.tree.TreeNode;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Container extends JFrame {

    private JPanel containerPanel;
    private JLabel title;
    private JButton uploadFileBtn;
    private JLabel logo;
    private JPanel containerLogo;
    public Tree vendorsTree;
    public List vendorsList;

    public Container() {
        setContentPane(containerPanel);
        setTitle("Vendors App");
        ImageIcon appIcon = new ImageIcon(getClass().getResource("../resources/app-icon.png"));
        setIconImage(appIcon.getImage());
        setSize(450, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon(getClass().getResource("../resources/vendors.png"));
        ImageIcon resizedIcon = new ImageIcon(icon.getImage().getScaledInstance(280, 240, Image.SCALE_DEFAULT));
        logo.setIcon(resizedIcon);

        vendorsList = new List();
        vendorsTree = new Tree();
        setLocationRelativeTo(null);
        setVisible(true);
        uploadFileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int eventReturned = fileChooser.showOpenDialog(containerPanel);
                if (eventReturned == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new FileReader(selectedFile));
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }

                    while (true) {
                        try {
                            String line = reader.readLine();
                            if (line == null) break;
                            String[] attributes = line.trim().split("\t");

                            long cedula = Long.parseLong(attributes[0]);
                            String name = attributes[1];
                            Rank currentRank = Rank.valueOf(attributes[2].toUpperCase());
                            double salesMonthly = Double.parseDouble(attributes[3]);
                            long parentId = 0;
                            if (attributes.length == 5)
                                parentId = Long.parseLong(attributes[4]);

                            Vendor person = new Vendor(cedula, name, currentRank, salesMonthly, parentId);
                            vendorsList.add(new TreeNode(person));

                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    insertNodes(0);
                    vendorsTree.assignRanks(vendorsTree.getRoot());
                    vendorsTree.assignCommissions(vendorsTree.getRoot());

                    JSONObject vendorsJSON = vendorsTree.serializeTree();
                    System.out.println(vendorsJSON);
                }
            }
        });
    }

    public void insertNodes(long parentId) {
        TreeNode localeRoot = vendorsList.findByParentId(parentId);
        ListNode current = vendorsList.getHead();

        if (parentId == 0) {
            vendorsTree.insert(localeRoot, parentId);
        }

        while (current != null) {
            if (current.getTreeNode().getVendor().getParentId() == parentId) {
                vendorsTree.insert(current.getTreeNode(), parentId);
                insertNodes(current.getTreeNode().getVendor().getCedula());
            }
            current = current.getNext();
        }
    }

}
