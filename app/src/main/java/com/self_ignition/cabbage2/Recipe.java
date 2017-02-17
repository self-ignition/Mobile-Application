package com.self_ignition.cabbage2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.self_ignition.cabbage2.SeverRequests;
/**
 * Created by r-bur on 15/02/2017.
 */

public class Recipe implements VolleyCallback {
    RecipeReadyCallback callback;

    private String title = "none";
    private String prepTime = "none";
    private String cookTime = "none";
    private String author = "none";
    private String yield = "none";
    private String imageURL = "none";
    private Bitmap image = null;

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
    public Bitmap getImage() { return image; }
    public String getImageURL() {
        return imageURL;
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
    public void setImage(Bitmap image) { this.image = image; }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setRecipe(Context context, String term, final RecipeReadyCallback callback) {
        this.callback = callback;
        term = term.replace(" ", "%20");
        String url = "http://computing.derby.ac.uk/~cabbage/recipe.php?terms=" + term;
        SeverRequests ser = new SeverRequests();
        ser.GetRecipe(context, url, this);
    }
    public void setRandomRecipe(Context context, final RecipeReadyCallback callback){
        this.callback = callback;
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

        //RECIPE TABLE
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

        //INGREDIENTS TABLE
        try
        {
            //Spilt second table into ingredients;
            parts = Arrays.asList(tables.get(1).split("\\|"));
            for (String s: this.getIngredients()) {
                Log.i("INGREDIENTS", s);
            }
            this.setIngredients(parts);
        }
        catch (Exception e)
        {
            Log.e("Recipe Exception", e.getMessage());
        }

        //METHOD TABLE
        try
        {
            //split third table into steps;
            parts = Arrays.asList(tables.get(2).split("\\|"));
            this.setSteps(parts);
        }
        catch (Exception e)
        {
            Log.e("Recipe Exception", e.getMessage());
        }

        //IMAGE TABLE
        this.setImageURL(tables.get(3));
        new downloadImage(this).execute();
    }
}

class downloadImage extends AsyncTask<String, Void, String>{

    Recipe r = null;

    public downloadImage(Recipe r){
        this.r = r;
    }

    @Override
    protected String doInBackground(String... params) {
        //download the image from the url in the image table;
        try {
            Bitmap x;

            HttpURLConnection connection = (HttpURLConnection) new URL(r.getImageURL()).openConnection();
            connection.setRequestProperty("User-agent", "Mozilla/4.0");

            connection.connect();
            InputStream input = connection.getInputStream();

            x = BitmapFactory.decodeStream(input);
            r.setImage(x);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        r.callback.onReady(r);
    }
}
