package com.example.articulationtraining.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.articulationtraining.R;
import com.example.articulationtraining.ui.articulation.ArticulationActivity;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private Button btn_ListenDiscern,
                   btn_Articulation,
                   btn_History,
                   btn_Setting;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        // UI設置
        setupUI();
    }

    public void setupUI() {
        btn_ListenDiscern = findViewById(R.id.btn_ListenDiscern);
        btn_Articulation = findViewById(R.id.btn_Articulation);
        btn_History = findViewById(R.id.btn_History);
        btn_Setting = findViewById(R.id.btn_Setting);

        // 聽辨訓練
        btn_ListenDiscern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // 構音訓練
        btn_Articulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, ArticulationActivity.class);
                startActivity(intent);
            }
        });
        // 歷史紀錄
        btn_History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // 設定
        btn_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}