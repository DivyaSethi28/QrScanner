package com.example.divyasethi.qrscanner;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ZbarAct extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mZBarScannerView;
    private ImageView mImageView;
    private ActionBar mActionBar;
    private TextView mTextView;
    private FrameLayout mContainer;
    private int RESET_ID;
    private Context context;
    private static boolean flag = false;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zbar);
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(getString(R.string.actionbar_title));
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mContainer = (FrameLayout) findViewById(R.id.container);
        mZBarScannerView =(ZBarScannerView)findViewById(R.id.scanner_view);
        mImageView = (ImageView) findViewById(R.id.xScanCompleate);
        mTextView=(TextView)findViewById(R.id.title);
        mTextView.bringToFront();
        mZBarScannerView.setVisibility(View.INVISIBLE);
        context=getApplicationContext();
        if (Build.VERSION.SDK_INT >= 23) {
            if (isCameraPermissionGranted()) {
                initiateScan();
            } else {
                checkCameraPermission();
            }
        }
        else
        {
            initiateScan();
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void checkCameraPermission() {
        if(!flag){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Snackbar.make(mContainer, "You must enable Camera to continue scanning", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(ZbarAct.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
            }
        } else {
            Snackbar.make(mContainer, "You must enable Camera to continue scanning", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Ok", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            final Intent i = new Intent();
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            i.addCategory(Intent.CATEGORY_DEFAULT);
                            i.setData(Uri.parse("package:" + context.getPackageName()));
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(i);
                        }}).show();
        }
    }

    private void initiateScan() {
        mZBarScannerView.setVisibility(View.VISIBLE);
        mZBarScannerView.setupScanner();
        mImageView.setVisibility(View.INVISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isCameraPermissionGranted() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(grantResults.length > 0 ) {
            if (requestCode == PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateScan();
            } else {
                if(!flag){
                    final AlertDialog builder=new AlertDialog.Builder(ZbarAct.this).create();
                    builder.setTitle("Permission Denied");
                    builder.setMessage("You must grant Camera permission to enable Scaning.");
                    builder.setButton(DialogInterface.BUTTON_POSITIVE,"Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ZbarAct.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                        }
                    });
                    builder.setButton(DialogInterface.BUTTON_NEGATIVE,"Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            builder.dismiss();
                            flag = true;
                        }
                    });
                    builder.show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if(menuItem.getItemId() == android.R.id.home ) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        } else if(menuItem.getItemId() == RESET_ID) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mImageView.setVisibility(View.INVISIBLE);
                }
            }, 3000);
            return true;
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuItem reset= menu.add("RESET");
        reset.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        RESET_ID=reset.getItemId();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mZBarScannerView.setResultHandler(this);
        mZBarScannerView.startCamera();
        if (Build.VERSION.SDK_INT >= 23)
         if(isCameraPermissionGranted())
                initiateScan();
    }

    @Override
    public void onPause() {
        super.onPause();
        mZBarScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        mZBarScannerView.stopCamera();
    if(result!=null){
        mImageView.setVisibility(View.VISIBLE);
        mImageView.bringToFront();
        Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mImageView.setVisibility(View.INVISIBLE);
                mZBarScannerView.startCamera();
            }
        }, 3000);
    } else {
            Toast.makeText(this,"Null Result",Toast.LENGTH_LONG).show();
        }
    }
}
