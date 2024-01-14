package com.example.articulationtraining.ui.articulation.voiceInteraction.monosyllabic.voiceInteraction;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arthenica.mobileffmpeg.ExecuteCallback;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.example.articulationtraining.R;
import com.example.articulationtraining.ui.base.BaseActivity;
import com.example.articulationtraining.utils.api.AzureApiClient;
import com.example.articulationtraining.utils.api.AzureApiService;
import com.example.articulationtraining.utils.api.response.SpeechRecognitionResponse;
import com.example.articulationtraining.utils.pinyin.data.DictionaryData;
import com.example.articulationtraining.utils.pinyin.handling.ChuyinServer;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MonosyllabicVoiceInteractionActivity extends BaseActivity implements MonosyllabicVoiceInteractionContract.View{

    private ImageButton img_mic;
    private ChuyinServer chuyinServer;

    private final int fail = 0;
    private final int success = 1;

    private TextView tv_question;
    private LinearLayout linearLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_interaction);
        setupServer();

        tv_question = findViewById(R.id.tv_question);
        tv_question.setText("ㄍ\nㄣ");

        linearLayout = findViewById(R.id.linearLayout);

        img_mic = findViewById(R.id.img_mic);
        img_mic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // 按住時執行的代碼
                        chuyinServer.startRecording();
                        return true;
                    case MotionEvent.ACTION_UP:
                        // 放開時執行的代碼
                        chuyinServer.stopRecording();
                        return true;
                }
                return false;
            }
        });
    }

    private void setupServer(){
        chuyinServer = new ChuyinServer(this);
        chuyinServer.setOnChuyinListener(onChuyinListener);
    }

    private ChuyinServer.OnChuyinListener onChuyinListener
            = new ChuyinServer.OnChuyinListener() {
        @Override
        public void onSuccess(String responseText) {
            setNewAnswer(responseText);
        }

        @Override
        public void onError(String errorMessage) {
//            view.showError(errorMessage);
        }
    };

    public void setNewAnswer(String answer) {
        linearLayout.removeAllViews();
        Log.e("20240113", "setNewAnswer: " + answer);
        String[] splitString = answer.split("\\|");

        for (String part : splitString) {
            addNewTextView(part);
        }
    }
    public void addNewTextView(String part) {
        Log.e("20240113", "part: " + part);

        LayoutInflater inflater = LayoutInflater.from(MonosyllabicVoiceInteractionActivity.this);
        View childView = inflater.inflate(R.layout.item_pinyin_show, null);
        String regex = "[\\sˊˇˋ˙]+";
        String result = part.replaceAll(regex, "");
        if(result.equals("ㄍㄣ")){
            Log.e("20240113", "成功");
            showResultDialog(success);

        }else{
            showResultDialog(fail);
        }
        TextView tv_chuyin =  childView.findViewById(R.id.tv_chuyin);
        tv_chuyin.setText(result);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(part);
        while (matcher.find()) {
            String match = matcher.group();
            TextView tv_tone =  childView.findViewById(R.id.tv_tone);
            tv_tone.setText(match);
        }

        linearLayout.addView(childView, linearLayout.getChildCount());
    }



//    public void showResultDialog(int result) {
//        // 創建佈局載入器
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.dialog_result_layout, null);
//
//        // 找到佈局中的視圖元素
//        TextView resultMessage = dialogView.findViewById(R.id.tv_result);
//
//        switch(result){
//            case success:
//                resultMessage.setText("好棒!");
//                break;
//            case fail:
//                resultMessage.setText("再加油!");
//                break;
//        }
//
//
//        // 創建對話框生成器
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setView(dialogView);
//
//        // 創建並顯示對話框
//        AlertDialog resultDialog = builder.create();
//        resultDialog.show();
//    }
}