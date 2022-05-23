package main.java.com.vendors.tree;

import main.java.com.vendors.list.List;
import main.java.com.vendors.models.Vendor;

public class TreeNode {
    private Vendor vendor;
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

    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    public void addChild(TreeNode node) {
        this.children.add(node);
    }
}
