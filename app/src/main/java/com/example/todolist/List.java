package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.w3c.dom.Text;

public class List extends AppCompatActivity {
    private ListView ListView;
    private Button Add;
    private Button Remove;
    private EditText TaskName;
    private TextView View;
    JSONArray Data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        View = findViewById(R.id.TextView);
        Add = findViewById(R.id.AddButton);
        Remove = findViewById(R.id.RemoveButton);
        TaskName = findViewById(R.id.TaskName);
        final RequestQueue queue = Volley.newRequestQueue(List.this);
        final String url = "http://www.morin.tk:5000/";
        String extras = getIntent().getExtras().getString("ID");
        extras = extras.replaceAll("\\[", "").replaceAll("\\]", "");
        String[] infoSplit = extras.split(",");
        final String ID = infoSplit[0];
        //[27,"",""]
        JsonArrayRequest getTasks = new JsonArrayRequest

                (Request.Method.GET, url+"gettasks?User_ID="+ID, null, new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        Data = response;
                        String Tasks = Data.toString();
                        Tasks = Tasks.replaceAll("\\[", "\n").replaceAll("\\]", "").replaceAll("\"" , "").replaceAll(",", "\n").replaceAll("_", " ");
                        View.setText(Tasks);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(List.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        //Log.e("FlaskConnection", error.getMessage());
                    }
                });
        queue.add(getTasks);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = TaskName.getText().toString();
                String submitname = name.replaceAll(" ", "_");
                String newtaskurl = url + ("newtask?taskname=" + submitname + "&" + "User_ID=" + ID);
                StringRequest TaskRequest = new StringRequest(Request.Method.GET, newtaskurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(List.this, "Task Added", Toast.LENGTH_SHORT).show();
                                View.append("\n" + TaskName.getText().toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(List.this, "something went wrong", Toast.LENGTH_SHORT).show();
                        Log.d("FlaskConnection", error.getMessage());
                    }
                });
                queue.add(TaskRequest);

            }


        });
        Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = TaskName.getText().toString();
                String submitname = name.replaceAll(" ", "_");
                String removetaskurl = url + ("deletetask?taskname=" + submitname + "&" + "User_ID=" + ID);
                Log.d("url", removetaskurl);
                StringRequest RemoveRequest = new StringRequest(Request.Method.GET, removetaskurl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(List.this, "Task Removed", Toast.LENGTH_SHORT).show();
                                String Temp = View.getText().toString();
                                String tasktoremove = "\n"+TaskName.getText().toString();
                                Temp = Temp.replaceAll(tasktoremove, "");
                                View.setText(Temp);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(List.this, "Task doesn't exist", Toast.LENGTH_SHORT).show();
                        //Log.d("FlaskConnection", error.getMessage());
                    }
                });
                queue.add(RemoveRequest);

            }


        });
    }
}
