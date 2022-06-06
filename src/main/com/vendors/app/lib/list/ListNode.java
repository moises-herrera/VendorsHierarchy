package main.com.vendors.app.lib.list;

import main.com.vendors.app.lib.tree.TreeNode;

public class ListNode {
    private TreeNode treeNode;
    private ListNode next;

    public ListNode(TreeNode node) {
        this(node, null);
    }

    public ListNode(TreeNode node, ListNode next) {
        this.treeNode = node;
        this.next = next;
    }

    public TreeNode getTreeNode() {
        return treeNode;
    }

    public void setTreeNode(TreeNode treeNode) {
        this.treeNode = treeNode;
    }

    public ListNode getNext() {
        return next;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }
}
