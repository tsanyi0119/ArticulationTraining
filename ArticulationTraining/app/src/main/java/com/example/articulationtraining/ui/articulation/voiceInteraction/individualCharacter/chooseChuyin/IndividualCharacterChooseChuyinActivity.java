package com.example.articulationtraining.ui.articulation.voiceInteraction.individualCharacter.chooseChuyin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.articulationtraining.R;
import com.example.articulationtraining.ui.articulation.voiceInteraction.individualCharacter.ARDisplay.IndividualARDisplayActivity;
import com.example.articulationtraining.ui.articulation.voiceInteraction.individualCharacter.voiceInteraction.IndividualCharacterVoiceInteractionActivity;
import com.example.articulationtraining.ui.articulation.voiceInteraction.monosyllabic.chooseChuyin.MonosyllabicChooseChuyinActivity;
import com.example.articulationtraining.ui.articulation.voiceInteraction.monosyllabic.voiceInteraction.MonosyllabicVoiceInteractionActivity;

public class IndividualCharacterChooseChuyinActivity extends AppCompatActivity {

    String[] zhuyinArray = {
            "ㄅ", "ㄆ", "ㄇ", "ㄈ", "ㄉ", "ㄊ", "ㄋ", "ㄌ", "ㄍ", "ㄎ",
            "ㄏ", "ㄐ", "ㄑ", "ㄒ", "ㄓ", "ㄔ", "ㄕ", "ㄖ", "ㄗ", "ㄘ",
            "ㄙ", "ㄚ", "ㄛ", "ㄜ", "ㄝ", "ㄞ", "ㄟ", "ㄠ", "ㄡ", "ㄢ",
            "ㄣ", "ㄤ", "ㄥ", "ㄦ", "ㄧ", "ㄨ", "ㄩ"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_character_choose_chuyin);

        // 取得GridLayout元素
        GridLayout gridLayout = findViewById(R.id.GridLayout_chuyin);

        // 設定GridLayout的列數和行數
        // 設定GridLayout的列數和行數
        //(totalItems + columns - 1) / columns;
        gridLayout.setColumnCount(3);
        gridLayout.setRowCount((zhuyinArray.length + 2) / 3);

        // 動態建立Button並添加到GridLayout中
        for (int i = 0; i < zhuyinArray.length; i++) {
            Button button = new Button(this);
            button.setText("Button " + zhuyinArray[i]);

            // 設定Button的Layout參數
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.setGravity(Gravity.CENTER);

            // 設定Button的列和行位置
            params.rowSpec = GridLayout.spec(i / 3);
            params.columnSpec = GridLayout.spec(i % 3);

            // 將Button添加到GridLayout中
            gridLayout.addView(button, params);

            // 添加點擊事件
            int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 在這裡處理Button的點擊事件
                    // 例如，顯示一個Toast消息
                    Toast.makeText(IndividualCharacterChooseChuyinActivity.this, "Button " + (finalI + 1) + " 被點擊了", Toast.LENGTH_SHORT).show();
                }
            });
        }
        Button btn_choose_submit = findViewById(R.id.btn_choose_submit);
        btn_choose_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳頁
                Intent intent = new Intent(IndividualCharacterChooseChuyinActivity.this, IndividualARDisplayActivity.class);
                startActivity(intent);
            }
        });
    }
}