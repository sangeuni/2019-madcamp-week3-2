package com.example.q.customerapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_fragment3, container, false);
        store_name = "hare";
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        final ListView listView = (ListView) rootView.findViewById(R.id.menu_list);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View register_layout = LayoutInflater.from(getContext())
                        .inflate(R.layout.add_menu_layout, null);
                new MaterialStyledDialog.Builder(getContext())
                        .setIcon(R.drawable.heart)
                        .setHeaderColor(R.color.colorPrimary)
                        .setTitle("Add Menu")
                        .setDescription("Please fill all fields")
                        .setCustomView(register_layout)
                        .setNegativeText("CANCEL")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveText("REGISTER")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                final MaterialEditText edit_name = register_layout.findViewById(R.id.name);
                                final MaterialEditText edit_price = register_layout.findViewById(R.id.price);

                                if (TextUtils.isEmpty(edit_name.getText().toString())) {
                                    Toast.makeText(getContext(), "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                if (TextUtils.isEmpty(edit_price.getText().toString())) {
                                    Toast.makeText(getContext(), "Name cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                try {
                                    add_menu(edit_name.getText().toString(), edit_price.getText().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).show();
            }
        });


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

    private void add_menu(final String name, final String price) throws JSONException {

        JSONObject postparams = new JSONObject();
        JSONObject post = new JSONObject();
        postparams.put("name", name);
        postparams.put("price", price);
        post.put("menu", postparams);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url + "/" + store_name, post,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Failure Callback
                    }
                });
// Adding the request to the queue along with a unique string tag
        Volley.newRequestQueue(getActivity()).add(jsonObjReq);
    }
}
