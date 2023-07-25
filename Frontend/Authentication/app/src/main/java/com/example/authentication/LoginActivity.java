package com.example.authentication;

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

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    Button btn_login;
    EditText edt_user , edt_pass;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);

        if(sharedPreferences.contains("token")) {
            startActivity(new Intent(LoginActivity.this , MainActivity.class));
            finish();
        }

        btn_login =(Button) findViewById(R.id.button);
        edt_user =(EditText) findViewById(R.id.edt_UserName);
        edt_pass =(EditText) findViewById(R.id.edt_Password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInternetConnection()) {
                    if (validate()) {

                        String UserName = edt_user.getText().toString();
                        String Pass = edt_pass.getText().toString();
                        loginUser(UserName, Pass);

                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean validate() {
        if(edt_user.getText().toString().isEmpty()) {
            edt_user.setError("Email is Required");
            return false;
        }
        if(!edt_user.getText().toString().contains("@") || !edt_user.getText().toString().contains(".com")) {
            edt_user.setError("Email is not valid");
            return false;
        }
        if(edt_pass.getText().toString().isEmpty()) {
            edt_pass.setError("Password is Required");
            return false;
        }

        if(edt_pass.getText().toString().length() < 8) {
            edt_pass.setError("Password must contain 8 characters");
            return false;
        }

        return true;
    }


    private void loginUser(String UserName , String Pass) {

        JSONObject obj = new JSONObject();
        try {
            obj.put ("email", UserName) ;
            obj.put ("password", Pass) ;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://10.0.2.2:8000/api/login", obj, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")){

                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                        JSONObject data = response.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");

                        SharedPreferences.Editor Edit = sharedPreferences.edit();

                        Edit.putString("email",user.getString("email"));
                        Edit.putString("token",data.getString("token"));
                        Edit.apply();

                        Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                        startActivity(intent);
                        finish();


                    }else {
                        Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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