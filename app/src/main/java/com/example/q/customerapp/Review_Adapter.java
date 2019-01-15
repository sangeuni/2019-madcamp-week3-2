package com.example.q.customerapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.q.customerapp.R;

import java.util.List;

public class Review_Adapter extends BaseAdapter {
    private Context context;
    private List<Review_Item> reviewList;

    public Review_Adapter(Context context, List<Review_Item> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public int getCount() {
        return reviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return reviewList.get(position).getText();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context,R.layout.review_item,null);
        TextView itemText = (TextView)v.findViewById(R.id.list_item);
        itemText.setText(reviewList.get(position).getText());
        return v;
    }
}
