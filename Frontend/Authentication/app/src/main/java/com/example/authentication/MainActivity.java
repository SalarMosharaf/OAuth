package com.example.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    TextView textView;
    SharedPreferences shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView =(TextView) findViewById(R.id.ProfileUpdate);
        shared = getSharedPreferences("User", MODE_PRIVATE);


    }

    public void view_click(View view) {

        if(view.getId() == R.id.ProfileUpdate) {
            Intent intent = new Intent(MainActivity.this , profile.class);
            startActivity(intent);
        }

    }

    private void getUser () {

        StringRequest request = new StringRequest(Request.Method.GET, "http://10.0.2.2:8000/api/MenuItems?"+"email="+shared.getString("email",null), new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    if(object.getString("status").equals("success")){
                        //set on Menu Textview
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
        });


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request) ;

    }

    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

        return connected;
    }


}