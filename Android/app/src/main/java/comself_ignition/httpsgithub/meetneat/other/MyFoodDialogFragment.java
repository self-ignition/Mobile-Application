package comself_ignition.httpsgithub.meetneat.other;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;

import comself_ignition.httpsgithub.meetneat.R;

public class MyFoodDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
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
                    outputStreamWriter.flush();
                    outputStreamWriter.close();
                    Toast.makeText(getActivity(),str + " added", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
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

}
