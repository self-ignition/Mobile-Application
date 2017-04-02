package comself_ignition.httpsgithub.meetneat.other;

/**
 * Created by ctrue on 22/02/2017.
 */

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


/**
 * Created by r-bur on 15/02/2017.
 */

public class Recipe implements VolleyCallback {
    RecipeReadyCallback callback;

    private String id = "none";
    private String title = "none";
    private String prepTime = "none";
    private String cookTime = "none";
    private String author = "none";
    private String yield = "Servings Unavailable";
    private String imageURL = "none";
    private Bitmap image = null;
    private String rating = "none";

    private List<String> ingredients = new ArrayList<>();
    private List<String> steps = new ArrayList<>();
    private List<String> reviews = new ArrayList<>();

    public String getId() { return id; }
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
    public String getRating() { return rating; }
    public List<String> getReviews() { return reviews;  }

    public void setId (String id) { this.id = id; }
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
    public void setRating(String rating) { this.rating = rating; }
    public void setReviews(List<String> reviews) {this.reviews = reviews; }

    public void setRecipe(Context context, String term, final RecipeReadyCallback callback) {
        this.callback = callback;
        term = term.replace(" ", "%20").trim();
        String url = "http://computing.derby.ac.uk/~cabbage/recipe.php?terms=" + term;
        Log.e("Result", "setRecipe: " + url);
        ServerRequests ser = new ServerRequests();
        ser.GetRecipe(context, url, this);
    }
    public void setRandomRecipe(Context context, final RecipeReadyCallback callback){
        this.callback = callback;
        String url = "http://computing.derby.ac.uk/~cabbage/randomrecipe.php";
        ServerRequests ser = new ServerRequests();
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
        try {
            Log.d("Result", "shit result: " + result);
            setId(parts.get(0));
            setTitle(parts.get(1));
            setPrepTime(parts.get(2));
            setCookTime(parts.get(3));
            setAuthor(parts.get(4));
            setYield(parts.get(5));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        //INGREDIENTS TABLE
        try {
            //Spilt second table into ingredients;
            parts = Arrays.asList(tables.get(1).split("\\|"));
            this.setIngredients(parts);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //METHOD TABLE
        try {
            //split third table into steps;
            parts = Arrays.asList(tables.get(2).split("\\|"));
            this.setSteps(parts);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //IMAGE TABLE
        try
        {
            this.setImageURL(tables.get(3).replace(" ", "%20"));
            new downloadImage(this, this.callback).execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //Reviews Table
        try {
            //split third table into steps;
            parts = Arrays.asList(tables.get(4).split("\\|"));
            this.setReviews(parts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DownloadImage(RecipeReadyCallback callback){
        if(!this.imageURL.equals("none"))
        {
            new downloadImage(this, callback).execute();
        }
    }
}

class downloadImage extends AsyncTask<String, Void, String>{

    Recipe r = null;
    RecipeReadyCallback callback;

    public downloadImage(Recipe r, RecipeReadyCallback callback){
        this.r = r;
        this.callback = callback;
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

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outWidth = 200;
            options.outHeight = 113;
            x = BitmapFactory.decodeStream(input, null, options);
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
        try {
            callback.onReady(r);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}