package edu.eci.Arep;

import edu.eci.Arep.HttpServer;

public class WebAplication {
        
    public static void main(String[] args) {
        staticfiles("/webroot");
        //get("/hello", (req, resp) -> "Hello " + req.getValues("name"));
        get("/pi", (req, resp) -> {
            return String.valueOf(Math.PI); 
        });

        HttpServer.start(args);
    }

}
