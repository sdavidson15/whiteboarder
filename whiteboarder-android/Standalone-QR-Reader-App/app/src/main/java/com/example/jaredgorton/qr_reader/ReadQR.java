package com.example.jaredgorton.qr_reader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ReadQR extends AppCompatActivity {
//    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_qr);
    }

//    public void QRScanner(View view) {
//        mScannerView = new ZXingScannerView(this);  // programmatically initialize the scanner view
//        setContentView(mScannerView);
//        mScannerView.setResultHandler(this);    // register ourselves as a handler for the scan results
//        mScannerView.startCamera();     // start camera
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mScannerView.stopCamera();  // stop camera on pause
//    }
}
