package com.example.cocktail.request;

import com.example.cocktail.model.CocktailEntity;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.CompletableFuture;

public class ApiRequest {

    /**
     * Obtiene a traves de llamada a Api los nombres de todas las provincias
     *
     * @return
     */
    public static CompletableFuture<ObservableList<String>> getNamesProvincesAsync() {
        return CompletableFuture.supplyAsync(() -> {
            ObservableList<String> listProvinces = FXCollections.observableArrayList();
            String strUrl = "https://apiv1.geoapi.es/provincias?type=JSON&key=&sandbox=1";
            try {
                JSONObject responseJSON = DataProvider.getJSONObject(strUrl); //Objetien el JSON Object
                JSONArray responseJSONArray = new JSONArray(responseJSON.getJSONArray("data")); //Aislala lista de provincias
                responseJSONArray.forEach(item -> { //Recorre los JSON de provincia y saca el  nombre
                    if (item instanceof JSONObject) {
                        String province = (String) ((JSONObject) item).get("PRO");
                        listProvinces.add(province);
                    }
                });
            } catch (MalformedURLException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (URISyntaxException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            }
            return listProvinces; //retorna lista con nombre de provincias
        });
    }

    public static CompletableFuture<ObservableList<String>> getAllNamesCategoriesCocktailsAsync() {
        return CompletableFuture.supplyAsync(() -> {
            ObservableList<String> listCategories = FXCollections.observableArrayList();
            String strUrl = "https://www.thecocktaildb.com/api/json/v1/1/list.php?c=list";

            try {
                JSONObject responseJSON = DataProvider.getJSONObject(strUrl); //Objetien el JSON Object
                JSONArray responseJSONArray = new JSONArray(responseJSON.getJSONArray("drinks")); //Aislala lista de categorias
                responseJSONArray.forEach(item -> { //Recorre los JSONArray de categorias
                    if (item instanceof JSONObject) {
                        String category = (String) ((JSONObject) item).get("strCategory");
                        listCategories.add(category);
                        //System.out.println(category);
                    }
                });
            } catch (MalformedURLException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (URISyntaxException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            }
            return listCategories;
        });
    }

    public static CompletableFuture<ObservableList<String>> getAllNamesTypesGlassCocktailsAsync() {
        return CompletableFuture.supplyAsync(() -> {
            ObservableList<String> listTypesGlass = FXCollections.observableArrayList();
            String strUrl = "https://www.thecocktaildb.com/api/json/v1/1/list.php?g=list";
            try {
                JSONObject responseJSON = DataProvider.getJSONObject(strUrl); //Objetien el JSON Object
                JSONArray responseJSONArray = new JSONArray(responseJSON.getJSONArray("drinks")); //Aislala lista de categorias
                responseJSONArray.forEach(item -> { //Recorre los JSONArray de categorias
                    if (item instanceof JSONObject) {
                        String typeGlass = (String) ((JSONObject) item).get("strGlass");
                        listTypesGlass.add(typeGlass);
                    }
                });
            } catch (MalformedURLException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (URISyntaxException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            }
            return listTypesGlass;
        });
    }

    public static CompletableFuture<ObservableList<String>> getAllNamesIngredientsCocktails() {
        return CompletableFuture.supplyAsync(() -> {
            ObservableList<String> listIngredients = FXCollections.observableArrayList();
            String strUrl = "https://www.thecocktaildb.com/api/json/v1/1/list.php?i=list";
            try {
                JSONObject responseJSON = DataProvider.getJSONObject(strUrl); //Objetien el JSON Object
                JSONArray responseJSONArray = new JSONArray(responseJSON.getJSONArray("drinks")); //Aislala lista de categorias
                responseJSONArray.forEach(item -> { //Recorre los JSONArray de categorias
                    if (item instanceof JSONObject) {
                        String ingredient = (String) ((JSONObject) item).get("strIngredient1");
                        listIngredients.add(ingredient);
                    }
                });
            } catch (MalformedURLException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (URISyntaxException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            }
            return listIngredients;
        });
    }

    public static CompletableFuture<ObservableList<String>> getAllNamesAlcoholicGraduation() {
        return CompletableFuture.supplyAsync(() -> {
            ObservableList<String> listAlcoholicGraduations = FXCollections.observableArrayList();
            String strUrl = "https://www.thecocktaildb.com/api/json/v1/1/list.php?a=list";
            try {
                JSONObject responseJSON = DataProvider.getJSONObject(strUrl); //Objetien el JSON Object
                JSONArray responseJSONArray = new JSONArray(responseJSON.getJSONArray("drinks")); //Aislala lista de categorias
                responseJSONArray.forEach(item -> { //Recorre los JSONArray de categorias
                    if (item instanceof JSONObject) {
                        String alcoholicGraduation = (String) ((JSONObject) item).get("strAlcoholic");
                        listAlcoholicGraduations.add(alcoholicGraduation);
                    }
                });
            } catch (MalformedURLException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (URISyntaxException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            }
            return listAlcoholicGraduations;
        });
    }

    public static CompletableFuture<ObservableList<CocktailEntity>> getDrinkEntityByCategoryAsync(String category) {
        return CompletableFuture.supplyAsync(() -> {
            ObservableList<CocktailEntity> listDrinkByCategory = FXCollections.observableArrayList();
            String strUrl = String.format("https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=%s", category);
            try {
                JSONObject responseJSON = DataProvider.getJSONObject(strUrl); //Objetien el JSON Object
                JSONArray responseJSONArray = new JSONArray(responseJSON.getJSONArray("drinks")); //Aislala lista de categorias
                Gson gson = new Gson();
                responseJSONArray.forEach(item -> { //Recorre los JSONArray de categorias
                    if (item instanceof JSONObject) {
                        CocktailEntity cocktailEntity = gson.fromJson(item.toString(), CocktailEntity.class);
                        listDrinkByCategory.add(cocktailEntity);
                    }
                });
            } catch (MalformedURLException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (URISyntaxException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            }
            return listDrinkByCategory;
        });
    }

    public static CompletableFuture<ObservableList<CocktailEntity>> getDrinkEntityByTypeGlassAsync(String typeGlass) {
        return CompletableFuture.supplyAsync(() -> {
            ObservableList<CocktailEntity> listDrinkByTypeGlass = FXCollections.observableArrayList();
            String strUrl = String.format("https://www.thecocktaildb.com/api/json/v1/1/filter.php?g=%s", typeGlass);
            try {
                JSONObject responseJSON = DataProvider.getJSONObject(strUrl); //Objetien el JSON Object
                JSONArray responseJSONArray = new JSONArray(responseJSON.getJSONArray("drinks")); //Aislala lista de categorias
                Gson gson = new Gson();
                responseJSONArray.forEach(item -> { //Recorre los JSONArray de categorias
                    if (item instanceof JSONObject) {
                        CocktailEntity cocktailEntity = gson.fromJson(item.toString(), CocktailEntity.class);
                        listDrinkByTypeGlass.add(cocktailEntity);
                    }
                });
            } catch (MalformedURLException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (URISyntaxException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            }
            return listDrinkByTypeGlass;
        });
    }

    public static CompletableFuture<ObservableList<CocktailEntity>> getDrinkEntityByIngredients(String ingredient) {
        return CompletableFuture.supplyAsync(() -> {
            ObservableList<CocktailEntity> listDrinkByIngredient = FXCollections.observableArrayList();
            String strUrl = String.format("https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=%s", ingredient);
            try {
                JSONObject responseJSON = DataProvider.getJSONObject(strUrl); //Objetien el JSON Object
                JSONArray responseJSONArray = new JSONArray(responseJSON.getJSONArray("drinks")); //Aislala lista de categorias
                Gson gson = new Gson();
                responseJSONArray.forEach(item -> { //Recorre los JSONArray de categorias
                    if (item instanceof JSONObject) {
                        CocktailEntity cocktailEntity = gson.fromJson(item.toString(), CocktailEntity.class);
                        listDrinkByIngredient.add(cocktailEntity);
                    }
                });
            } catch (MalformedURLException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (URISyntaxException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            }
            return listDrinkByIngredient;
        });
    }
    public static CompletableFuture<ObservableList<CocktailEntity>> getDrinkEntityByAlcoholicGraduation(String graduation){
        return CompletableFuture.supplyAsync(() -> {
            ObservableList<CocktailEntity> listDrinkByAlcoholicGraduation = FXCollections.observableArrayList();
            String strUrl = String.format("https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=%s", graduation);
            try {
                JSONObject responseJSON = DataProvider.getJSONObject(strUrl); //Objetien el JSON Object
                JSONArray responseJSONArray = new JSONArray(responseJSON.getJSONArray("drinks")); //Aislala lista de categorias
                Gson gson = new Gson();
                responseJSONArray.forEach(item -> { //Recorre los JSONArray de categorias
                    if (item instanceof JSONObject) {
                        CocktailEntity cocktailEntity = gson.fromJson(item.toString(), CocktailEntity.class);
                        listDrinkByAlcoholicGraduation.add(cocktailEntity);
                    }
                });
            } catch (MalformedURLException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (IOException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            } catch (URISyntaxException e) {
                e.fillInStackTrace();
                System.out.println(e.getMessage());
            }
            return listDrinkByAlcoholicGraduation;
        });
    }
}
