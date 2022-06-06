package main.com.vendors.app.controllers;

import main.com.vendors.enums.Rank;
import main.com.vendors.list.List;
import main.com.vendors.list.ListNode;
import main.com.vendors.models.Vendor;
import main.com.vendors.tree.Tree;
import main.com.vendors.tree.TreeNode;

import org.json.JSONObject;

import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;


public class VendorController {

    public VendorController() {
    }

    public Object createTree(Request request, Response response) throws ServletException, IOException {
        response.type("application/json");
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
        HttpServletRequest requestRaw = request.raw();
        requestRaw.setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
        Part file = requestRaw.getPart("file");

        List vendorsList = new List();
        Tree vendorsTree = new Tree();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }

        while (true) {
            try {
                String line = reader.readLine();
                System.out.println("Line: " + line);
                if (line == null) break;
                String[] attributes = line.trim().split("\t");

                long cedula = Long.parseLong(attributes[0]);
                String name = attributes[1];
                Rank currentRank = Rank.valueOf(attributes[2].toUpperCase());
                double salesMonthly = Double.parseDouble(attributes[3]);
                long parentId = 0;
                if (attributes.length == 5)
                    parentId = Long.parseLong(attributes[4]);

                Vendor person = new Vendor(cedula, name, currentRank, salesMonthly, parentId);
                vendorsList.add(new TreeNode(person));

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }

        insertNodes(vendorsList, vendorsTree, 0);
        vendorsTree.assignRanks(vendorsTree.getRoot());
        vendorsTree.assignCommissions(vendorsTree.getRoot());

        JSONObject vendorsJSON = vendorsTree.serializeTree();

        return vendorsJSON;
    }

    private void insertNodes(List list, Tree tree, long parentId) {
        TreeNode localeRoot = list.findByParentId(parentId);
        ListNode current = list.getHead();

        if (parentId == 0) {
            tree.insert(localeRoot, parentId);
        }

        while (current != null) {
            if (current.getTreeNode().getVendor().getParentId() == parentId) {
                tree.insert(current.getTreeNode(), parentId);
                insertNodes(list, tree, current.getTreeNode().getVendor().getCedula());
            }
            current = current.getNext();
        }
    }

}
