package com.example.divyasethi.qrscanner;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ZbarAct extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView zBarScannerView;
    private boolean isFlash=false;
    private ImageView imageView;
    List<BarcodeFormat> formatList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zbar);
        FrameLayout contentFrame= (FrameLayout) findViewById(R.id.content_frame);
        zBarScannerView=new ZBarScannerView(this);
        contentFrame.addView(zBarScannerView);
        imageView= (ImageView)findViewById(R.id.scan_tick);
      /*  Button qr= (Button)findViewById(R.id.qr);
        Button bar=(Button)findViewById(R.id.bar);
        Button flash= (Button)findViewById(R.id.flash);




        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                formatList=new ArrayList<BarcodeFormat>();
                formatList.add(BarcodeFormat.QRCODE);
                zBarScannerView.setFormats(formatList);
                imageView.setVisibility(View.INVISIBLE);
                zBarScannerView.startCamera();

            }
        });

        bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                formatList=new ArrayList<BarcodeFormat>();
                formatList.add(BarcodeFormat.CODE128);
                zBarScannerView.setFormats(formatList);
                imageView.setVisibility(View.INVISIBLE);
                zBarScannerView.startCamera();
            }
        });


        flash.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(Build.VERSION_CODES.M)
            public void onClick(View v) {
                    isFlash=!isFlash;
                    zBarScannerView.setFlash(isFlash);
                }

            }); */

    }


    @Override
    public void onResume() {
        super.onResume();
        zBarScannerView.setResultHandler(this);
        zBarScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        zBarScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        zBarScannerView.stopCamera();
    if(result!=null){

        imageView.setVisibility(View.VISIBLE);
        imageView.bringToFront();
        Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();

    }
        else
        {
            Toast.makeText(this,"Null Result",Toast.LENGTH_LONG).show();
        }


    }
}
