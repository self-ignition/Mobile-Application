package comself_ignition.httpsgithub.meetneat.fragment;

import android.animation.ValueAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.Recipe;
import comself_ignition.httpsgithub.meetneat.other.RecipeReadyCallback;
import comself_ignition.httpsgithub.meetneat.other.ReviewsDialogFragment;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.SavedRecipeAction;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;

public class RecipeFragment extends Fragment {

    public RecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe, container, false);
    }


}

