package main.java.com.vendors.tree;

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

}
