package comself_ignition.httpsgithub.meetneat.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.Recipe;
import comself_ignition.httpsgithub.meetneat.SearchResult;
import comself_ignition.httpsgithub.meetneat.SearchResultCallback;

import static comself_ignition.httpsgithub.meetneat.R.id.toolbar;

public class SearchResultsActivity extends AppCompatActivity implements SearchResultCallback {

    SearchResult searchResults = new SearchResult(this);

    List<Recipe> recipes = new ArrayList<>();
    ListView list;

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_search_results);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set list view values
        list=(ListView) findViewById(R.id.listView);
        list.setAdapter(new adapter(this, recipes));

        //Set the terms and commence search
        String terms = this.getIntent().getStringExtra("query");
        searchResults.Search(terms, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);


        MenuItem item = menu.findItem(R.id.refresh);
        item.setVisible(false);

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

    public void doMySearch(String query) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
    }

    public void onSearchComplete(Recipe r) {
        recipes.add(r);

        UpdateFields();
    }

    private void UpdateFields() {
        list=(ListView) findViewById(R.id.listView);
        list.setAdapter(new adapter(this, recipes));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the recipe we want to load.
                Recipe r = (Recipe) list.getItemAtPosition(position);

                //Start the recipe activity for the recipe we chose.
                Intent i = new Intent(getBaseContext(), RecipeActivity.class);
                i.putExtra("recipe-id", r.getId());
                startActivity(i);
            }
        });
        ((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
    }
}

class adapter extends ArrayAdapter<Recipe> {
    Context context;
    List<Recipe> recipes;

    adapter(Context c, List<Recipe> recipes) {
        super(c, R.layout.activity_search_results_row, recipes);
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
        View row = inflater.inflate(R.layout.activity_search_results_row, parent, false);
        TextView title = (TextView) row.findViewById(R.id.textView2);
        TextView prepTime = (TextView) row.findViewById(R.id.textView3);
        TextView servings = (TextView) row.findViewById(R.id.textView4);
        ImageView image = (ImageView) row.findViewById(R.id.imageView);

        title.setText(recipes.get(position).getTitle());
        prepTime.setText(recipes.get(position).getPrepTime());
        servings.setText(recipes.get(position).getYield());
        image.setBackground(new BitmapDrawable(recipes.get(position).getImage()));

        return row;
    }
}
