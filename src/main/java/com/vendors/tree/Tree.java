package main.java.com.vendors.tree;

import main.java.com.vendors.enums.Rank;
import main.java.com.vendors.list.List;
import main.java.com.vendors.list.ListNode;
import main.java.com.vendors.models.Vendor;

public class Tree {
    private TreeNode root;

    public Tree() {
        this.root = null;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public void insert(Vendor data, long parentId) {
        TreeNode newNode = new TreeNode(data);
        if (root == null) {
            root = newNode;
        } else if (parentId > 0) {
            if (root.getVendor().getCedula() == parentId) {
                root.addChild(newNode);
                return;
            }

            TreeNode parent = findParentById(root.getChildren(), parentId);
            if (parent != null) parent.addChild(newNode);
        }
    }

    public TreeNode findParentById(List children, long parentId) {
        ListNode current = children.getHead();

        while (current != null) {
            if (current.getTreeNode().getVendor().getCedula() == parentId) {
                break;
            }

            if (!current.getTreeNode().getChildren().isEmpty()) {
                return findParentById(current.getTreeNode().getChildren(), parentId);
            }

            current = current.getNext();
        }

        return current != null ? current.getTreeNode() : null;
    }

    public boolean verifyRankCopper(Vendor person) {
        if (person.getSalesMonthly() >= 0) {
            return true;
        }

        return false;
    }

    public boolean verifyRankBronze(Vendor person) {
        TreeNode vendorParent = findParentById(root.getChildren(), person.getCedula());
        boolean childrenSalesForBronze = checkSalesByLevel(vendorParent.getChildren(), 1);

        if (person.getSalesMonthly() > 200000 && childrenSalesForBronze) {
            return true;
        }

        return false;
    }

    public boolean verifyRankSilver(Vendor person) {
        TreeNode vendorParent = findParentById(root.getChildren(), person.getCedula());
        double childrenSales = calculateChildrenSales(vendorParent.getChildren(), 0);
        int childrenInOne = countChildrenByLevel(vendorParent.getChildren(), 0, 0);

        if (person.getSalesMonthly() > 300000 && childrenSales > 1000000 && childrenInOne >= 3) {
            return true;
        }

        return false;
    }

    public boolean verifyRankGold(Vendor person) {
        TreeNode vendorParent = findParentById(root.getChildren(), person.getCedula());
        double childrenSales = calculateChildrenSales(vendorParent.getChildren(), 0);
        TreeNode childWithSilver = existsChildWithRank(vendorParent.getChildren(), 0, Rank.PLATA);
        int treeLevels = countTreeLevels(vendorParent.getChildren(), 0);

        if (person.getSalesMonthly() > 400000 && childrenSales > 2000000 &&  childWithSilver != null && treeLevels >= 3) {
            return true;
        }

        return false;
    }

    public boolean checkSalesByLevel(List children, int level) {
        ListNode current = children.getHead();

        while (current != null) {
            if (level == 1 && current.getTreeNode().getVendor().getSalesMonthly() > 300000) {
                return true;
            }

            if (!current.getTreeNode().getChildren().isEmpty()) {
                checkSalesByLevel(current.getTreeNode().getChildren(), ++level);
            }

            current = current.getNext();
        }

        return false;
    }

    public double calculateChildrenSales(List children, double sales) {
        ListNode current = children.getHead();

        while (current != null) {
            sales += current.getTreeNode().getVendor().getSalesMonthly();

            if (!current.getTreeNode().getChildren().isEmpty()) {
                return calculateChildrenSales(current.getTreeNode().getChildren(), sales);
            }

            current = current.getNext();
        }

        return sales;
    }

    public int countTreeLevels(List children, int level) {
        ListNode current = children.getHead();

        while (current != null) {
            if (!current.getTreeNode().getChildren().isEmpty()) {
                return countTreeLevels(current.getTreeNode().getChildren(), ++level);
            }

            current = current.getNext();
        }

        return level;
    }

    public int countChildrenByLevel(List children, int level, int counter) {
        ListNode current = children.getHead();

        while (current != null) {
            if (level > 1) break;

            if (level == 1) {
                counter++;
            }

            if (!current.getTreeNode().getChildren().isEmpty()) {
                return countChildrenByLevel(current.getTreeNode().getChildren(), ++level, counter);
            }

            current = current.getNext();
        }

        return counter;
    }

    public TreeNode existsChildWithRank(List children, int level, Rank rank) {
        ListNode current = children.getHead();

        while (current != null) {
            if (current.getTreeNode().getVendor().getCurrentRank() == rank) {
                return current.getTreeNode();
            }

            if (!current.getTreeNode().getChildren().isEmpty()) {
                return existsChildWithRank(current.getTreeNode().getChildren(), ++level, rank);
            }

            current = current.getNext();
        }

        return null;
    }

}
