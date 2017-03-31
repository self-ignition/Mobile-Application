package comself_ignition.httpsgithub.meetneat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.activity.LoginActivity;
import comself_ignition.httpsgithub.meetneat.activity.RecipeActivity;
import comself_ignition.httpsgithub.meetneat.activity.SearchResultsActivity;
import comself_ignition.httpsgithub.meetneat.other.MyFriendsDialogFragment;
import comself_ignition.httpsgithub.meetneat.other.Recipe;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.SavedRecipeAction;
import comself_ignition.httpsgithub.meetneat.other.SearchResult;
import comself_ignition.httpsgithub.meetneat.other.SearchResultCallback;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;

import static android.R.id.list;
import static comself_ignition.httpsgithub.meetneat.R.string.recipe;

public class SavedRecipesFragment extends Fragment implements VolleyCallback, SearchResultCallback {
    SearchResult searchResults;
    List<Recipe> recipes = new ArrayList<>();
    ListView list;
    adapter adapter;

    public SavedRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchResults = new SearchResult(getActivity().getBaseContext());
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater = getActivity().getMenuInflater();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_logout:
                Toast.makeText(getContext(), "Logged out", Toast.LENGTH_LONG).show();

                SaveSharedPreference.setLoggedIn(getActivity(), false);

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void doMySearch(String query) {
        Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
        intent.putExtra("query", query);
        getActivity().startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_search_results, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        recipes.clear();
        ServerRequests sr = new ServerRequests();
        sr.GetSavedRecipes(getActivity(), this, SavedRecipeAction.get, SaveSharedPreference.getUserName(getActivity()));
    }

    public void onSearchComplete(Recipe r) {
        recipes.add(r);

        UpdateFields();
    }

    private void UpdateFields() {
        list=(ListView) getActivity().findViewById(R.id.listView);
        list.setAdapter(new adapter(getActivity(), recipes));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the recipe we want to load.
                Recipe r = (Recipe) list.getItemAtPosition(position);
                //Start the recipe activity for the recipe we chose.
                Intent i = new Intent(getContext(), RecipeActivity.class);
                i.putExtra("recipe-id", r.getId());
                startActivity(i);
            }
        });
        //((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onSuccess(String result) {

        if(result.equals("")) {
            list=(ListView) getActivity().findViewById(R.id.listView);
            list.setAdapter(null);
        } else {
            searchResults.Retrieve(result, this);
        }

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

        Collections.sort(recipes, new Comparator<Recipe>() {
            @Override
            public int compare(Recipe o1, Recipe o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });

        title.setText(recipes.get(position).getTitle());
        prepTime.setText(recipes.get(position).getPrepTime());
        servings.setText(recipes.get(position).getYield());
        Glide.with(getContext())
                .load(recipes.get(position).getImageURL())
                .crossFade()
                .centerCrop()
                .into(image);
        return row;
    }

}
