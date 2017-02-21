package com.self_ignition.cabbage2;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

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
        UpdateRecipes();

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    public void NewRecipes(View v) {
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

    public void doMySearch(String query) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        MenuItem searchViewItem = menu.findItem(R.id.search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                doMySearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.refresh:
                UpdateRecipes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onReady(Recipe r) {
        final Recipe recipe = r;
        ImageView button;
        TextView text;

        switch(buttonToSet){
            case 1:
                button = (ImageView) findViewById(R.id.picture1);
                text = (TextView) findViewById(R.id.text1);
                break;
            case 2:
                button = (ImageView) findViewById(R.id.picture2);
                text = (TextView) findViewById(R.id.text2);
                break;
            case 3:
                button = (ImageView) findViewById(R.id.picture3);
                text = (TextView) findViewById(R.id.text3);
                break;
            case 4:
                button = (ImageView) findViewById(R.id.picture4);
                text = (TextView) findViewById(R.id.text4);
                break;
            case 5:
                button = (ImageView) findViewById(R.id.picture5);
                text = (TextView) findViewById(R.id.text5);
                break;
            case 6:
                button = (ImageView) findViewById(R.id.picture6);
                text = (TextView) findViewById(R.id.text6);
                break;
            default:
                button = (ImageView) findViewById(R.id.picture1);
                text = (TextView) findViewById(R.id.text1);
                Log.e("Button set", "OOB Exception: Value for int was " + Integer.toString(buttonToSet));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), RecipeActivity.class);
                i.putExtra("recipe-id", recipe.getId());
                Log.i("Intent Extra", "id of recipe:" + recipe.getId());
                startActivity(i);
            }
        });
        button.setBackground(new BitmapDrawable(recipe.getImage()));
        text.setText(recipe.getTitle());
        buttonToSet++;
    }
}

