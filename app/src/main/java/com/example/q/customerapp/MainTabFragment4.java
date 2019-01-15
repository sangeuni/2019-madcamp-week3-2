package com.example.q.customerapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.q.customerapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainTabFragment4 extends Fragment {
    private ListView listView;
    private Review_Adapter adapter;
    private List<Review_Item> reviewList = null;
    String url = "http://socrip3.kaist.ac.kr:9280/stores";
    String store_name;

    //String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment4, container, false);

        store_name = "hare";
        listView = (ListView)rootView.findViewById(R.id.listview);
        reviewList = new ArrayList<Review_Item>();
        adapter = new Review_Adapter(getContext().getApplicationContext(),reviewList);

        listView.setAdapter(adapter);

        JsonObjectRequest ArrayRequest = new JsonObjectRequest(Request.Method.GET, url+"/"+store_name, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object = response;
                    JSONArray reviewArray = object.getJSONArray("review");
                    String review;
                    int count = 0;
                    while (count < reviewArray.length()) {
                        JSONObject reviewObject = reviewArray.getJSONObject(count);
                        review = reviewObject.getString("write");
                        //phone = object.getString("phone");
                        Review_Item item = new Review_Item(review);
                        reviewList.add(item);
                        count++;
                    }
                    adapter.notifyDataSetChanged();
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
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(ArrayRequest);

        return rootView;
    }

}
