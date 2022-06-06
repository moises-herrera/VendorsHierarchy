package main.com.vendors.app.controllers;

import main.com.vendors.app.lib.list.List;
import main.com.vendors.app.lib.tree.Tree;

import main.com.vendors.app.utils.DataReader;

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

        List data = DataReader.getListFromFile(reader);
        vendorsTree.insertNodesFromList(data, 0);
        vendorsTree.assignRanks(vendorsTree.getRoot());
        vendorsTree.assignCommissions(vendorsTree.getRoot());

        JSONObject vendorsJSON = vendorsTree.serializeTree();

        return vendorsJSON;
    }

}
