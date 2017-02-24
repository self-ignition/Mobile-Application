package comself_ignition.httpsgithub.meetneat.other;

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
        ServerRequests ser = new ServerRequests();
        ser.DoSearch(context, terms, SearchType.OR, this);
    }

    public void Retrieve(String terms, SearchResultCallback callback){
        this.callback = callback;
        for (String id: terms.split("\\|")) {
            ServerRequests ser = new ServerRequests();
            ser.DoSearch(context, id, SearchType.THIS, this);
        }
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
            try {
                r.setId(parts.get(0));
                r.setTitle(parts.get(1));
                r.setPrepTime(parts.get(2));
                r.setCookTime(parts.get(3));
                r.setAuthor(parts.get(4));
                r.setYield(parts.get(5));
                r.setImageURL(parts.get(6).replace(" ", "%20"));
                r.DownloadImage(this);
            }
            catch (IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onReady(Recipe r) {
        callback.onSearchComplete(r);
    }
}

