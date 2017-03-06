package comself_ignition.httpsgithub.meetneat.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

import org.w3c.dom.Text;

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
import comself_ignition.httpsgithub.meetneat.other.ModelObject;
import comself_ignition.httpsgithub.meetneat.other.Recipe;
import comself_ignition.httpsgithub.meetneat.other.RecipeReadyCallback;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.SavedRecipeAction;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;

import static android.R.attr.data;
import static android.os.Build.VERSION_CODES.M;

public class RecipeActivity extends AppCompatActivity implements VolleyCallback, RecipeReadyCallback {
    Recipe recipe = new Recipe();

    List<String> titles;
    Map<String,List<String>> details;

    boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        //init();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this));

        //init();
        //Set the recipe
        recipe.setRecipe(this, getIntent().getStringExtra("recipe-id"), this);
    }

    @Override
    public void onSuccess(String result) {
        //CHECK TO SEE IF THE RECIPE IS IN THE LIST OF RESULTS.
        final ImageView mImageRed = (ImageView) findViewById(R.id.heart_red);

        if(result.contains(recipe.getId())) {
            isAdded = true;
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
        final ImageView mImageRed = (ImageView) findViewById(R.id.heart_red);
        ServerRequests sr = new ServerRequests();

        if(isAdded) {
            sr.GetSavedRecipes(this, null, SavedRecipeAction.remove, SaveSharedPreference.getUserName(this), recipe.getId());
            isAdded = false;
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
        else
        {
            sr.GetSavedRecipes(this, null, SavedRecipeAction.set, SaveSharedPreference.getUserName(this), recipe.getId());
            isAdded = true;
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

    @Override
    public void onReady(Recipe r) {
        recipe = r;

        //Used to set the colour of the heart.
        ServerRequests sr = new ServerRequests();
        sr.GetSavedRecipes(this, this, SavedRecipeAction.get, SaveSharedPreference.getUserName(this));

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

        TextView ingredients = (TextView) findViewById(R.id.ingredients_text);
        String text = recipe.getIngredients().toString();
        text = text.replace("[","");
        text = text.replace("]","");
        text = text.replace(",","\n\n");
        ingredients.setText(text);

        TextView method = (TextView) findViewById(R.id.method_text);
        String method_text = recipe.getSteps().toString();
        method_text = method_text.replace("[","");
        method_text = method_text.replace("]","");
        method_text = method_text.replace(".,",".\n\n");
        method_text = method_text.replace("),",")\n\n");
        method.setText(method_text);

        //Update the image
        ImageView image = (ImageView)findViewById(R.id.image);
        image.setImageDrawable(new BitmapDrawable(recipe.getImage()));
    }
}

class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        ModelObject modelObject = ModelObject.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return ModelObject.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        ModelObject customPagerEnum = ModelObject.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }
}