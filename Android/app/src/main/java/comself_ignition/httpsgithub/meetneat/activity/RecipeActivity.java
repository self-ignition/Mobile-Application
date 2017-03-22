package comself_ignition.httpsgithub.meetneat.activity;

import android.animation.ValueAnimator;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import comself_ignition.httpsgithub.meetneat.R;
import comself_ignition.httpsgithub.meetneat.other.ModelObject;
import comself_ignition.httpsgithub.meetneat.other.Recipe;
import comself_ignition.httpsgithub.meetneat.other.RecipeReadyCallback;
import comself_ignition.httpsgithub.meetneat.other.ReviewsDialogFragment;
import comself_ignition.httpsgithub.meetneat.other.SaveSharedPreference;
import comself_ignition.httpsgithub.meetneat.other.SavedRecipeAction;
import comself_ignition.httpsgithub.meetneat.other.ServerRequests;
import comself_ignition.httpsgithub.meetneat.other.VolleyCallback;

public class RecipeActivity extends AppCompatActivity implements VolleyCallback, RecipeReadyCallback {
    Recipe recipe = new Recipe();

    boolean isAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new CustomPagerAdapter(this));

        //Set the recipe
        recipe.setRecipe(this, getIntent().getStringExtra("recipe-id"), this);
    }

    public void reviewDialog(View v) {
        FragmentManager frag = getFragmentManager();
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

        TextView review = (TextView) findViewById(R.id.reviews_text);
        String reviews_text = recipe.getReviews().toString();
        reviews_text = reviews_text.replace("[","");
        reviews_text = reviews_text.replace("]","");
        review.setText(reviews_text);

        Glide.with(this)
                .load(recipe.getImageURL())
                .crossFade()
                .centerCrop()
                .into(image);
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
