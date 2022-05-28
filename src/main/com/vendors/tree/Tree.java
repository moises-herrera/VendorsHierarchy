package main.com.vendors.tree;

import main.com.vendors.enums.Rank;
import main.com.vendors.list.List;
import main.com.vendors.list.ListNode;
import main.com.vendors.models.Vendor;

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

    public void assignRanks(TreeNode localeRoot) {
        ListNode current = localeRoot.getChildren().getHead();

        if (verifyRankCopper(localeRoot.getVendor())) {
            localeRoot.getVendor().setCurrentRank(Rank.COBRE);
        }

        if (verifyRankBronze(localeRoot.getVendor())) {
            localeRoot.getVendor().setCurrentRank(Rank.BRONCE);
        }

        if (verifyRankSilver(localeRoot.getVendor())) {
            localeRoot.getVendor().setCurrentRank(Rank.PLATA);
        }

        if (verifyRankGold(localeRoot.getVendor())) {
            localeRoot.getVendor().setCurrentRank(Rank.ORO);
        }

        while (current != null) {
            if (!current.getTreeNode().getChildren().isEmpty()) {
                assignRanks(current.getTreeNode());
            }

            current = current.getNext();
        }
    }

    public void assignCommissions(TreeNode localeRoot) {
        ListNode current = localeRoot.getChildren().getHead();

        localeRoot.getVendor().assignPersonalCommission();
        localeRoot.getVendor().assignLevelUpCommission();

        while (current != null) {
            if (!current.getTreeNode().getChildren().isEmpty()) {
                assignCommissions(current.getTreeNode());
            }

            current = current.getNext();
        }
    }

    public boolean verifyRankCopper(Vendor person) {
        if (person.getSalesMonthly() >= 0) {
            return true;
        }

        return false;
    }

    public boolean verifyRankBronze(Vendor person) {
        TreeNode vendorParent = root;

        if (person.getCedula() != root.getVendor().getCedula()) {
            vendorParent = findParentById(root.getChildren(), person.getCedula());
        }

        double salesInLevelOne = calculateChildrenSalesByLevel(vendorParent.getChildren(), 1);

        if (person.getSalesMonthly() > 200000 && salesInLevelOne > 300000) {
            return true;
        }

        return false;
    }

    public boolean verifyRankSilver(Vendor person) {
        TreeNode vendorParent = root;

        if (person.getCedula() != root.getVendor().getCedula()) {
            vendorParent = findParentById(root.getChildren(), person.getCedula());
        }

        double childrenSales = calculateChildrenSales(vendorParent.getChildren());
        int childrenInLevelOne = countChildrenByLevel(vendorParent.getChildren(), 1);

        if (person.getSalesMonthly() > 300000 && childrenSales > 1000000 && childrenInLevelOne >= 3) {
            return true;
        }

        return false;
    }

    public boolean verifyRankGold(Vendor person) {
        TreeNode vendorParent = root;

        if (person.getCedula() != root.getVendor().getCedula()) {
            vendorParent = findParentById(root.getChildren(), person.getCedula());
        }

        double childrenSales = calculateChildrenSales(vendorParent.getChildren());
        TreeNode childWithSilver = existsChildWithRank(vendorParent.getChildren(), Rank.PLATA);
        int treeLevels = countTreeLevels(vendorParent.getChildren());

        if (person.getSalesMonthly() > 400000 && childrenSales > 2000000 && childWithSilver != null && treeLevels >= 3) {
            return true;
        }

        return false;
    }

    public double calculateChildrenSalesByLevel(List children, int checkLevel) {
        return calculateChildrenSalesByLevel(children, 0, 0, checkLevel);
    }

    public double calculateChildrenSalesByLevel(List children, double sales, int currentLevel, int checkLevel) {
        ListNode current = children.getHead();
        ++currentLevel;

        while (current != null) {
            if (currentLevel == checkLevel) {
                sales += current.getTreeNode().getVendor().getSalesMonthly();
            }

            if (!current.getTreeNode().getChildren().isEmpty()) {
                return calculateChildrenSalesByLevel(current.getTreeNode().getChildren(), sales, currentLevel, checkLevel);
            }

            current = current.getNext();
        }

        return sales;
    }

    public double calculateChildrenSales(List children) {
        return calculateChildrenSales(children, 0);
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

    public int countTreeLevels(List children) {
        return countTreeLevels(children, 0);
    }

    public int countTreeLevels(List children, int level) {
        ListNode current = children.getHead();
        ++level;

        while (current != null) {
            if (!current.getTreeNode().getChildren().isEmpty()) {
                return countTreeLevels(current.getTreeNode().getChildren(), level);
            }

            current = current.getNext();
        }

        return level;
    }

    public int countChildrenByLevel(List children, int checkLevel) {
        return countChildrenByLevel(children, 0, checkLevel, 0);
    }

    public int countChildrenByLevel(List children, int level, int checkLevel, int counter) {
        ListNode current = children.getHead();
        ++level;

        while (current != null) {
            if (level > checkLevel) break;

            if (level == checkLevel) {
                counter++;
            }

            if (!current.getTreeNode().getChildren().isEmpty()) {
                return countChildrenByLevel(current.getTreeNode().getChildren(), level, checkLevel, counter);
            }

            current = current.getNext();
        }

        return counter;
    }

    public TreeNode existsChildWithRank(List children, Rank rank) {
        return existsChildWithRank(children, 0, rank);
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

    public double getLevelCommission(Vendor person) {
        TreeNode vendorParent = root;

        if (person.getCedula() != root.getVendor().getCedula()) {
            vendorParent = findParentById(root.getChildren(), person.getCedula());
        }

        double commission = calculateLevelCommission(vendorParent.getChildren());

        return commission;
    }

    public double calculateLevelCommission(List children) {
        return calculateLevelCommission(children, 0, 0);
    }

    public double calculateLevelCommission(List children, int currentLevel, double commission) {
        ListNode current = children.getHead();
        ++currentLevel;

        while (current != null) {
            double childSales = current.getTreeNode().getVendor().getSalesMonthly();
            double percentageLevel = 0;

            if (currentLevel == 1) {
                percentageLevel = 1;
            } else if (currentLevel == 2) {
                percentageLevel = 2;
            } else if (currentLevel >= 3) {
                percentageLevel = 3;
            }

            percentageLevel = percentageLevel / 100;

            commission += childSales * percentageLevel;

            if (!current.getTreeNode().getChildren().isEmpty()) {
                return calculateLevelCommission(current.getTreeNode().getChildren(), currentLevel, commission);
            }

            current = current.getNext();
        }

        return commission;
    }

}
