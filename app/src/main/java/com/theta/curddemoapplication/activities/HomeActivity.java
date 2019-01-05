package com.theta.curddemoapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.theta.curddemoapplication.Common.AppConstants;
import com.theta.curddemoapplication.Common.BaseActivity;
import com.theta.curddemoapplication.Global.GlobalClass;
import com.theta.curddemoapplication.Global.Log;
import com.theta.curddemoapplication.R;
import com.theta.curddemoapplication.adapter.DataAdapter;
import com.theta.curddemoapplication.database.DatabaseHelper;
import com.theta.curddemoapplication.listener.RecyclerItemClickListener;
import com.theta.curddemoapplication.model.EnquiryDataModel;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    TextView nodata;

    DataAdapter dataAdapter;

    // Database Helper
    DatabaseHelper db;

    ImageView ivLeft;
    ImageView ivRight;
    ArrayList<EnquiryDataModel> dataModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolBar();
        ivLeft = setLeftMenu();
        ivRight = setRightMenu();
        setTitle("Data");

        initView();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EnquiryDataModel model = dataModels.get(position);
                Log.e("selected offer is " + model.getID());
                Intent intent = new Intent(HomeActivity.this, EnquiryFormActivity.class);
                intent.putExtra("data", model);
                startActivity(intent);
            }
        }));
    }

    private void initView() {
        db = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerview);
        nodata = findViewById(R.id.nodata);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ivRight.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
    }

    private void getData() {
        dataModels = new ArrayList<>();
        dataModels = db.getEnquiryData();
        if (dataModels.size() > 0) {
            dataAdapter = new DataAdapter(this, dataModels);
            recyclerView.setAdapter(dataAdapter);
            nodata.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            nodata.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivRight) {
            // add enquiry form
            Intent intent = new Intent(HomeActivity.this, EnquiryFormActivity.class);
            startActivity(intent);
        }
        if (view.getId() == R.id.ivLeft) {
            GlobalClass.storePreference(AppConstants.PREF_KEY_IS_LOGGED_IN, false);
            // add enquiry form
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
