package com.grozziie.grozziie_aaam.wifi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.print.PrintHelper;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.grozziie.grozziie_aaam.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.util.Locale;

public class WifiPrintPdf extends AppCompatActivity {
Button pdf_pickkkk,print_relaa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_print_pdf);
        pdf_pickkkk=findViewById(R.id.pdf_pickkkk);
        print_relaa=findViewById(R.id.print_relaa);


        Toolbar toolbar=findViewById(R.id.profile_toolbar);
        String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
        if (defaultlanguage.toLowerCase().toString().equals("english")) {
            toolbar.setTitle("Pdf Select");
            pdf_pickkkk.setText("Pick Pdf");
            print_relaa.setText("Print Pdf");
        }
        else {
            toolbar.setTitle("PDF选择");
            pdf_pickkkk.setText("选择 PDF");
            print_relaa.setText("打印 PDF");
        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        ////flag
        print_relaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==0) {
                    String locallanguage=Locale.getDefault().getDisplayLanguage();
                    if (locallanguage.toLowerCase().equals("english")) {
                        Toast.makeText(WifiPrintPdf.this, "Please firstly select and upload pdf.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(WifiPrintPdf.this, "请先选择并上传pdf", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (flag==1){
                }
            }
        });
        pdf_pickkkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("application/pdf");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 101);

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
    }
    int flag=0;
    Uri filepath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            if (data.getData() != null) {
                filepath=data.getData();
                flag=1;
                try {
                    if (flag==0) {
                        Toast.makeText(this, "please upload a image", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "fffff", Toast.LENGTH_SHORT).show();
                        ContentResolver resolver=getContentResolver();
                        InputStream inputStream=resolver.openInputStream(filepath);
                        Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                        inputStream.close();
                        //Toast.makeText(this, imageuri+""+bitmap, Toast.LENGTH_SHORT).show();
                        Bitmap bitmap1=BitmapFactory.decodeResource(getResources(),R.drawable.imagepost);

                        PrintHelper photoPrinter = new PrintHelper(WifiPrintPdf.this);
                        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

                        photoPrinter.printBitmap(" Testing.pdf"+"-  print", bitmap1);

                    }


                }catch (Exception e) {
                }
            } else
                Toast.makeText(this, "NO FILE CHOSEN", Toast.LENGTH_SHORT).show();

        }
        else {

        }

    }

}