package com.example.scannerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ScanAdapter extends ArrayAdapter<String> {

    Context mContext;
    ArrayList<ScanType> Scans;

    public ScanAdapter(Context context, ArrayList<ScanType> products) {
        super(context, R.layout.listview_item);
        this.Scans = products;
        mContext = context;


    }

    @Override
    public int getCount() {
        return Scans.size();
    }

    @NonNull
    @Override
   /* public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.listview_item, parent, false);
            mViewHolder.mFlag = (ImageView) convertView.findViewById(R.id.imageView);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mFlag.setImageResource(flags[position]);
        mViewHolder.mName.setText(names[position]);

        return convertView;
    }*/


    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) mContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.listview_item, parent, false);

        TextView txtDate = convertView.findViewById(R.id.txtDate);

        txtDate.setText("Scan Timestamp: " + Scans.get(position).getTimeStamp());


        return convertView;
    }



    static class ViewHolder {
        ImageView contactImage;
        TextView contactName;
        TextView contactNumber;
        TextView contactEmail;
    }
}
