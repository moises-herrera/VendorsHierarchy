package main.com.vendors.app;

import static spark.Spark.*;

import main.com.vendors.app.controllers.VendorController;
import spark.Filter;

public class Server {
    public static void main(String[] args) {
        new Server();
    }

    Server() {
        setCorsHeaders();
        initializeRoutes();
    }

    private void setCorsHeaders() {
        after((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Methods", "POST,OPTIONS");
            response.header("Access-Control-Allow-Origin", "http://localhost:3000");
            response.header("Access-Control-Allow-Headers", "Content-Type,Authorization,Content-Length,Accept,Origin,");
            response.header("Access-Control-Allow-Credentials", "true");
        });
    }

    private void initializeRoutes() {
        VendorController vendorController = new VendorController();

        post("/vendors", vendorController::createTree);
    }
}
