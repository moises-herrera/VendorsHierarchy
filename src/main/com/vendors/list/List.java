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

    public int size() {
        ListNode current = head;
        int counter = 0;

        while (current != null) {
            counter++;
            current = current.getNext();
        }

        return counter;
    }

    public TreeNode findByParentId(long id) {
        ListNode current = head;

        while (current != null) {
            if (current.getTreeNode().getVendor().getParentId() == id) {
                return current.getTreeNode();
            }

            current = current.getNext();
        }

        return null;
    }

    public TreeNode getNode(int index) {
        ListNode current = head;
        int i = 0;

        while (current != null) {
            if (i == index) {
                return current.getTreeNode();
            }

            i++;
            current = current.getNext();
        }

        return null;
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
