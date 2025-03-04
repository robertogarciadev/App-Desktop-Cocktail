package com.example.cocktail.request;

import com.example.cocktail.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class DataProvider {

    public  static JSONObject getJSONObject(String urlValue) throws IOException, URISyntaxException {
        URL url = new URI(urlValue).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String responseStr = reader.readLine();
        return new JSONObject(responseStr);
    }

    public static Scene getScene(String resource) {
        Scene scene = null;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(resource));
            scene = new Scene(fxmlLoader.load());

        } catch (IOException e) {
            e.fillInStackTrace();
            e.getCause();
        }
        return scene;
    }
}
