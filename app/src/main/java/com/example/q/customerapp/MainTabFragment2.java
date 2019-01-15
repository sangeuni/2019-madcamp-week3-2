package com.example.q.customerapp;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class MainTabFragment2 extends Fragment {
    private static Context context;
    private Button button2;
    private TextView CN1, CN2, WN1, WN2;
    int t,wow,aa;
    String ID,IDD;
    final String url = "http://socrip3.kaist.ac.kr:5880/stores";
    final String urll = "http://socrip3.kaist.ac.kr:5880/customers";
    String phonenumber; /////////fragment에서 받아옴
    String storename,newid;  ////////얘도 받아옴 bundle로

    int a, b,c;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment2, container, false);
        button2 = (Button) rootView.findViewById(R.id.button2);
        CN1 = (TextView) rootView.findViewById(R.id.CN1);
        CN2 = (TextView) rootView.findViewById(R.id.CN2);
        WN1 = (TextView) rootView.findViewById(R.id.WN1);
        WN2 = (TextView) rootView.findViewById(R.id.WN2);
        final String url = "http://socrip3.kaist.ac.kr:5880/stores";
        final String urll = "http://socrip3.kaist.ac.kr:5880/customers";


        JsonArrayRequest jjjArrayRequest = new JsonArrayRequest(Request.Method.GET, urll, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Do something with response
                //mTextView.setText(response.toString());
                // Process the JSON
                try {
                    JSONArray contact = response;
                    for (int i = 0; i < contact.length(); i++) {
                        JSONObject jjObject = contact.getJSONObject(i);
                        if (jjObject.getString("phone").equals(phonenumber) && jjObject.getString("store_name").equals(storename)) {
                            a = jjObject.getInt("waiting_number");
                            b = jjObject.getInt("customer_number");
                            c=i;
                            ID=jjObject.getString("_id");
                            CN2.setText(b);
                            WN2.setText(a);
                            break;
                        }
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

////////////////////////////취소버튼누르면 일어나는일
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /////////////////////처음으로 딜리트를하자///////////////////////
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.DELETE, urll + "/" + ID, null, new Response.Listener<JSONObject>() {
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
                Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonRequest);
                ////////////////일단 자기 자신 삭제 후 뒤에있는 커스토머넘버들 수정
                JsonArrayRequest jjjArrayRequest = new JsonArrayRequest(Request.Method.GET, urll, null,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    JSONArray contact = response;
                                    for (int i = c; i < contact.length(); i++) {
                                        JSONObject jjObject = contact.getJSONObject(i);
                                        /////////////////////만약 커스토머넘버가 나보다 크면
                                        if (jjObject.getString("store_name").equals(storename) && jjObject.getInt("customer_number") > b) {
                                            wow = jjObject.getInt("waiting_number");
                                            newid = jjObject.getString("_id");


                                            //////////////////////waiting-number수정작업중
                                            JSONObject hiObject = new JSONObject();
                                            try {
                                                hiObject.put("waiting_number", wow - 1);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            /////////////////////////
                                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, urll + "/" + newid, hiObject, new Response.Listener<JSONObject>() {
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
                                            Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonRequest);
                                        }
                                    }
                                    //////////////////여기까지 customer서버에서 수정


//////////////////if문끝 (뒤에 웨이팅넘버 하나씩 줄이는거 끝)
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
                Volley.newRequestQueue(getActivity().getApplicationContext()).add(jjjArrayRequest);
                //////////////////////////////  .////////////////////////////////////////////////////////
                Toast.makeText(getActivity().getApplicationContext(), "취소 완료!", Toast.LENGTH_SHORT).show();


                ////////////store waitibng number 수정
                JsonArrayRequest kkArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    JSONArray contact = response;
                                    for (int i = 0; i < contact.length(); i++) {
                                        JSONObject jjObject = contact.getJSONObject(i);
                                        if (jjObject.getString("store_name").equals(storename)) {
                                            aa = jjObject.getInt("waiting_number");
                                            IDD = jjObject.getString("_id");
                                            JSONObject hiObject = new JSONObject();
                                            try {
                                                hiObject.put("waiting_number", aa - 1);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
/////////////////////store 에 post하는 함수임///////////////////////
                                            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url + "/" + IDD, hiObject, new Response.Listener<JSONObject>() {
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
                                            Volley.newRequestQueue(getActivity().getApplicationContext()).add(jsonRequest);
                                        }
                                    }
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
                Volley.newRequestQueue(getActivity().getApplicationContext()).add(kkArrayRequest);
            }
//////////////////////////취소버튼누르면일어나는일끝




    });
        return rootView;


    }


    ///버튼 2 누르면 생기는일
//                button2.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    String urlll = "http://socrip3.kaist.ac.kr:5880/customers";
//
//                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.DELETE, urlll, null
//                            new Response.Listener<String>()
//                            {
//                                @Override
//                                public void onResponse(String response) {
//                                    // response
//                                }
//                            },
//                            new Response.ErrorListener()
//                            {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    // error.
//
//                                }
//                            }
//                    );
//                    Volley.newRequestQueue(third.getContext()).add(jsonArrayRequest);
//
//
//                    // Access the RequestQueue through your singleton class.
//                }
//
//                });



}

