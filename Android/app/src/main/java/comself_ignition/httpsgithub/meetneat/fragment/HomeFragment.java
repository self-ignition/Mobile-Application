package comself_ignition.httpsgithub.meetneat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import comself_ignition.httpsgithub.meetneat.other.Recipe;
import comself_ignition.httpsgithub.meetneat.other.RecipeReadyCallback;
import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.activity.RecipeActivity;
import comself_ignition.httpsgithub.meetneat.activity.SearchResultsActivity;


public class HomeFragment extends Fragment implements RecipeReadyCallback {
    List<Recipe> recipes = new ArrayList<Recipe>();
    int buttonToSet = 1;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UpdateRecipes();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
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
            case R.id.action_refresh:
                UpdateRecipes();
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

    public void UpdateRecipes() {
        recipes.clear();
        buttonToSet = 1;

        for (int i = 0; i < 6; i++)
        {
            Recipe r = new Recipe();
            r.setRandomRecipe(getActivity(), this);
            recipes.add(r);
        }
    }

        @Override
    public void onReady(Recipe r) {
        final Recipe recipe = r;
        ImageView button;
        TextView text;

        switch(buttonToSet){
            case 1:
                button = (ImageView) getView().findViewById(R.id.picture1);
                text = (TextView) getView().findViewById(R.id.text1);
                break;
            case 2:
                button = (ImageView) getView().findViewById(R.id.picture2);
                text = (TextView) getView().findViewById(R.id.text2);
                break;
            case 3:
                button = (ImageView) getView().findViewById(R.id.picture3);
                text = (TextView) getView().findViewById(R.id.text3);
                break;
            case 4:
                button = (ImageView) getView().findViewById(R.id.picture4);
                text = (TextView) getView().findViewById(R.id.text4);
                break;
            case 5:
                button = (ImageView) getView().findViewById(R.id.picture5);
                text = (TextView) getView().findViewById(R.id.text5);
                break;
            case 6:
                button = (ImageView) getView().findViewById(R.id.picture6);
                text = (TextView) getView().findViewById(R.id.text6);
                break;
            default:
                button = (ImageView) getView().findViewById(R.id.picture1);
                text = (TextView) getView().findViewById(R.id.text1);
                Log.e("Button set", "OOB Exception: Value for int was " + Integer.toString(buttonToSet));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getBaseContext(), RecipeActivity.class);
                i.putExtra("recipe-id", recipe.getId());
                Log.i("Intent Extra", "id of recipe:" + recipe.getId());
                startActivity(i);
            }
        });

            Glide.with(getContext())
                    .load(recipe.getImageURL())
                    .crossFade()
                    .centerCrop()
                    .into(button);
            text.setText(recipe.getTitle());
        buttonToSet++;
    }
}