package me.dm7.golive.sayac.sample;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;
import me.dm7.barcodescanner.zbar.sample.R;

public class SimpleScannerActivity extends BaseScannerActivity implements ZBarScannerView.ResultHandler {
    public static String result="0";
    private ZBarScannerView mScannerView;

    TextView str2;
    @Override
    public void onCreate(Bundle state) {
        logSave.logAdd("SimpleScannerActivity onCreate");
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        str2 = new TextView(this);
        str2.setText("Barkodu Okutun");
        str2.setTextSize(20);
        str2.setTextColor(Color.GREEN);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT,Gravity.CENTER);
        contentFrame.addView(str2,params);
    }
    @Override
    public void onResume() {
        super.onResume();
        logSave.logAdd("SimpleScannerActivity onResume");
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
    @Override
    public void onBackPressed() {
        logSave.logAdd("Back Click");
        finish();
        launchActivity(MainActivity.class);
    }
    @Override
    public void onPause() {
        logSave.logAdd("SimpleScannerActivity onPause");
        super.onPause();
        mScannerView.stopCamera();
    }
    public void launchActivity(Class<?> clss) {
        Intent intent = new Intent(this, clss);
        startActivity(intent);
    }
    @Override
    public void handleResult(Result rawResult) {
        PostInfo.Barcode = rawResult.getContents();
        logSave.logAdd("SimpleScannerActivity HandleResult: " + rawResult.getContents());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
            }
        }, 2000);
        logSave.logAdd("SimpleScannerActivity Kapatıldı.");
        finish();
        launchActivity(sayacmain.class);
    }
}
