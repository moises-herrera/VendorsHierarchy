package main.java.com.vendors.list;

import main.java.com.vendors.tree.TreeNode;

public class List {
    ListNode head;
    ListNode tail;

    public List() {
        this.head = this.tail = null;
    }

    public void agregarPunto(TreeNode treeNode) {
        if (!isEmpty()) {
            tail.setNext(new ListNode(treeNode, null));
            tail = tail.getNext();
        } else {
            head = tail = new ListNode(treeNode, null);
        }
    }

    public boolean isEmpty() {
        return head == null;
    }
}
