package test.com.vendors;

import main.com.vendors.enums.Rank;
import main.com.vendors.models.Vendor;
import main.com.vendors.tree.Tree;
import main.com.vendors.tree.TreeNode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

public class TreeTest {
    private Tree tree;

    @BeforeEach
    void setUp() {
        tree = new Tree();
    }

    void initializeTree() {
        File vendorsFile = new File("src\\test\\com\\vendors\\mocks\\vendors.txt");
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(vendorsFile));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        String line;

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
                tree.insert(person, parentId);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Test
    void findNodeById() {
        // Arrange
        initializeTree();

        // Act
        long nodeId = 908000;
        TreeNode found = tree.find(nodeId);

        // Assert
        Assertions.assertFalse(tree.isEmpty());
        Assertions.assertNotNull(found);

        long foundNodeId = found.getVendor().getCedula();
        Assertions.assertEquals(foundNodeId, nodeId);
    }
}
