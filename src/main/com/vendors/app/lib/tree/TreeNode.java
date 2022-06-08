package com.vendors.app.lib.tree;

import com.vendors.app.lib.list.List;
import com.vendors.app.lib.list.ListNode;
import com.vendors.app.models.Vendor;

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
