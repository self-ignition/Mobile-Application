package comself_ignition.httpsgithub.meetneat.activity;

import android.animation.ValueAnimator;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.fragment.RecipeFragment;
import comself_ignition.httpsgithub.meetneat.fragment.RecipeMethodFragment;
import comself_ignition.httpsgithub.meetneat.fragment.RecipeReviewFragment;
import comself_ignition.httpsgithub.meetneat.other.Recipe;
import comself_ignition.httpsgithub.meetneat.other.RecipeReadyCallback;
import comself_ignition.httpsgithub.meetneat.other.ReviewsDialogFragment;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.SavedRecipeAction;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;

import static comself_ignition.httpsgithub.meetneat.R.id.pager;

public class RecipeActivity extends AppCompatActivity implements VolleyCallback, RecipeReadyCallback {
    Recipe recipe = new Recipe();

    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter adapter;

    boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Method"));
        tabLayout.addTab(tabLayout.newTab().setText("Reviews"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager)findViewById(pager);
        viewPager.setOffscreenPageLimit(3);

        adapter = new PagerAdapter(getSupportFragmentManager());

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        viewPager.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        recipe.setRecipe(this, getIntent().getStringExtra("recipe-id"), this);
    }

    public void reviewDialog(View v) {
        final ReviewsDialogFragment dialog = new ReviewsDialogFragment(recipe.getId());
        dialog.show(getSupportFragmentManager(), "Review Fragment");
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
        time.setText("Prep time: " + recipe.getPrepTime() + "\nCook Time: " + recipe.getCookTime());

        TextView yield = (TextView) findViewById(R.id.yield);
        yield.setText(recipe.getYield());

        TextView author = (TextView) findViewById(R.id.author);
        author.setText("Created by: " + recipe.getAuthor());

        TextView ingredients = (TextView) findViewById(R.id.ingredients_text);
        String ingredients__string = "";
        for (String s: recipe.getIngredients()) {
            ingredients__string += s;
        }
        ingredients.setText(ingredients__string);

        TextView method = (TextView) findViewById(R.id.method_text);
        String method_string = "";
        for (String s: recipe.getSteps()) {
            method_string += s;
        }
        method.setText(method_string);

        //Update the image
        ImageView image = (ImageView)findViewById(R.id.image);

        TextView review = (TextView) findViewById(R.id.reviews_text);
        String review_string = "";
        for (String s: recipe.getReviews()) {
            review_string += s;
        }

        if(recipe.getReviews().isEmpty()) {
            review_string = "Be the first to leave a review!";
        }
        review.setText(review_string);

        Glide.with(this)
                .load(recipe.getImageURL())
                .crossFade()
                .centerCrop()
                .into(image);
    }
}

class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                RecipeFragment tab1 = new RecipeFragment();
                return tab1;
            case 1:
                RecipeMethodFragment tab2 = new RecipeMethodFragment();
                return tab2;
            case 2:
                RecipeReviewFragment tab3 = new RecipeReviewFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
