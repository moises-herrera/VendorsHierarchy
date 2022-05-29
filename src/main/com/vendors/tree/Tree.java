package main.com.vendors.tree;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public boolean isEmpty() {
        return root == null;
    }

    public JSONObject serializeTree() {
        JSONObject rootJSON = root.getVendor().toJSON();
        recursiveSerialize(root, rootJSON);

        return rootJSON;
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

            TreeNode parent = find(parentId);
            if (parent != null) {
                parent.addChild(newNode);
            }
        }
    }

    public TreeNode find(long nodeId) {
        TreeNode foundNode = null;

        if (!isEmpty()) {
            foundNode = recursiveFind(root, nodeId);
        }

        return foundNode;
    }

    public TreeNode recursiveFind(TreeNode currentNode, long nodeId) {
        TreeNode resultNode = null;
        int i = 0;

        if (currentNode.getVendor().getCedula() == nodeId) {
            resultNode = currentNode;
        } else if (currentNode.hasChildren()) {
            i = 0;
            while (resultNode == null && i < currentNode.getNumberChildren()) {
                resultNode = recursiveFind(currentNode.getChildAt(i), nodeId);
                i++;
            }
        }

        return resultNode;
    }

    public void assignRanks(TreeNode localeRoot) {
        ListNode current = localeRoot.getChildren().getHead();
        Vendor person = localeRoot.getVendor();

        if (verifyRankCopper(person)) {
            person.setCurrentRank(Rank.COBRE);
        }

        if (verifyRankBronze(localeRoot)) {
            person.setCurrentRank(Rank.BRONCE);
        }

        if (verifyRankSilver(localeRoot)) {
            person.setCurrentRank(Rank.PLATA);
        }

        if (verifyRankGold(localeRoot)) {
            person.setCurrentRank(Rank.ORO);
        }

        while (current != null) {
            assignRanks(current.getTreeNode());

            current = current.getNext();
        }
    }

    public void assignCommissions(TreeNode localeRoot) {
        ListNode current = localeRoot.getChildren().getHead();
        Vendor person = localeRoot.getVendor();

        person.assignPersonalCommission();
        person.assignLevelUpCommission();

        while (current != null) {
            assignCommissions(current.getTreeNode());

            current = current.getNext();
        }
    }

    public boolean verifyRankCopper(Vendor person) {
        if (person.getSalesMonthly() >= 0) {
            return true;
        }

        return false;
    }

    public boolean verifyRankBronze(TreeNode treeNode) {
        double salesInLevelOne = calculateChildrenSalesByLevel(treeNode.getChildren(), 1);

        if (treeNode.getVendor().getSalesMonthly() > 200000 && salesInLevelOne > 300000) {
            return true;
        }

        return false;
    }

    public boolean verifyRankSilver(TreeNode treeNode) {
        double childrenSales = calculateChildrenSales(treeNode.getChildren());
        int childrenInLevelOne = countChildrenByLevel(treeNode.getChildren(), 1);

        if (treeNode.getVendor().getSalesMonthly() > 300000 && childrenSales > 1000000 && childrenInLevelOne >= 3) {
            return true;
        }

        return false;
    }

    public boolean verifyRankGold(TreeNode treeNode) {
        double childrenSales = calculateChildrenSales(treeNode.getChildren());
        TreeNode childWithSilver = existsChildWithRank(treeNode.getChildren(), Rank.PLATA);
        int treeLevels = countTreeLevels(treeNode.getChildren());

        if (treeNode.getVendor().getSalesMonthly() > 400000 && childrenSales > 2000000 && childWithSilver != null && treeLevels >= 3) {
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
            vendorParent = find(person.getCedula());
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

    public void recursiveSerialize(TreeNode treeNode, JSONObject vendor) {
        ListNode current = treeNode.getChildren() != null ? treeNode.getChildren().getHead() : null;
        JSONArray array = new JSONArray();
        vendor.put("children", array);

        while (current != null) {
            JSONObject currentVendor = current.getTreeNode().getVendor().toJSON();
            array.put(currentVendor);

            recursiveSerialize(current.getTreeNode(), currentVendor);

            current = current.getNext();
        }
    }

}
