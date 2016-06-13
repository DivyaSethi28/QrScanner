package com.example.divyasethi.qrscanner;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private IntentIntegrator intentIntegrator;
    private int flag = 0;
    private DecoratedBarcodeView barcodeView;
    private ImageView imageView;

    private BarcodeCallback barcodeCallback= new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {

            Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();
            imageView=(ImageView)findViewById(R.id.scan_tick1);
            imageView.setVisibility(View.VISIBLE);
            imageView.bringToFront();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentIntegrator = new IntentIntegrator(this);
        barcodeView= (DecoratedBarcodeView)findViewById(R.id.decorated);
        Intent intent=intentIntegrator.createScanIntent();
       // barcodeView.initializeFromIntent(intent);
        barcodeView.decodeContinuous(barcodeCallback);
        /*barcodeView.setEnabled(true);
        if(!barcodeView.isActivated())
            Log.e("Activated","Not Activated");
        if(!barcodeView.isEnabled())
            Log.e("Enabled","Not Enabled");*/


    }

    public void onZxing(View view) {

        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.initiateScan();

    }

    public void onZBar(View view) {

        Intent intent = new Intent(this, ZbarAct.class);
        startActivity(intent);

    }

   public void onActivityResult(int requestCode, int resultCode, Intent intent) {

       Log.e("OnActivityResult", "Inside onActivityresult...");
        IntentResult scanresult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (resultCode != RESULT_CANCELED)
            if (scanresult != null) {
                if (flag == 0)
                    flag = 1;
                Toast.makeText(this, scanresult.getContents(), Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(200);
                    intentIntegrator.initiateScan();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else
                Toast.makeText(this, "NULL RESULT", Toast.LENGTH_LONG).show();
        else if (flag == 0)
            Toast.makeText(this, "Operation Cancelled", Toast.LENGTH_LONG).show();

    }


    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();


    }

    @Override
    protected void onResume() {

        super.onResume();
        barcodeView.resume();
    }


}
