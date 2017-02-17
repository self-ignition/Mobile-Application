package com.self_ignition.cabbage2;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

public class SearchResultsActivity extends AppCompatActivity implements VolleyCallback{

    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(new adapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public void onSuccess(String result) {

    }
}

class SingleRow {
    String title;
    String prepTime;

    SingleRow(String title, String prepTime) {
        this.title = title;
        this.prepTime = prepTime;
    }
}

class adapter extends BaseAdapter {
    ArrayList<SingleRow> list;
    Context context;

    adapter(Context c) {
        context = c;
        list = new ArrayList<SingleRow>();
        Resources res = c.getResources();
        String[] titles = res.getStringArray(R.array.Titles);
        String[] prepTime = res.getStringArray(R.array.Preptime);
        for(int i =0; i < 10; i++)
        {
            list.add(new SingleRow(titles[i], prepTime[i]));
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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

        SingleRow temp = list.get(position);

        title.setText(temp.title);
        prepTime.setText(temp.prepTime);

        return row;
    }
}