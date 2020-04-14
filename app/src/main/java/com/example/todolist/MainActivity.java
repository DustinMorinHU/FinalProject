package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity {
    private Button Login;
    private Button SignUP;
    private EditText Username;
    private EditText Password;
    JSONArray Data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Login = findViewById(R.id.LoginButton);
        SignUP = findViewById(R.id.SignUPButton);
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String url ="http://www.morin.tk:5000/";
        final String testurl="http://www.morin.tk:5000/testquery";

        JsonArrayRequest TEST = new JsonArrayRequest

                (Request.Method.GET, testurl, null, new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        Data = response;
                        Log.d("data",Data.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        //Log.e("FlaskConnection", error.getMessage());
                    }
                });
        queue.add(TEST);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"Login Clicked", Toast.LENGTH_SHORT).show();
                Username = findViewById(R.id.Username);
                Password = findViewById(R.id.Password);
                String  Uname = Username.getText().toString();
                String  Pword = Password.getText().toString();
                String loginurl = url + ("login?username=" +Uname + "&" + "password=" + Pword);
                Log.d("url",loginurl);
                JsonArrayRequest Login = new JsonArrayRequest

                        (Request.Method.GET, loginurl, null, new Response.Listener<JSONArray>(){
                            @Override
                            public void onResponse(JSONArray response) {
                                Intent i = new Intent(MainActivity.this, List.class);
                                    try {
                                        String ID =response.get(0).toString();
                                        i.putExtra("ID",ID);
                                        startActivity(i);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                                    }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Username or Password Incorrect", Toast.LENGTH_SHORT).show();
                                //Log.d("FlaskConnection", error.getMessage());
                            }
                        });
                queue.add(Login);
            }
        });
        SignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Username = findViewById(R.id.Username);
                Password = findViewById(R.id.Password);
                String  Uname = Username.getText().toString();
                String  Pword = Password.getText().toString();
                String signupurl = url + ("signup?username=" +Uname + "&" + "password=" + Pword);
                Log.d("url",signupurl);
                StringRequest SignupRequest = new StringRequest(Request.Method.GET, signupurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Intent i = new Intent(MainActivity.this, List.class);
                                Toast.makeText(MainActivity.this, "SignUP Successful", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("FlaskConnection", error.getMessage());
                    }
                });
                queue.add(SignupRequest);

            }
        });
    }
}
