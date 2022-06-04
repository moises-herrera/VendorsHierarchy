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
        Assertions.assertEquals(nodeId, foundNodeId);
    }

    @Test
    void countTreeLevels() {
        // Arrange
        initializeTree();

        // Act
        int levels = tree.countTreeLevels(tree.getRoot());

        // Assert
        Assertions.assertEquals(7, levels);
    }

    @Test
    void countChildrenByLevelOfRoot() {
        // Arrange
        initializeTree();

        // Act
        int children = tree.countChildrenByLevel(tree.getRoot(), 1);

        // Assert
        Assertions.assertEquals(3, children);
    }

    @Test
    void countChildrenByLevelOfLocaleRoot() {
        // Arrange
        initializeTree();

        // Act
        TreeNode localeRoot = tree.find(556777);
        int children = tree.countChildrenByLevel(localeRoot, 1);

        // Assert
        Assertions.assertEquals(2, children);
    }

    @Test
    void assignRanks() {
        // Arrange
        initializeTree();

        // Act
        tree.assignRanks(tree.getRoot());

        // Assert
        Assertions.assertEquals(Rank.PLATA, tree.getRoot().getVendor().getPreviousRank());
        Assertions.assertEquals(Rank.ORO, tree.getRoot().getVendor().getCurrentRank());
    }

    @Test
    void childrenSalesOfRoot() {
        // Arrange
        initializeTree();

        File vendorsFile = new File("src\\test\\com\\vendors\\mocks\\vendors.txt");
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(vendorsFile));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        double childrenSales = 0;

        while (true) {
            try {
                String line = reader.readLine();
                if (line == null) break;
                String[] attributes = line.trim().split("\t");

                if (attributes.length == 5) {
                    childrenSales += Double.parseDouble(attributes[3]);
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Act
        TreeNode root = tree.getRoot();
        double totalSales = tree.calculateChildrenSales(root);

        // Assert
        Assertions.assertEquals(childrenSales, totalSales);
    }

    @Test
    void childrenSalesOfLocaleRoot() {
        // Arrange
        initializeTree();

        File vendorsFile = new File("src\\test\\com\\vendors\\mocks\\vendors.txt");
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(vendorsFile));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        double childrenSales = 0;

        while (true) {
            try {
                String line = reader.readLine();
                if (line == null) break;
                String[] attributes = line.trim().split("\t");

                if (attributes.length == 5 && Long.parseLong(attributes[0]) != 345345) {
                    long parentId = Long.parseLong(attributes[4]);

                    if (parentId == 345345 || parentId == 567543)
                        childrenSales += Double.parseDouble(attributes[3]);
                }

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Act
        TreeNode localeRoot = tree.getRoot().getChildAt(0);
        double totalSales = tree.calculateChildrenSales(localeRoot);

        // Assert
        Assertions.assertEquals(childrenSales, totalSales);
    }

    @Test
    void calculateChildrenSalesByLevelOfRoot() {
        // Arrange
        initializeTree();

        // Act
        double expectedFirst = 1800000;
        double salesFirstLevel = tree.calculateChildrenSalesByLevel(tree.getRoot(), 1);

        double expectedThird = 2510000;
        double salesThirdLevel = tree.calculateChildrenSalesByLevel(tree.getRoot(), 3);

        // Assert
        Assertions.assertEquals(expectedFirst, salesFirstLevel);
        Assertions.assertEquals(expectedThird, salesThirdLevel);
    }

    @Test
    void calculateChildrenSalesByLevelOfLocaleRoot() {
        // Arrange
        initializeTree();

        // Act
        TreeNode localeRoot = tree.find(909000);

        double expectedFirst = 1200000;
        double salesFirstLevel = tree.calculateChildrenSalesByLevel(localeRoot, 1);

        double expectedFourth = 1000000;
        double salesFourthLevel = tree.calculateChildrenSalesByLevel(localeRoot, 4);

        // Assert
        Assertions.assertEquals(expectedFirst, salesFirstLevel);
        Assertions.assertEquals(expectedFourth, salesFourthLevel);
    }

    @Test
    void existsChildWithRank() {
        // Arrange
        initializeTree();

        // Act
        Rank searchedRank = Rank.ORO;
        TreeNode childWithGold = tree.existsChildWithRank(tree.getRoot(), searchedRank);

        // Assert
        Assertions.assertNotNull(childWithGold);
        Rank rankOfChild = childWithGold.getVendor().getCurrentRank();
        Assertions.assertEquals(rankOfChild, searchedRank);
    }

    @Test
    void notExistsChildWithRank() {
        // Arrange
        initializeTree();

        // Act
        TreeNode localeRoot = tree.find(901000);
        Rank searchedRank = Rank.PLATA;
        TreeNode childWithSilver = tree.existsChildWithRank(localeRoot, searchedRank);

        // Assert
        Assertions.assertNull(childWithSilver);
    }

    @Test
    void calculateLevelCommissionOfRoot() {
        // Arrange
        initializeTree();

        // Act
        double rootCommission = tree.calculateLevelCommission(tree.getRoot());

        // Assert
        Assertions.assertEquals(243900, rootCommission);
    }

    @Test
    void calculateLevelCommissionOfLocaleRoot() {
        // Arrange
        initializeTree();

        // Act
        TreeNode localeRoot = tree.find(901000);
        double commission = tree.calculateLevelCommission(localeRoot);

        // Assert
        Assertions.assertEquals(84000, commission);
    }
}
