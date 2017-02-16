package com.self_ignition.cabbage2;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.nearby.messages.internal.Update;

import java.util.ArrayList;
import java.util.List;

import static com.self_ignition.cabbage2.R.id.button;

public class HomeActivity extends AppCompatActivity {

    List<Recipe> recipes = new ArrayList<Recipe>();

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
        //THIS LINE IS V' IMPORTANT, IT IS USED TO MAKE THE NEWEST RESULTS APPEAR,
        //IT IS CURRENTLY COMMENTED OUT SO THAT THE GUI ACTUALLY DRAWS THE STRINGS.
        //recipes.clear();

        for (int i = 0; i < 6; i++)
        {
            Recipe r = new Recipe();
            r.setRandomRecipe(this);
            recipes.add(r);
        }

        Button button_1 = (Button) findViewById(R.id.home_button_one);
        button_1.setText(recipes.get(0).getTitle());

        Button button_2 = (Button) findViewById(R.id.home_button_two);
        button_2.setText(recipes.get(1).getTitle());

        Button button_3 = (Button) findViewById(R.id.home_button_three);
        button_3.setText(recipes.get(2).getTitle());

        Button button_4 = (Button) findViewById(R.id.home_button_four);
        button_4.setText(recipes.get(3).getTitle());

        Button button_5 = (Button) findViewById(R.id.home_button_five);
        button_5.setText(recipes.get(4).getTitle());

        Button button_6 = (Button) findViewById(R.id.home_button_six);
        button_6.setText(recipes.get(5).getTitle());
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

