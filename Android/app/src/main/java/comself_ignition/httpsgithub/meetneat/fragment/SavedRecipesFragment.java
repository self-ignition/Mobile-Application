package comself_ignition.httpsgithub.meetneat.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.activity.RecipeActivity;
import comself_ignition.httpsgithub.meetneat.other.Recipe;
import comself_ignition.httpsgithub.meetneat.other.SearchResult;
import comself_ignition.httpsgithub.meetneat.other.SearchResultCallback;

import static android.R.id.list;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SavedRecipesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SavedRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedRecipesFragment extends Fragment implements SearchResultCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    SearchResult searchResults;
    List<Recipe> recipes = new ArrayList<>();
    ListView list;

    public SavedRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedRecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedRecipesFragment newInstance(String param1, String param2) {
        SavedRecipesFragment fragment = new SavedRecipesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchResults = new SearchResult(getActivity().getBaseContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        readRecipes();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_search_results, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onSearchComplete(Recipe r) {
        recipes.add(r);

        UpdateFields();
    }

    private void UpdateFields() {
        list=(ListView) getActivity().findViewById(R.id.listView);
        list.setAdapter(new adapter(getActivity(), recipes)); /*MAYBE NO*/
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
        ((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void readRecipes()
    {
        String ret = "";
        try {
            InputStream inputStream = getActivity().openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
                Log.i("HELLO", ret);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        //Search ( Query, Callback )
        searchResults.Search(ret, this);
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
