package main.com.vendors.tree;

import org.json.JSONArray;
import org.json.JSONObject;

import main.com.vendors.enums.Rank;
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

        if (verifyRankCopper(localeRoot)) {

            if (verifyRankBronze(localeRoot)) {

                if (verifyRankSilver(localeRoot)) {

                    if (verifyRankGold(localeRoot)) {
                        person.setCurrentRank(Rank.ORO);
                    } else {
                        person.setCurrentRank(Rank.PLATA);
                    }

                } else {
                    person.setCurrentRank(Rank.BRONCE);
                }

            } else {
                person.setCurrentRank(Rank.COBRE);
            }
        }

        while (current != null) {
            assignRanks(current.getTreeNode());

            current = current.getNext();
        }
    }

    public void assignCommissions(TreeNode localeRoot) {
        ListNode current = localeRoot.getChildren().getHead();
        Vendor person = localeRoot.getVendor();
        int maxLevel = countTreeLevels(localeRoot);

        person.assignPersonalCommission();
        person.assignLevelUpCommission();
        if (maxLevel > 0) person.assignLevelCommission(maxLevel);

        person.calculateVendorCommission();

        double commission = calculateLevelCommission(localeRoot);
        if (commission > 0) person.addLevelCommission(commission);

        while (current != null) {
            assignCommissions(current.getTreeNode());

            current = current.getNext();
        }
    }

    public boolean verifyRankCopper(TreeNode treeNode) {
        return treeNode.getVendor().getSalesMonthly() >= 0;
    }

    public boolean verifyRankBronze(TreeNode treeNode) {
        double salesInLevelOne = calculateChildrenSalesByLevel(treeNode, 1);

        if (treeNode.getVendor().getSalesMonthly() > 200000 && salesInLevelOne > 300000) {
            return true;
        }

        return false;
    }

    public boolean verifyRankSilver(TreeNode treeNode) {
        double childrenSales = calculateChildrenSales(treeNode);
        int childrenInLevelOne = countChildrenByLevel(treeNode, 1);

        if (treeNode.getVendor().getSalesMonthly() > 300000 && childrenSales > 1000000 && childrenInLevelOne >= 3) {
            return true;
        }

        return false;
    }

    public boolean verifyRankGold(TreeNode treeNode) {
        double childrenSales = calculateChildrenSales(treeNode);
        TreeNode childWithSilver = existsChildWithRank(treeNode, Rank.PLATA);
        int treeLevels = countTreeLevels(treeNode);

        if (treeNode.getVendor().getSalesMonthly() > 400000 && childrenSales > 2000000 && childWithSilver != null && treeLevels >= 3) {
            return true;
        }

        return false;
    }

    public double calculateChildrenSales(TreeNode localeRoot) {
        return calculateChildrenSales(localeRoot, 0, 0);
    }

    public double calculateChildrenSales(TreeNode currentNode, double sales, int level) {
        int i = 0;

        if (level > 0) sales += currentNode.getVendor().getSalesMonthly();

        if (currentNode.hasChildren()) {
            ++level;

            while (i < currentNode.getNumberChildren()) {
                sales = calculateChildrenSales(currentNode.getChildAt(i), sales, level);
                i++;
            }
        }

        return sales;
    }

    public double calculateChildrenSalesByLevel(TreeNode localeRoot, int checkLevel) {
        return calculateChildrenSalesByLevel(localeRoot, 0, 0, checkLevel);
    }

    public double calculateChildrenSalesByLevel(TreeNode currentNode, double sales, int currentLevel, int checkLevel) {
        int i = 0;

        if (currentLevel == checkLevel) {
            sales += currentNode.getVendor().getSalesMonthly();
        }

        if (currentNode.hasChildren()) {
            ++currentLevel;

            while (i < currentNode.getNumberChildren()) {
                sales = calculateChildrenSalesByLevel(currentNode.getChildAt(i), sales, currentLevel, checkLevel);
                i++;
            }
        }

        return sales;
    }

    public int countTreeLevels(TreeNode localeRoot) {
        return countTreeLevels(localeRoot, 0, 0);
    }

    public int countTreeLevels(TreeNode currentNode, int level, int greater) {
        int i = 0;

        if (level > greater) {
            greater = level;
        }

        if (currentNode.hasChildren()) {
            ++level;

            while (i < currentNode.getNumberChildren()) {
                greater = countTreeLevels(currentNode.getChildAt(i), level, greater);
                i++;
            }
        }

        return greater;
    }

    public int countChildrenByLevel(TreeNode localeRoot, int checkLevel) {
        return countChildrenByLevel(localeRoot, 0, checkLevel, 0);
    }

    public int countChildrenByLevel(TreeNode currentNode, int level, int checkLevel, int counter) {
        int i = 0;

        if (currentNode.hasChildren()) {
            ++level;

            while (level <= checkLevel && i < currentNode.getNumberChildren()) {
                if (level == checkLevel) {
                    counter++;
                }
                countChildrenByLevel(currentNode.getChildAt(i), level, checkLevel, counter);
                i++;
            }
        }

        return counter;
    }

    public TreeNode existsChildWithRank(TreeNode localeRoot, Rank rank) {
        return existsChildWithRank(localeRoot, 0, rank);
    }

    public TreeNode existsChildWithRank(TreeNode currentNode, int level, Rank rank) {
        TreeNode found = null;
        int i = 0;

        if (level > 0 && currentNode.getVendor().getCurrentRank() == rank) {
            found = currentNode;
        } else if (currentNode.hasChildren()) {
            ++level;

            while (found == null && i < currentNode.getNumberChildren()) {
                found = existsChildWithRank(currentNode.getChildAt(i), level, rank);
                i++;
            }
        }

        return found;
    }

    public double calculateLevelCommission(TreeNode localeRoot) {
        return calculateLevelCommission(localeRoot, 0, 0);
    }

    public double calculateLevelCommission(TreeNode currentNode, int currentLevel, double commission) {
        int i = 0;

        if (currentLevel > 0) {
            double childSales = currentNode.getVendor().getSalesMonthly();
            double percentageLevel = 0;

            if (currentLevel == 1) {
                percentageLevel = 1;
            } else if (currentLevel == 2) {
                percentageLevel = 2;
            } else {
                percentageLevel = 3;
            }

            percentageLevel = percentageLevel / 100;
            commission += childSales * percentageLevel;
        }

        if (currentNode.hasChildren()) {
            ++currentLevel;

            while (i < currentNode.getNumberChildren()) {
                commission = calculateLevelCommission(currentNode.getChildAt(i), currentLevel, commission);
                i++;
            }
        }

        return commission;
    }

}
