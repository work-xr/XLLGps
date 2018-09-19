package com.hsf1002.sky.xllgps.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hsf1002.sky.xllgps.R;
import com.hsf1002.sky.xllgps.model.RxjavaHttpModel;

public class MainActivity extends Activity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.text_view);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RxjavaHttpModel.getInstance().getGpsInfo();
                    }
                }).start();
            }
        });
    }
}
