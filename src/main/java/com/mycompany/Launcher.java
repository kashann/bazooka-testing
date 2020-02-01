package com.mycompany;

import java.util.ResourceBundle;

import org.apache.camel.main.Main;

public class Launcher {
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(Launcher.class.getName());
    public static void main(String... args) throws Exception {
        Main main = new Main();
        
        double latitude = Double.parseDouble(BUNDLE.getString("latitude"));
        double longitude = Double.parseDouble(BUNDLE.getString("longitude"));
        double distance = Double.parseDouble(BUNDLE.getString("distance"));
        int port = Integer.parseInt(BUNDLE.getString("port"));
        
        main.addRouteBuilder(new CamelRoute(latitude, longitude, distance, port));
        main.run(args); //starts in localhost:8080
    }
}
