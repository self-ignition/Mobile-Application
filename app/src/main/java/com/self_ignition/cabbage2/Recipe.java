package com.self_ignition.cabbage2;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.self_ignition.cabbage2.SeverRequests;
/**
 * Created by r-bur on 15/02/2017.
 */

public class Recipe implements VolleyCallback {
    private String title = "none";
    private String prepTime = "none";
    private String cookTime = "none";
    private String author = "none";
    private String yield = "none";

    private List<String> ingredients = new ArrayList<>();
    private List<String> steps = new ArrayList<>();

    public String getTitle() {
        return title;
    }
    public String getPrepTime() {
        return prepTime;
    }
    public String getCookTime() {
        return cookTime;
    }
    public String getAuthor() {
        return author;
    }
    public String getYield() {
        return yield;
    }
    public List<String> getIngredients() {
        return ingredients;
    }
    public List<String> getSteps() {
        return steps;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }
    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setYield(String yield) {
        this.yield = yield;
    }
    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public void setRecipe(Context context, String url) {
        url = "http://computing.derby.ac.uk/~cabbage/recipe.php?terms=%20Chicken%20chasseur%20with%20creamy%20mash";
        SeverRequests ser = new SeverRequests();
        ser.GetRecipe(context, url, this);
    }
    public void setRandomRecipe(Context context){
        String url = "http://computing.derby.ac.uk/~cabbage/randomrecipe.php";
        SeverRequests ser = new SeverRequests();
        ser.GetRecipe(context, url, this);
    }

    public void onSuccess(String result) {
        List<String> tables = new ArrayList<String>();
        List<String> parts = new ArrayList<String>();

        //Spilt response into parts
        tables = Arrays.asList(result.split("¦"));

        //Spilt first table into parts to set params
        parts = Arrays.asList(tables.get(0).split("\\|"));

        try
        {

            this.setTitle(parts.get(0));
            this.setPrepTime(parts.get(1));
            this.setCookTime(parts.get(2));
            this.setAuthor(parts.get(3));
            this.setYield(parts.get(4));
        }
        catch (IndexOutOfBoundsException e)
        {
            Log.e("Recipe IOOB", e.getMessage());
        }

        try
        {
            //Spilt second table into ingredients;
            parts = Arrays.asList(tables.get(1).split(","));
            this.setIngredients(parts);
        }
        catch (Exception e)
        {
            Log.e("Recipe Exception", e.getMessage());
        }

        try
        {
            //split third table into steps;
            parts = Arrays.asList(tables.get(2).split(","));
            this.setSteps(parts);
        }
        catch (Exception e)
        {
            Log.e("Recipe Exception", e.getMessage());
        }
    }
}

