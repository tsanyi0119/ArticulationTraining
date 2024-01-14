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
import com.example.articulationtraining.utils.api.AzureApiClient;
import com.example.articulationtraining.utils.api.AzureApiService;
import com.example.articulationtraining.utils.api.response.SpeechRecognitionResponse;
import com.example.articulationtraining.utils.pinyin.data.DictionaryData;

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

public class MonosyllabicVoiceInteractionActivity extends AppCompatActivity {

    private MediaRecorder mediaRecorder;
    private ImageButton img_mic;
    private String filePath , outPath;
    private AzureApiService azureApiService;
    private static final int REQUEST_PERMISSION_CODE = 000;
    private DictionaryData dictionaryData;

    private final int fail = 0;
    private final int success = 1;

    private TextView tv_question;
    private LinearLayout linearLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_interaction);

        tv_question = findViewById(R.id.tv_question);
        tv_question.setText("ㄍ\nㄣ");

        linearLayout = findViewById(R.id.linearLayout);

        dictionaryData = new DictionaryData();

        requestPermission();
        mediaRecorder = new MediaRecorder();
        AudioRecorder();

        img_mic = findViewById(R.id.img_mic);
        img_mic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // 按住時執行的代碼
                        deleteFiles();
                        if (ContextCompat.checkSelfPermission(MonosyllabicVoiceInteractionActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                            // 開始錄音
                            try {
                                if (mediaRecorder == null) {
                                    mediaRecorder = new MediaRecorder();
                                } else {
                                    mediaRecorder.reset();
                                }
                                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                                mediaRecorder.setOutputFile(filePath);
                                mediaRecorder.prepare();
                                mediaRecorder.start();
                            } catch (IOException | RuntimeException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // 如果尚未授權錄音權限，再次請求權限
                            requestPermission();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        // 放開時執行的代碼
                        try {
                            mediaRecorder.stop();
                            setffmpeg();
                        } catch (IllegalStateException e) {
                            // 處理錯誤
                            e.printStackTrace();
                    }
                        return true;
                }
                return false;
            }
        });
    }

    private String convertToPinyin(String text) {
        StringBuilder pinyinBuilder = new StringBuilder();

        for (char c : text.toCharArray()) {
            if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                // 如果是漢字，則轉換成拼音
                String[] pinyinArray = PinyinHelper.toMPS2PinyinStringArray(c);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    pinyinBuilder.append(pinyinArray[0]); // 取第一個拼音
                } else {
                    pinyinBuilder.append(c); // 如果沒有拼音，保留原字符
                }
            } else {
                // 如果是非漢字字符，直接保留
                pinyinBuilder.append(c);
            }
        }

        return pinyinBuilder.toString().toUpperCase();
    }

    // 將語音識別結果按照1到5的數字進行切割並顯示
    private String processAndDisplayResults(String text) {
        // 使用正規表達式進行匹配，將數字前的字母和數字分組
        String[] parts = text.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
        String str = "";
        StringBuilder resultStringBuilder = new StringBuilder();
        for (String part : parts) {
            if(dictionaryData.phoneticMap.get(part.trim()) != null){
                str = str + dictionaryData.phoneticMap.get(part.trim());
            }
        }

        return str;
    }

    private void requestPermission() {
        // 請求錄音權限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
        }
    }

    private void AudioRecorder() {
        // 獲取外部存儲的目錄
        File externalDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);

        // 檢查目錄是否存在，如果不存在，創建它
        if (!externalDir.exists()) {
            externalDir.mkdirs();
        }

        // 設置錄音的文件路徑
        filePath = new File(externalDir, "recorded_audio.mp3").getAbsolutePath();
        outPath = new File(externalDir, "recorded_audio2.wav").getAbsolutePath();
//        filePath = new File(externalDir, "recorded_audio.wav").getAbsolutePath();

    }

    private void setffmpeg(){
        // 原始 MP3 文件路徑
        String src = filePath;

        // 轉換後 WAV 文件路徑
        String dst = outPath;

        // 轉換
        FFmpeg.executeAsync(
                "-y -i " + src + " -c:a pcm_s16le -ar 44100 -ac 2 " + dst, new ExecuteCallback() {
                    @Override
                    public void apply(final long executionId, final int returnCode) {
                        // 根據returnCode進行處理
                        if (returnCode == RETURN_CODE_SUCCESS) {
                            // FFmpeg執行成功
                            Log.e("TAG", "FFmpeg執行成功");
                            getAzureSpeech(dst);
                        } else if (returnCode == RETURN_CODE_CANCEL) {
                            // 使用者取消了執行
                            Log.e("TAG", "使用者取消了執行");
                        }
                    }
                });
    }

    private void deleteFiles(){
        File audioFile = new File(filePath);
        File convertedFile = new File(outPath);
        if (audioFile.exists() && audioFile.isFile()) {
            audioFile.delete();
        }
        if (convertedFile.exists() && convertedFile.isFile()) {
            convertedFile.delete();
        }
        Log.d("20240107", "deleteFiles：" + Thread.currentThread().getName());
    }

    private void getAzureSpeech(String filePath) {

        AzureApiClient azureApiClient = new AzureApiClient();
        azureApiService = azureApiClient.getAzureApiService();

        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/wav"), file);

        azureApiService.recognizeSpeech("eyJhbGciOiJFUzI1NiIsImtpZCI6ImtleTEiLCJ0eXAiOiJKV1QifQ.eyJyZWdpb24iOiJlYXN0YXNpYSIsInN1YnNjcmlwdGlvbi1pZCI6IjVlZjhhN2Q0M2JjNjQ2MjliMTBlZWZkNWQ2NDI1YzI3IiwicHJvZHVjdC1pZCI6IlNwZWVjaFNlcnZpY2VzLkYwIiwiY29nbml0aXZlLXNlcnZpY2VzLWVuZHBvaW50IjoiaHR0cHM6Ly9hcGkuY29nbml0aXZlLm1pY3Jvc29mdC5jb20vaW50ZXJuYWwvdjEuMC8iLCJhenVyZS1yZXNvdXJjZS1pZCI6Ii9zdWJzY3JpcHRpb25zL2NmNDkwOWQwLTcwNDAtNGY2NS04MTU4LWU3YTk4MGQ5MWFiMC9yZXNvdXJjZUdyb3Vwcy9BenVyZV9TcGVlY2gvcHJvdmlkZXJzL01pY3Jvc29mdC5Db2duaXRpdmVTZXJ2aWNlcy9hY2NvdW50cy9BenVyZVNwZWVjaE51dGMiLCJzY29wZSI6InNwZWVjaHNlcnZpY2VzIiwiYXVkIjoidXJuOm1zLnNwZWVjaHNlcnZpY2VzLmVhc3Rhc2lhIiwiZXhwIjoxNzA0NDY2ODA3LCJpc3MiOiJ1cm46bXMuY29nbml0aXZlc2VydmljZXMifQ.0xCbGt6AKTLO-cvm5euBdv4MtWbHmJ3BZgoq7dV2oCU59CUhqQiovcS5iuIi6A5TCKaES7_B0ECGn7Yc7IpwEw",
                        "audio/wav",
                        "b942541d1814476183a5bdaa5e49f65c", requestFile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SpeechRecognitionResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull SpeechRecognitionResponse speechRecognitionResponse) {
                        Log.e("20240111 Azure", "onNext: " + speechRecognitionResponse.getDisplayText());
                        String pinyin = convertToPinyin(speechRecognitionResponse.getDisplayText());
                        Log.e("20240111 Azure", "onNext: " + pinyin);
                        setNewAnswer(processAndDisplayResults(pinyin));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("20240111 Azure", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

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

    public void showResultDialog(int result) {
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