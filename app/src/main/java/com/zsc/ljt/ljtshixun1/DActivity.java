package com.zsc.ljt.ljtshixun1;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

public class DActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StringBuilder sb = new StringBuilder("李景涛\n");
        sb.append("2014030401027\n").append("男\n").append("广东河源");
        TextView tv = new TextView(this);
        tv.setText(sb.toString());
        tv.setTextSize(24);
        tv.setTextColor(Color.BLACK);
        tv.setGravity(Gravity.CENTER);
        setContentView(tv);
    }
}
