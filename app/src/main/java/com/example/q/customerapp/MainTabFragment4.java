package com.example.q.customerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainTabFragment4 extends Fragment {
    private ListView listView;
    private Review_Adapter adapter;
    private List<Review_Item> reviewList = null;
    String url = "http://socrip3.kaist.ac.kr:5880/stores";

    //String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment4, container, false);


        listView = (ListView)rootView.findViewById(R.id.listview);
        reviewList = new ArrayList<Review_Item>();
        adapter = new Review_Adapter(getContext().getApplicationContext(),reviewList);

        listView.setAdapter(adapter);

        JsonArrayRequest jjjArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONArray jsonArray = response;
                    String txt;
                    int count = 0;
                    while (count < jsonArray.length()) {
                        JSONObject object = jsonArray.getJSONObject(count);
                        txt = object.getString("store_name");

                        //phone = object.getString("phone");
                        Review_Item item = new Review_Item(txt);
                        reviewList.add(item);
                        count++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                    }
                }

        );
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(jjjArrayRequest);

        return rootView;
    }

}
