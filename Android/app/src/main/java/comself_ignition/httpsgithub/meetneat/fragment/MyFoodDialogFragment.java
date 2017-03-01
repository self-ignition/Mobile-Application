package comself_ignition.httpsgithub.meetneat.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.FriendAction;
import comself_ignition.httpsgithub.meetneat.other.Recipe;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;

import static android.R.attr.data;
import static android.support.design.R.id.text;
import static comself_ignition.httpsgithub.meetneat.R.id.name;

public class MyFoodDialogFragment extends DialogFragment implements VolleyCallback {

    VolleyCallback callback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_add_food, container, false);
        getDialog().setTitle("Simple Dialog");
        final Button add = (Button) rootView.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText name = (EditText) rootView.findViewById(R.id.input_addFood);
                String str = name.getText().toString();
                try {

                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getActivity().openFileOutput("food.txt", Context.MODE_APPEND));
                    outputStreamWriter.append(str + "|");
                    outputStreamWriter.close();
                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }

                Toast.makeText(getActivity(),"Food added", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        Button dismiss = (Button) rootView.findViewById(R.id.cancel);
        dismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }

    @Override
    public void onSuccess(String result) {
        Toast.makeText(getActivity(),"Request sent", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}

