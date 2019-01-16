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
import com.example.q.customerapp.R;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.rengwuxian.materialedittext.MaterialEditText;

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
    private static Typeface typeface;

    //String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment4, container, false);
        if(typeface == null) {
            typeface = Typeface.createFromAsset(getActivity().getAssets(),
                    "font.ttf");
        }
        setGlobalFont(rootView);
        store_name = "hare";
        listView = (ListView)rootView.findViewById(R.id.listview);
        reviewList = new ArrayList<Review_Item>();
        adapter = new Review_Adapter(getContext().getApplicationContext(),reviewList);

        FloatingActionButton fab = rootView.findViewById(R.id.fab);

        listView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View register_layout = LayoutInflater.from(getContext())
                        .inflate(R.layout.add_review_layout, null);
                new MaterialStyledDialog.Builder(getContext())
                        .setIcon(R.drawable.heart2)
                        .setHeaderColor(R.color.colorPrimary)
                        .setTitle("Add Review")
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
                                final MaterialEditText edit_name = register_layout.findViewById(R.id.review);


                                if (TextUtils.isEmpty(edit_name.getText().toString())) {
                                    Toast.makeText(getContext(), "Email cannot be null or empty", Toast.LENGTH_SHORT).show();
                                    return;
                                }


                                try {
                                    add_menu(edit_name.getText().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }).show();
            }
        });
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
    private void add_menu(final String name) throws JSONException {

        JSONObject postparams = new JSONObject();
        JSONObject post = new JSONObject();
//        postparams.put("write", name);
        post.put("review", name);

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
