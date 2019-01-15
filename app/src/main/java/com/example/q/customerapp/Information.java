package com.example.q.customerapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Information extends BaseActivity {
    private static Context context;
    private Button button;
    private TextView Number, Name, mTextView;
    public static String numberofpeople = "a";
    public static String phone = "b";
    int aa = 1000000;
    int j = 0;
    int number = 0;
    String ID;
    int kiki = 0;

    public static Context getContext() {
        return context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.information);
        button = (Button) findViewById(R.id.reservation);
        Name = (TextView) findViewById(R.id.textViewName);
        final EditText edit = (EditText) findViewById(R.id.edittext);
        final EditText edit2 = (EditText) findViewById(R.id.edittext2);
        Intent intent = new Intent(this.getIntent());
        final String s = intent.getStringExtra("store");
        final String url = "http://socrip3.kaist.ac.kr:5880/stores";
        final String urll = "http://socrip3.kaist.ac.kr:5880/customers";
        Name.setText(s);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                numberofpeople = edit.getText().toString();
                phone = edit2.getText().toString();//this will get a string
                Intent result = new Intent();
                result.putExtra("store", s);
                result.putExtra("phone", phone);
                setResult(RESULT_OK, result);
                number = Integer.parseInt(numberofpeople);
///////////////////가게에서 지금 손님수랑, 웨이팅넘버수 받아오기////////////////
                JsonArrayRequest jjjArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    JSONArray contact = response;
                                    for (int i = 0; i < contact.length(); i++) {
                                        JSONObject jjObject = contact.getJSONObject(i);
                                        if (jjObject.getString("store_name").equals(s)) {
                                            aa = jjObject.getInt("waiting_number");
                                            ID = jjObject.getString("_id");
                                            kiki = jjObject.getInt("customer_number");

/////////////////////store 에put하는 함수임///////////////////////
                                            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                            JSONObject jObject = new JSONObject();
                                            try {
                                                jObject.put("store_name", s);
                                                jObject.put("waiting_number", aa + 1);
                                                jObject.put("people_count", number);
                                                jObject.put("phone", phone);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            JsonObjectRequest jjsonRequest = new JsonObjectRequest(Request.Method.POST, urll, jObject, new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    //TODO: handle success
                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    error.printStackTrace();
                                                    //TODO: handle failure
                                                }
                                            });
                                            Volley.newRequestQueue(Information.getContext()).add(jjsonRequest);
                                        }
                                    }
                                    /////////////////////////////////////////////////////////////////////////////////////
                                    JSONObject hiObject = new JSONObject();
                                    try {
                                        hiObject.put("_id", ID);
                                        hiObject.put("store_name", s);
                                        hiObject.put("waiting_number", aa + 1);
                                        hiObject.put("token", "hihi");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url + "/" + s, hiObject, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            //TODO: handle success
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            error.printStackTrace();
                                            //TODO: handle failure
                                        }
                                    });
                                    Volley.newRequestQueue(Information.getContext()).add(jsonRequest);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Do something with response
                                //mTextView.setText(response.toString());
                                // Process the JSON
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Do something when error occurred
                                error.printStackTrace();
                            }
                        }
                );
                Volley.newRequestQueue(getContext()).add(jjjArrayRequest);
                Toast.makeText(getContext(), "예약 완료!", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }
}