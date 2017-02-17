package com.self_ignition.cabbage2;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.google.android.gms.nearby.messages.internal.Update;

import java.util.ArrayList;
import java.util.List;

import static com.self_ignition.cabbage2.R.id.button;
import static com.self_ignition.cabbage2.R.id.default_activity_button;

public class HomeActivity extends AppCompatActivity implements RecipeReadyCallback{

    List<Recipe> recipes = new ArrayList<Recipe>();
    int buttonToSet = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        UpdateRecipes();
    }
    public void NewRecipies(View v) {
        UpdateRecipes();
    }

    private void UpdateRecipes() {
        recipes.clear();
        buttonToSet = 1;

        for (int i = 0; i < 6; i++)
        {
            Recipe r = new Recipe();
            r.setRandomRecipe(this, this);
            recipes.add(r);
        }
    }

    @Override
    public void onReady(Recipe r) {
        final Recipe recipe = r;
        Button button;

        switch(buttonToSet){
            case 1:
                button = (Button) findViewById(R.id.home_button_one);
                break;
            case 2:
                button = (Button) findViewById(R.id.home_button_two);
                break;
            case 3:
                button = (Button) findViewById(R.id.home_button_three);
                break;
            case 4:
                button = (Button) findViewById(R.id.home_button_four);
                break;
            case 5:
                button = (Button) findViewById(R.id.home_button_five);
                break;
            case 6:
                button = (Button) findViewById(R.id.home_button_six);
                break;
            default:
                button = (Button) findViewById(R.id.home_button_one);
                Log.e("Button set", "OOB Exception: Value for int was " + Integer.toString(buttonToSet));
        }

        button.setText(recipe.getTitle());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), RecipeActivity.class);
                i.putExtra("recipe-title", recipe.getTitle());
                Log.i("Intent Extra", "Title of recipe:" + recipe.getTitle());
                startActivity(i);
            }
        });
        button.setBackground(new BitmapDrawable(recipe.getImage()));
        buttonToSet++;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    public void searchResults(View v) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        startActivity(intent);
    }

    public void recipe(View v) {
        Intent intent = new Intent(this, RecipeActivity.class);
        startActivity(intent);
    }
}

