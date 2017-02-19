package com.self_ignition.cabbage2;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.R.id.list;
import static android.media.CamcorderProfile.get;

public class SearchResultsActivity extends AppCompatActivity  implements SearchResultCallback {

    SearchResult searchResults = new SearchResult(this);

    List<Recipe> recipies = new ArrayList<>();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Some default values to prevent crash
        Recipe r = new Recipe();
        r.setTitle("Loading Please Wait...");
        r.setPrepTime("...");
        recipies.add(r);

        //Set list view values
        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(new adapter(this, recipies));

        //Set the terms and commence search
        String terms = getIntent().getStringExtra("query");
        searchResults.Search(terms, this);
    }


    @Override
    public void onSearchComplete() {
        //// TODO: 18/02/2017 fill out list of recipes for list view

        recipies = searchResults.results;

        for (Recipe r: recipies) {
            Log.i("RESULTS", "onSearchComplete: " + r.getTitle());
        }

        UpdateFields();
    }

    private void UpdateFields() {
        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(new adapter(this, recipies));
        ((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
    }
}

class adapter extends ArrayAdapter<Recipe> {

    Context context;
    List<Recipe> recipes;

    adapter(Context c, List<Recipe> recipes) {
        super(c, R.layout.single_row, recipes);
        this.context = c;
        this.recipes = recipes;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Recipe getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_row, parent, false);
        TextView title = (TextView) row.findViewById(R.id.textView2);
        TextView prepTime = (TextView) row.findViewById(R.id.textView3);

        title.setText(recipes.get(position).getTitle());
        prepTime.setText(recipes.get(position).getPrepTime());

        return row;
    }
}

