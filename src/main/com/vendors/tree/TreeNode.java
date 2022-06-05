package main.com.vendors.tree;

import main.com.vendors.list.List;
import main.com.vendors.list.ListNode;
import main.com.vendors.models.Vendor;

public class TreeNode {
    private Vendor vendor;
    private TreeNode parent;
    private List children;

    public TreeNode (Vendor data) {
        vendor = data;
        children = new List();
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public TreeNode getParent() {
        return parent;
    }

    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        ListNode current = children.getHead();

        while (current != null) {
            current.getTreeNode().parent = this;
            current = current.getNext();
        }

        this.children = children;
    }

    public void addChild(TreeNode node) {
        node.parent = this;
        this.children.add(node);
    }

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    public int getNumberChildren() {
        return children.size();
    }

    public TreeNode getChildAt(int index) {
        return children.getNode(index);
    }
}
