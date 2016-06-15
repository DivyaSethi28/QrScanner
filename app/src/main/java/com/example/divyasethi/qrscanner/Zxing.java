package com.example.divyasethi.qrscanner;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;


public class Zxing extends AppCompatActivity {

    private DecoratedBarcodeView barcodeView;
    private ImageView imageView;
    private ActionBar actionBar;

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
        setContentView(R.layout.activity_zxing);
        if(!(ContextCompat.checkSelfPermission(getApplicationContext(),"android.permission.CAMERA")== PackageManager.PERMISSION_DENIED)) {

            Toast.makeText(getApplicationContext(),"Camera available",Toast.LENGTH_SHORT).show();
        }
          else  {
                String[] permissions= new String[1];
                permissions[0]= new String("android.permission.CAMERA");
                ActivityCompat.requestPermissions(this,permissions,0);

        }
        barcodeView= (DecoratedBarcodeView)findViewById(R.id.decorated);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Scan Shipment");
        barcodeView.setStatusText("");
        barcodeView.removeViewAt(1);
        barcodeView.decodeContinuous(barcodeCallback);
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
