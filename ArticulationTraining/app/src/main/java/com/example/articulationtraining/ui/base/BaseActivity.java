package com.example.articulationtraining.ui.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.articulationtraining.R;

public class BaseActivity extends AppCompatActivity implements BaseContract.View {

    @Override
    public void showResultDialog(int result) {
        final int fail = 0;
        final int success = 1;
        // 創建佈局載入器
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_result_layout, null);

        // 找到佈局中的視圖元素
        TextView resultMessage = dialogView.findViewById(R.id.tv_result);

        switch(result){
            case success:
                resultMessage.setText("好棒!");
                break;
            case fail:
                resultMessage.setText("再加油!");
                break;
        }


        // 創建對話框生成器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);

        // 創建並顯示對話框
        AlertDialog resultDialog = builder.create();
        resultDialog.show();
    }

}
