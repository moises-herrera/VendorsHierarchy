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

    public void addFirst(TreeNode treeNode) {
        head = new ListNode(treeNode, head);
        if (tail == null) tail = head;
    }

    public void add(TreeNode treeNode) {
        if (!isEmpty()) {
            tail.setNext(new ListNode(treeNode, null));
            tail = tail.getNext();
        } else {
            head = tail = new ListNode(treeNode, null);
        }
    }

    public void removeNodeByData(long cedula) {
        if (isEmpty()) return;

        ListNode current = head;
        ListNode previous = null;

        if (current != null && current.getTreeNode().getVendor().getCedula() == cedula) {
            head = current.getNext();
            return;
        }

        while (current != null && current.getTreeNode().getVendor().getCedula() != cedula) {
            previous = current;
            current = current.getNext();
        }

        if (current == null) {
            return;
        }

        previous.setNext(current.getNext());
    }

    public void removeAllNodesByData(long cedula) {
        if (isEmpty()) return;

        ListNode current = head;
        ListNode previous = null;

        while (current != null && current.getTreeNode().getVendor().getCedula() == cedula) {
            head = current.getNext();
            current = head;
        }

        while (current != null) {
            while (current != null && current.getTreeNode().getVendor().getCedula() != cedula) {
                previous = current;
                current = current.getNext();
            }

            if (current == null)
                return;

            previous.setNext(current.getNext());
            current = previous.getNext();
        }
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
