package main.com.vendors.list;

import main.com.vendors.tree.TreeNode;

public class List {
    private ListNode head;
    private ListNode tail;

    public List() {
        this.head = this.tail = null;
    }

    public ListNode getHead() {
        return head;
    }

    public void setHead(ListNode head) {
        this.head = head;
    }

    public ListNode getTail() {
        return tail;
    }

    public void setTail(ListNode tail) {
        this.tail = tail;
    }

    public void add(TreeNode treeNode) {
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

    public void printList() {
        if (isEmpty()) return;
        ListNode current = head;

        while (current != null) {
            System.out.println(current.getTreeNode().getVendor().getName());
            current = current.getNext();
        }
    }
}
