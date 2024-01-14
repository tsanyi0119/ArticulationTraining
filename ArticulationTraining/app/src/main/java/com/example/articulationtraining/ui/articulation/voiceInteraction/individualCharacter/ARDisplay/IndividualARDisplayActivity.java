package com.example.articulationtraining.ui.articulation.voiceInteraction.individualCharacter.ARDisplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.articulationtraining.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class IndividualARDisplayActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_ardisplay);
        ImageView overlayImageView = findViewById(R.id.img_target);
        if (checkCameraPermission()) {
            startCamera();
        } else {
            requestCameraPermission();
        }

        overlayImageView.setOnTouchListener(new View.OnTouchListener() {
            private float initialX, initialY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 記錄觸摸事件時的初始位置
                        initialX = event.getRawX();
                        initialY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 計算移動距離
                        float deltaX = event.getRawX() - initialX;
                        float deltaY = event.getRawY() - initialY;

                        // 設置新的位置
                        view.animate()
                                .x(view.getX() + deltaX)
                                .y(view.getY() + deltaY)
                                .setDuration(0)
                                .start();

                        // 同時移動 imageView8，保持初始相對位置
                        ImageView imageView8 = findViewById(R.id.img_message);
                        imageView8.animate()
                                .x(imageView8.getX() + deltaX)
                                .y(imageView8.getY() + deltaY)
                                .setDuration(0)
                                .start();

                        TextView textView = findViewById(R.id.tv_title);
                        textView.animate()
                                .x(textView.getX() + deltaX)
                                .y(textView.getY() + deltaY)
                                .setDuration(0)
                                .start();

                        // 更新初始位置
                        initialX = event.getRawX();
                        initialY = event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        // 在這裡呼叫 performClick，以滿足可訪問性的規範
                        view.performClick();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });


    }
    private void startCamera() {
        PreviewView previewView = findViewById(R.id.viewFinder);
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider, previewView);
            } catch (ExecutionException | InterruptedException e) {
                // 处理相机初始化错误
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(ProcessCameraProvider cameraProvider, PreviewView previewView) {
        Preview preview = new Preview.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);
    }

    // 檢查相機權限
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    // 請求相機權限
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }

    // 檢查權限結果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 相機權限已獲得，啟動相機
                Log.e("TAG", "onRequestPermissionsResult: "+ "相機權限已獲得，啟動相機");
                startCamera();
            } else {
                Log.e("TAG", "onRequestPermissionsResult: "+ "拒絕相機權限");
                // 拒絕相機權限，可以根據情況進行處理，例如顯示一個提示或者重新請求權限
            }
        }
    }
}