package com.self_ignition.cabbage2;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.color.white;
import static android.R.id.extractArea;
import static android.R.id.list;

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

        recipe.setRecipe(this, getIntent().getStringExtra("recipe-title"), this);
        Log.i("Intent Extra", "onCreate: " + getIntent().getStringExtra("recipe-title"));
        //recipe.setRandomRecipe(this,this);
    }

    public void init()
    {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Loading, Please wait...");

        TextView time = (TextView) findViewById(R.id.time);
        time.setText("...");

        titles = new ArrayList<>();
        details = new HashMap<>();

        titles.add("Ingredients");
        titles.add("Method");
        titles.add("Reviews");

        List<String> ingredients = new ArrayList<>();
        List<String> method = new ArrayList<>();
        List<String> reviews = new ArrayList<>();

        ingredients.add("2 slices of white bread\n");
        ingredients.add("1 tin of heinz baked beans\n");
        ingredients.add("40g of grated mature cheddar\n");

        method.add("Place both slices of white bread in toaster.\n");
        method.add("Warm beans on the hob for 3 - 4 mins stirring constantly.\n");
        method.add("Grate cheese.\n");
        method.add("Place toast on plate. Pour on beans.\n");
        method.add("Sprinkle with cheese according to taste.\n");

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
            convertView = inflater.inflate(R.layout.list_parent,null);
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
            convertView = inflater.inflate(R.layout.list_child,null);
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

