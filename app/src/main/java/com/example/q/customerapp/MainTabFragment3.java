package com.example.q.customerapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainTabFragment3 extends Fragment {

    private String url = "http://socrip3.kaist.ac.kr:9280/stores";
    private String store_name;
    private ArrayList<Menu_Item> menuList = null;
    private Menu_Adapter adapter;
    private static Typeface typeface;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_fragment3, container, false);
        if(typeface == null) {
            typeface = Typeface.createFromAsset(getActivity().getAssets(),
                    "font.ttf");
        }
        setGlobalFont(rootView);
        store_name = "hare";
        final ListView listView = (ListView) rootView.findViewById(R.id.menu_list);



        // get menu list
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url + "/" + store_name, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            menuList = new ArrayList<>();
                            JSONArray tmp = (JSONArray) response.get("menu");
                            for (int i = 0; i < tmp.length(); i++) {
                                JSONObject curr = tmp.getJSONObject(i);
                                String name = curr.getString("name");
                                String price = curr.getString("price");
                                menuList.add(new Menu_Item(name, price));
                            }
                            adapter = new Menu_Adapter(getContext(), menuList);
                            listView.setAdapter(adapter);
                            //adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);

        return rootView;
    }

    private void setGlobalFont(View view) {
        if(view != null) {
            if(view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup)view;
                int vgCnt = viewGroup.getChildCount();
                for(int i = 0; i<vgCnt; i++) {
                    View v = viewGroup.getChildAt(i);
                    if(v instanceof TextView) {
                        ((TextView) v).setTypeface(typeface);
                    }
                    setGlobalFont(v);
                }
            }
        }
    }
}
