package com.example.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class profile extends AppCompatActivity {

    ImageView img_edit , img_done;
    EditText edt_name , edt_family , edt_email;
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        shared = getSharedPreferences("User", MODE_PRIVATE);

        img_edit =(ImageView) findViewById(R.id.edit_prof);
        img_done =(ImageView) findViewById(R.id.done_prof);
        edt_name =(EditText) findViewById(R.id.edt_name);
        edt_family =(EditText) findViewById(R.id.edt_family);
        edt_email =(EditText) findViewById(R.id.edt_Email);

        edt_name.setFocusable(false);
        edt_family.setFocusable(false);
        edt_email.setFocusable(false);


        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_name.setFocusableInTouchMode(true);
                edt_family.setFocusableInTouchMode(true);
                edt_email.setFocusableInTouchMode(true);
            }
        });


        img_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkInternetConnection()) {
                    if (validate()) {
                        String name = edt_name.getText().toString();
                        String family = edt_family.getText().toString();
                        String emial = edt_email.getText().toString();

                        updateUser(name, family, emial);
                    }
                }else {

                }
            }
        });


        edt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_name.isFocusable()) {
                    Toast.makeText(profile.this, "Please First click on Edit Button", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edt_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_family.isFocusable()) {
                    Toast.makeText(profile.this, "Please First click on Edit Button", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edt_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edt_email.isFocusable()) {
                    Toast.makeText(profile.this, "Please First click on Edit Button", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if(checkInternetConnection()) {
            getUser();
        }else {
            Toast.makeText(profile.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateUser(String name , String family , String email) {

        JSONObject obj = new JSONObject();
        try {
            obj.put ("name", name);
            obj.put ("family", family);
            obj.put ("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, "http://10.0.2.2:8000/api/update/"+shared.getString("email","").trim(), obj, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")){
                        Toast.makeText(profile.this, "Information Updated", Toast.LENGTH_SHORT).show();
                        sharedPrefUpdate(name , family , email);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + shared.getString("token","null"));
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request) ;

    }

    private void getUser () {


        StringRequest request = new StringRequest(Request.Method.GET, "http://10.0.2.2:8000/api/getUser?"+"email="+shared.getString("email",null), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONObject data = object.getJSONObject("data");
                    JSONObject user = data.getJSONObject("user");

                    if(object.getString("status").equals("success")){
                        edt_name.setText(user.getString("name"));
                        edt_family.setText(user.getString("family"));
                        edt_email.setText(user.getString("email"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("larabel", "onErrorResponse: "+shared.getString("email",null));
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + shared.getString("token","null"));
                return headers;
            }
        };


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request) ;

    }

    private void sharedPrefUpdate(String name, String family, String email) {

        SharedPreferences.Editor Edit = shared.edit();

        Edit.putString("name",name);
        Edit.putString("family",family);
        Edit.putString("email",email);
        Edit.apply();


    }

    private boolean validate() {
        if(edt_name.getText().toString().isEmpty()) {
            edt_name.setError("Name is Required");
            return false;
        }
        if(!edt_email.getText().toString().contains("@") || !edt_email.getText().toString().contains(".com")) {
            edt_email.setError("Email is not valid");
            return false;
        }
        if(edt_family.getText().toString().isEmpty()) {
            edt_family.setError("Family is Required");
            return false;
        }

        return true;
    }

    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        return connected;
    }

}