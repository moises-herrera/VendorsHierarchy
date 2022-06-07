package main.com.vendors.app.utils;

import main.com.vendors.app.lib.enums.Rank;
import main.com.vendors.app.lib.list.List;
import main.com.vendors.app.lib.tree.TreeNode;
import main.com.vendors.app.models.Vendor;

import java.io.*;

public class DataReader {

    public static List getListFromFile(BufferedReader reader) {
        List dataList = new List();

        try {
            while (true) {
                String line = reader.readLine();
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
                dataList.add(new TreeNode(person));
            }

            reader.close();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return dataList;
    }
}
