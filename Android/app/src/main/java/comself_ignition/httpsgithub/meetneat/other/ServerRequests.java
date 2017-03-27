package comself_ignition.httpsgithub.meetneat.other;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.util.ContentLengthInputStream;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import comself_ignition.httpsgithub.meetneat.activity.MainActivity;

import static comself_ignition.httpsgithub.meetneat.R.string.recipe;
import static org.apache.commons.lang3.StringUtils.split;

public class ServerRequests {

    //Test update for software engineering
    public void Login(final Context context, String _email, String _password, final VolleyCallback callback) {
        final String email = _email;
        final String password = _password;
        Map<String, String> mParams;
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://computing.derby.ac.uk/~cabbage/login.php";

        //Create the request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                //What happens when the request completes
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //CALL THE LOGIN METHOD
                        try {

                            callback.onSuccess(response);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    //What happens if the request fails
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "That didn't work!");
                //CALL DISPLAY ERROR METHOD
            }
        }
                //Some touchy-feely with body to add post payload
        ) {
            @Override
            protected Map<String, String> getParams() {
                //create the map for keypairs
                Map<String, String> params = new HashMap<String, String>();

                params.put("password", password);
                params.put("email", email);
                return params;
            }
        };
        //add the request to the queue
        queue.add(request);

    }

    public void DoSearch(final Context context, final String query, final SearchType type, final VolleyCallback callback) {
        Map<String, String> mParams;
        RequestQueue queue = Volley.newRequestQueue(context);
        String searchMethod = "";
        switch (type) {
            case OR:
                searchMethod = "OR";
                break;
            case AND:
                searchMethod = "AND";
                break;
            case THIS:
                searchMethod = "THIS";
                break;
            default:
                searchMethod = "OR";
        }

        final String url = "http://computing.derby.ac.uk/~cabbage/dosearch.php?terms=" + query.replace(" ", "%20") + "&search=" + searchMethod;

        //Create the request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                //What happens when the request completes
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("\r\n0 results")) {
                            //error
                            Toast.makeText(context, "No recipes found", Toast.LENGTH_LONG).show();
                            //other page
                            Intent previous = new Intent(context, MainActivity.class);
                            context.startActivity(previous);
                        }
                        else{
                            try {
                                callback.onSuccess(response);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    //What happens if the request fails
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccess("Failed");
            }
        });
        queue.add(request);
    }

    public void SendLocation(Context context, String _email, Location _location) {
        final Location location = _location;
        final String email = _email;
        Map<String, String> mParams;
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://computing.derby.ac.uk/~cabbage/updatelocation.php";

        //Create the request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                //What happens when the request completes
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //CALL THE LOGIN METHOD

                    }
                    //What happens if the request fails
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "That didn't work!");
                //CALL DISPLAY ERROR METHOD
            }
        }
                //Some touchy-feely with body to add post payload
        ) {
            @Override
            protected Map<String, String> getParams() {
                //create the map for keypairs
                Map<String, String> params = new HashMap<String, String>();

                params.put("location", location.toString());
                params.put("email", email);
                return params;
            }
        };
        //add the request to the queue
        queue.add(request);
    }

    public void SignUp(Context context, String _username, String _email, String _password) {
        Map<String, String> mParams;
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://computing.derby.ac.uk/~cabbage/signup.php";

        final String username = _username;
        final String email = _email;
        final String password = _password;

        //Create the request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                //What happens when the request completes
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                    //What happens if the request fails
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "That didn't work!");
            }
        }
                //Some touchy-feely with body to add post payload
        ) {
            @Override
            protected Map<String, String> getParams() {
                //create the map for keypairs
                Map<String, String> params = new HashMap<String, String>();

                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                return params;
            }
        };

        queue.add(request);
    }

    public void GetRecipe(Context context, final String url, final VolleyCallback callback) {
        Map<String, String> mParams;
        RequestQueue queue = Volley.newRequestQueue(context);

        //Create the request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                //What happens when the request completes
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String s = response.replace("&#39;", "'");
                        try {
                            callback.onSuccess(s);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    //What happens if the request fails
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccess("Failed");
            }
        });

        queue.add(request);
    }

    public void getUsername(final Context context, final String _email, final VolleyCallback callback) {
        Map<String, String> mParams;
        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "http://computing.derby.ac.uk/~cabbage/getusername.php";

        //Create the request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                //What happens when the request completes
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //CALL THE LOGIN METHOD
                        try {
                            callback.onSuccess(response);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                    //What happens if the request fails
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "That didn't work!");
                //CALL DISPLAY ERROR METHOD
            }
        }
                //Some touchy-feely with body to add post payload
        ) {
            @Override
            protected Map<String, String> getParams() {
                //create the map for keypairs
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", _email);
                return params;
            }
        };
        //add the request to the queue
        queue.add(request);

    }

    public void Friends(final Context context, final VolleyCallback callback, final FriendAction action, final String You, final String Them) {
        Map<String, String> mParams;
        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "http://computing.derby.ac.uk/~cabbage/friends.php";

        //Create the request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                //What happens when the request completes
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //CALL THE LOGIN METHOD
                        if (callback != null)
                            try {
                                callback.onSuccess(response);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                    }
                    //What happens if the request fails
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "That didn't work!");
                //CALL DISPLAY ERROR METHOD
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //create the map for keypairs
                Map<String, String> params = new HashMap<String, String>();
                switch (action) {
                    case add:
                        params.put("request", "add");
                        break;
                    case remove:
                        params.put("request", "remove");
                        break;
                    case confirm:
                        params.put("request", "confirm");
                        break;
                    case getSender:
                        params.put("request", "getSender");
                        break;
                    case getRecipient:
                        params.put("request", "getRecipient");
                        break;
                }

                params.put("sender", You);
                params.put("recipient", Them);
                return params;
            }
        };
        //add the request to the queue
        queue.add(request);

    }

    public void GetSavedRecipes(final Context context, final VolleyCallback callback, final SavedRecipeAction action, final String You, final String recipeID) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "http://computing.derby.ac.uk/~cabbage/savedrecipes.php";

        //Create the request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                //What happens when the request completes
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //CALL THE LOGIN METHOD
                        if (callback != null){
                            try {
                                callback.onSuccess(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    //What happens if the request fails
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "That didn't work!");
                //CALL DISPLAY ERROR METHOD
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //create the map for keypairs
                Map<String, String> params = new HashMap<String, String>();
                switch (action) {
                    case get:
                        params.put("request", "get");
                        break;
                    case set:
                        params.put("request", "set");
                        break;
                    case remove:
                        params.put("request", "remove");
                        break;
                }

                params.put("recipe_id", recipeID);
                params.put("username", You);
                return params;
            }
        };
        //add the request to the queue
        queue.add(request);
    }

    public void GetSavedRecipes(final Context context, final VolleyCallback callback, final SavedRecipeAction action, final String You) {
        GetSavedRecipes(context, callback, action, You, "");
    }

    public void MessageRequest(final Context context, final VolleyCallback callback, final MessageAction action, final String sender, final String recipient, final String message) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "http://computing.derby.ac.uk/~cabbage/message.php";

        //Create the request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                //What happens when the request completes
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //CALL THE LOGIN METHOD
                        if (callback != null) {
                            try {
                                callback.onSuccess(response);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    //What happens if the request fails
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "That didn't work!");
                //CALL DISPLAY ERROR METHOD
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //create the map for keypairs
                Map<String, String> params = new HashMap<String, String>();
                switch (action)
                {
                    case GET:
                        params.put("intent", "GET");
                        break;
                    case SEND:
                        params.put("intent", "SEND");
                        break;
                    case POPULATE:
                        params.put("intent", "POPULATE");
                }

                params.put("sender", sender);
                params.put("recipient", recipient);
                params.put("content", message);
                return params;
            }
        };
        //add the request to the queue
        queue.add(request);
    }

    public void AddReview(final Context context, final String You, final String recipeID, final String content) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "http://computing.derby.ac.uk/~cabbage/review.php";

        //Create the request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                //What happens when the request completes
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                    //What happens if the request fails
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "That didn't work!");
                //CALL DISPLAY ERROR METHOD
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //create the map for keypairs
                Map<String, String> params = new HashMap<String, String>();

                params.put("recipe_id", recipeID);
                params.put("username", You);
                params.put("content", content);
                return params;
            }
        };
        //add the request to the queue
        queue.add(request);
    }

    public void forgotPassword(final Context context, final String email) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "http://computing.derby.ac.uk/~cabbage/forgottenpassword.php";

        //Create the request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                //What happens when the request completes
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                    }
                    //What happens if the request fails
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "That didn't work!");
                //CALL DISPLAY ERROR METHOD
            }
        }) {
            protected Map<String, String> getParams() {
                //create the map for keypairs
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);
                return params;
            }
        };
        queue.add(request);
    }

    public void resetPassword(final Context context, final String email, final String currentPassword, final String newPassword) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final String url = "http://computing.derby.ac.uk/~cabbage/resetpassword.php";

        //Create the request
        StringRequest request = new StringRequest(Request.Method.POST, url,
                //What happens when the request completes
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                    }
                    //What happens if the request fails
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley", "That didn't work!");
                //CALL DISPLAY ERROR METHOD
            }
        }) {
            protected Map<String, String> getParams() {
                //create the map for keypairs
                Map<String, String> params = new HashMap<String, String>();

                params.put("email", email);
                params.put("currentPassword", currentPassword);
                params.put("newPassword", newPassword);
                return params;
            }
        };
        queue.add(request);
    }
}


