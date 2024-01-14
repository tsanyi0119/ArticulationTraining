package com.example.articulationtraining.ui.articulation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.articulationtraining.R;
import com.example.articulationtraining.ui.articulation.voiceInteraction.individualCharacter.chooseChuyin.IndividualCharacterChooseChuyinActivity;
import com.example.articulationtraining.ui.articulation.voiceInteraction.monosyllabic.chooseChuyin.MonosyllabicChooseChuyinActivity;
import com.example.articulationtraining.ui.articulation.voiceInteraction.vocabulary.chooseChuyin.VocabularyChooseChuyinActivity;

public class ArticulationActivity extends AppCompatActivity {
    Button btn_monosyllabic,btn_individualCharacter;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articulation);
        // 單音訓練
        btn_monosyllabic = findViewById(R.id.btn_monosyllabic);
        btn_monosyllabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ArticulationActivity.this, MonosyllabicChooseChuyinActivity.class);
                startActivity(intent);
            }
        });
        // 單字訓練
        btn_individualCharacter = findViewById(R.id.btn_individualCharacter);
        btn_individualCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ArticulationActivity.this, IndividualCharacterChooseChuyinActivity.class);
                startActivity(intent);
            }
        });
        // 詞彙訓練
        Button btn_vocabulary = findViewById(R.id.btn_vocabulary);
        btn_vocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ArticulationActivity.this, VocabularyChooseChuyinActivity.class);
                startActivity(intent);

            }
        });
    }
}