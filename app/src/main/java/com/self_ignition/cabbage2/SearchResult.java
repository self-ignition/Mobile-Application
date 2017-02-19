package com.self_ignition.cabbage2;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by r-bur on 18/02/2017.
 */

public class SearchResult implements VolleyCallback, RecipeReadyCallback{
    Context context;
    List<Recipe> results = new ArrayList<>();
    SearchResultCallback callback;

    public SearchResult(Context context){
        this.context = context;
    }

    public void Search(String terms, SearchResultCallback callback){
        this.callback = callback;
        SeverRequests ser = new SeverRequests();
        ser.DoSearch(context, terms, this);
    }

    @Override
    public void onSuccess(String result) {
        List<String> results = new ArrayList<String>();
        List<String> parts = new ArrayList<String>();

        //Spilt response into parts
        results = Arrays.asList(result.split("¦"));


        for (String s: results) {
            //Spilt recipe into parts
            parts = Arrays.asList(s.split("\\|"));

            Recipe r = new Recipe();
            r.setTitle(parts.get(0));
            r.setPrepTime(parts.get(1));
            r.setCookTime(parts.get(2));
            r.setImageURL(parts.get(3).replace(" ", "%20"));
            r.DownloadImage(this);

            //TODO: SET RATING
        }
    }

    @Override
    public void onReady(Recipe r) {
        callback.onSearchComplete(r);
    }
}
