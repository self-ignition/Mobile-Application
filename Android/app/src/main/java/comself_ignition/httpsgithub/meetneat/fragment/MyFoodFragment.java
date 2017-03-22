package comself_ignition.httpsgithub.meetneat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.activity.SearchResultsActivity;
import comself_ignition.httpsgithub.meetneat.other.MyFoodDialogFragment;

public class MyFoodFragment extends Fragment{

    public Button btnSearch;
    public Button btnRemove;

    public MyFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_food, container, false);
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

    public void doMySearch(String query) {
        Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
        intent.putExtra("query", query);
        getActivity().startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_add:
                FragmentManager fm = getFragmentManager();
                final MyFoodDialogFragment dialogFragment = new MyFoodDialogFragment();
                dialogFragment.show(fm, "Sample Fragment");

                fm.executePendingTransactions();
                dialogFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        onResume();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onResume() {
        super.onResume();

        String foodItem = "";
        // split food from stored list
        String[] storedFood;
        // Array for each food item, has to be done this way for check boxes to work
        List<food> foodList = new ArrayList<food>();

        try {
            InputStream inputStream = getActivity().openFileInput("food.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                foodItem = stringBuilder.toString();
                storedFood = foodItem.split("\\|");

                for (int i = 0; i < storedFood.length; i++) {
                    food foods = new food(storedFood[i]);
                    if(foods.getName().length() > 0)
                        foodList.add(foods);
                }
                RecyclerView rv = (RecyclerView) getActivity().findViewById(R.id.foodList);
                LinearLayoutManager llm = new LinearLayoutManager(getContext());
                rv.setLayoutManager(llm);
                rv.setAdapter(new adapterFood(foodList));

                final adapterFood mAdapter = new adapterFood(foodList);

                btnSearch = (Button) getActivity().findViewById(R.id.searchBtn);
                btnSearch.setEnabled(foodList.size() > 0);
                btnSearch.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String data = "";
                        List<food> foodlist = mAdapter.getFoodList();
                        for (int i = 0; i < foodlist.size(); i++) {
                            food singleFood = foodlist.get(i);
                            if (singleFood.isSelected()) {
                                if (data == "") {
                                    data = singleFood.getName();
                                } else {
                                    data = data + ", " + singleFood.getName();
                                }
                            }
                        }
                        //Create new view of search results
                        Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                        //add data(the ingredients) to the new view
                        intent.putExtra("query", data);
                        //start the activity
                        getActivity().startActivity(intent);
                    }
                });

                btnRemove = (Button) getActivity().findViewById(R.id.removeBtn);
                btnRemove.setEnabled(foodList.size() > 0);
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String data = "";
                        List<food> foodlist = mAdapter.getFoodList();

                        for (int i = 0; i < foodlist.size(); i++) {
                            food singleFood = foodlist.get(i);
                            if(!singleFood.isSelected()) {
                                if(data == "") {
                                    data = singleFood.getName();
                                } else {
                                    data = data + " | " + singleFood.getName();
                                }
                            }
                        }
                        try {
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getActivity().openFileOutput("food.txt", Context.MODE_PRIVATE));
                            outputStreamWriter.write(data);
                            outputStreamWriter.close();
                            onResume();
                        }
                        catch (IOException e) {
                            Log.e("Exception", "File write failed: " + e.toString());
                        }

                    }
                });

            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
}

class adapterFood extends RecyclerView.Adapter<adapterFood.ViewHolder> {
    private List<food> names;


    adapterFood(List<food> names) {
        this.names = names;
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_food_item_row, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(names.get(position).getName());

        holder.check.setChecked(names.get(position).isSelected());

        holder.check.setTag(names.get(position));

        holder.check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                food name = (food) cb.getTag();

                name.setSelected(cb.isChecked());
                names.get(position).setSelected(cb.isChecked());
            }
        } );
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        CheckBox check;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.FoodName);
            check = (CheckBox)itemView.findViewById(R.id.checkbox);
        }
    }

    public List<food> getFoodList() {
        return names;
    }
}

class food {
    private String name;
    private boolean isSelected;

    public food() {

    }

    public food(String name) {
        this.name = name;
    }

    public food(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}

