package comself_ignition.httpsgithub.meetneat.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.Recipe;
import comself_ignition.httpsgithub.meetneat.other.RecipeReadyCallback;

import static android.R.attr.data;
import static android.os.Build.VERSION_CODES.M;

public class RecipeActivity extends AppCompatActivity implements RecipeReadyCallback {

    ExpandableListView expandableListView;

    Recipe recipe = new Recipe();

    List<String> titles;
    Map<String,List<String>> details;
    ExpandableListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        expandableListView = (ExpandableListView) findViewById(R.id.exp_list);
        init();

        listAdapter = new MyListAdapter(this,titles,details);
        expandableListView.setAdapter(listAdapter);
        checkIfSaved();
        recipe.setRecipe(this, getIntent().getStringExtra("recipe-id"), this);
    }

    public void checkIfSaved() {
        String data = recipe.getId();
        String recipe = "";
        try {
            InputStream inputStream = this.openFileInput("config.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                recipe = stringBuilder.toString();

            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        final ImageView mImageRed = (ImageView) findViewById(R.id.heart_red);
        Log.i("WHAT IS ID: ", data);
        Log.i("WHAT IS RECIPE: ", recipe);
        if(recipe.contains(data)) {
            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mImageRed.setAlpha((Float) animation.getAnimatedValue());
                }
            });

            animator.setDuration(1500);
            animator.start();
        }
    }

    public void saveFunction(View v) {
        String data = recipe.getId();
        String recipe = "";
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("config.txt", Context.MODE_APPEND));
            outputStreamWriter.write("");
            outputStreamWriter.close();

            InputStream inputStream = this.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }
                inputStream.close();
                recipe = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        if(!recipe.contains(data))
        {
            try {

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("config.txt", Context.MODE_APPEND));
                outputStreamWriter.append(data + "|");
                outputStreamWriter.close();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }

            final ImageView mImageRed = (ImageView) findViewById(R.id.heart_red);

            ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mImageRed.setAlpha((Float) animation.getAnimatedValue());
                }
            });

            animator.setDuration(1500);
            animator.start();
        }
        else
        {
            try {
                InputStream inputStream = this.openFileInput("config.txt");

                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }

                    inputStream.close();
                    recipe = stringBuilder.toString();

                    recipe = recipe.replace(data +"|","");
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.openFileOutput("config.txt", Context.MODE_PRIVATE));
                    outputStreamWriter.write(recipe);
                    outputStreamWriter.close();

                    final ImageView mImageRed = (ImageView) findViewById(R.id.heart_red);

                    ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            mImageRed.setAlpha((Float) animation.getAnimatedValue());
                        }
                    });

                    animator.setDuration(1500);
                    animator.start();
                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
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
*/
   /* public void doMySearch(String query) {
        Intent intent = new Intent(this, SearchResultsActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
    }*/

    public void init()
    {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Loading, Please wait...");

        TextView time = (TextView) findViewById(R.id.time);
        time.setText("...");

        TextView yield = (TextView) findViewById(R.id.yield);
        yield.setText("...");

        TextView author = (TextView) findViewById(R.id.author);
        author.setText("...");

        titles = new ArrayList<>();
        details = new HashMap<>();

        titles.add("Ingredients");
        titles.add("Method");
        titles.add("Reviews");

        List<String> ingredients = new ArrayList<>();
        List<String> method = new ArrayList<>();
        List<String> reviews = new ArrayList<>();


        reviews.add("I love when my nan makes me these. 10/10.\n");

        details.put(titles.get(0),ingredients);
        details.put(titles.get(1),method);
        details.put(titles.get(2),reviews);
    }

    @Override
    public void onReady(Recipe r) {
        recipe = r;
        Log.i("Recipe callback", "Title of recipe:" + recipe.getTitle());
        UpdateFields();
    }

    private void UpdateFields() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(recipe.getTitle());

        TextView time = (TextView) findViewById(R.id.time);
        time.setText("Prep time: " + recipe.getPrepTime() + "/ Cook Time: " + recipe.getCookTime());

        TextView yield = (TextView) findViewById(R.id.yield);
        yield.setText(recipe.getYield());

        TextView author = (TextView) findViewById(R.id.author);
        author.setText("Created by: " + recipe.getAuthor());

        titles = new ArrayList<>();
        details = new HashMap<>();

        titles.add("Ingredients");
        titles.add("Method");
        titles.add("Reviews");

        List<String> reviews = new ArrayList<>();

        reviews.add("I love when my nan makes me these. 10/10.\n");

        details.put(titles.get(0),recipe.getIngredients());
        details.put(titles.get(1),recipe.getSteps());
        details.put(titles.get(2),reviews);

        listAdapter = new MyListAdapter(this,titles,details);
        expandableListView.setAdapter(listAdapter);
        ((BaseAdapter) expandableListView.getAdapter()).notifyDataSetChanged();

        //Update the image
        ImageView image = (ImageView)findViewById(R.id.image);
        image.setBackground(new BitmapDrawable(recipe.getImage()));
    }
}

class MyListAdapter extends BaseExpandableListAdapter{
    Context context;
    List<String> titles;
    Map<String,List<String>> details;


    public MyListAdapter(Context context, List<String> titles, Map<String, List<String>> details) {
        this.details = details;
        this.context = context;
        this.titles = titles;
    }

    @Override
    public int getGroupCount() {
        return titles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return details.get(titles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return titles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return details.get(titles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) getGroup(groupPosition);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_recipe_parent,null);
        }

        TextView txtParent = (TextView) convertView.findViewById(R.id.txtParent);
        txtParent.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String details = (String) getChild(groupPosition, childPosition);

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_recipe_child,null);
        }

        TextView txtChild = (TextView) convertView.findViewById(R.id.txtChild);
        txtChild.setText(details);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}