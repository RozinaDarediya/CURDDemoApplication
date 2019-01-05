package com.theta.curddemoapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theta.curddemoapplication.Global.Log;
import com.theta.curddemoapplication.R;
import com.theta.curddemoapplication.model.EnquiryDataModel;

import java.util.ArrayList;

/**
 * Created by Rozina on 04/01/19.
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyHolder> {

    Context context;
    ArrayList<EnquiryDataModel> enquiryDataModels;

    public DataAdapter(Context context, ArrayList<EnquiryDataModel> enquiryDataModels) {
        this.context = context;
        this.enquiryDataModels = enquiryDataModels;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.data_row, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        try {
            EnquiryDataModel model = enquiryDataModels.get(i);
            Glide.with(context).load(model.getImage()).into(myHolder.image);
            myHolder.txtUserName.setText(model.getName());
            myHolder.txtEmail.setText(model.getEmail());
            myHolder.txtEducation.setText(model.getEducation());

        } catch (Exception e) {
            Log.e(e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return enquiryDataModels.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView txtUserName;
        TextView txtEmail;
        TextView txtEducation;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtEducation = itemView.findViewById(R.id.txtEducation);
        }
    }
}
