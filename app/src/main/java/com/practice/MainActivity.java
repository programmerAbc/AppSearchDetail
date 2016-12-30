package com.practice;

import android.graphics.drawable.ClipDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
AppDetailLayout appDetailLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appDetailLayout= (AppDetailLayout) findViewById(R.id.appDetailLayout);
    }

    @Override
    public void onBackPressed() {
        appDetailLayout.doFinish(2000);
    }
}
