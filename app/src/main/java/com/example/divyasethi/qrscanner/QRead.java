package com.example.divyasethi.qrscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;

import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class QRead extends AppCompatActivity {

    private SurfaceView surfaceView;
    private QREader qrEader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qread);
        surfaceView=(SurfaceView)findViewById(R.id.surface);
        qrEader=new QREader.Builder(this, surfaceView, new QRDataListener() {
            @Override
            public void onDetected(String data) {

                Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();

            }
        }).facing(QREader.BACK_CAM).enableAutofocus(true).height(300).width(300).build();

        qrEader.init();


    }

    @Override
    protected void onStart(){

        super.onStart();
        qrEader.start();
    }

    @Override
    protected void onDestroy(){

        super.onDestroy();
        qrEader.releaseAndCleanup();

    }
}
