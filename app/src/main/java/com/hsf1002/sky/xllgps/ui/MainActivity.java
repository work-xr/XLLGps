package com.hsf1002.sky.xllgps.ui;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hsf1002.sky.xllgps.R;
import com.hsf1002.sky.xllgps.model.RxjavaHttpModel;

public class MainActivity extends Activity {
    private TextView reportPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reportPosition = (TextView)findViewById(R.id.btn);
        reportPosition.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                RxjavaHttpModel.getInstance().pushGpsInfo("gps", "baidu");
            }
        });
    }
}
