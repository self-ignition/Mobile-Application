package comself_ignition.httpsgithub.meetneat.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import comself_ignition.httpsgithub.meetneat.R;

import static android.R.id.list;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class MyFoodFragment extends Fragment {
    ListView list;

    public MyFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = (ListView) getActivity().findViewById(R.id.foodList);

        FloatingActionButton myFab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                final MyFoodDialogFragment dialogFragment = new MyFoodDialogFragment();
                dialogFragment.show(fm, "Sample Fragment");

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_food, container, false);
    }

    private void updateFragment() {
        String food = "";

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
                food = stringBuilder.toString();
                String[] friends = food.split("\\|");
                list=(ListView) getActivity().findViewById(R.id.foodList);
                list.setAdapter(new adapterFood(getActivity(), friends));
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
}

class adapterFood extends ArrayAdapter<String> {
    Context context;
    String[] names;

    adapterFood(Context c, String[] names) {
        super(c, R.layout.fragment_my_food_item_row, names);
        this.names = names;
        this.context = c;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.fragment_my_food_item_row, parent, false);
        TextView name = (TextView) row.findViewById(R.id.FoodName);

        name.setText(names[position].toString());

        return row;
    }
}