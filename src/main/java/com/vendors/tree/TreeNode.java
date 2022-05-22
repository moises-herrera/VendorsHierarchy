package main.java.com.vendors.tree;

import main.java.com.vendors.list.List;
import main.java.com.vendors.models.Vendor;

public class TreeNode {
    public Vendor vendor;
    public List children;

    public TreeNode (Vendor data) {
        vendor = data;
        children = new List();
    }
}
