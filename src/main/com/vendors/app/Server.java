package main.com.vendors.app;

import static spark.Spark.*;

import spark.Filter;

public class Server {
    public static void main(String[] args) {
        after((Filter) (request, response) -> {
            response.header("Access-Control-Allow-Origin", "http://localhost:3000");
            response.header("Access-Control-Allow-Methods", "POST");
            response.type("application/json");
        });

    }
}
