package comself_ignition.httpsgithub.meetneat.other;

import comself_ignition.httpsgithub.meetneat.R;

/**
 * Created by ctrue on 06/03/2017.
 */

public enum ModelObject {

    RECIPE(R.string.recipe, R.layout.activity_recipe),
    METHOD(R.string.method, R.layout.activity_recipe_method),
    REVIEW(R.string.review, R.layout.activity_recipe_review);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}