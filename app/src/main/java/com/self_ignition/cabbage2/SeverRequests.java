package com.self_ignition.cabbage2;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by r-bur on 10/02/2017.
 */

public class SeverRequests {

    public void Login(final Context context, String _email, String _password) {
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
                        Log.d("volley", "Response is: " + response);
                        //CALL THE LOGIN METHOD
                        if(response.equals("1"))
                        {
                            SaveSharedPreference.setLoggedIn(context, true);
                            Intent intent = new Intent(context, HomeActivity.class);
                            context.startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
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
                        Log.d("volley", "Response is: " + response);
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

    public void SignUp(Context context, String _username, String _email, String _password)
    {
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
                        Log.d("volley", "Response is: " + response);
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
}
