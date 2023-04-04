package com.grozziie.grozziie_aaam.wifi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.print.PrintHelper;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.grozziie.grozziie_aaam.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.Locale;

public class WifiImagePrint extends AppCompatActivity {
    Button sendButton,printbutton;
    ImageView pick_image_loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_image_print2);
        sendButton=findViewById(R.id.sendButton);
        printbutton=findViewById(R.id.printbutton);
        pick_image_loader=findViewById(R.id.pick_image_loader);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
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
        pick_image_loader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent = new Intent();
                                intent.setType("image/*");
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
        ///toolbar
        Toolbar toolbar=findViewById(R.id.profile_toolbar);
        String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
        if (defaultlanguage.toLowerCase().toString().equals("english")) {
            toolbar.setTitle("Image Select");
            sendButton.setText("Pick Up Image");
            printbutton.setText("Print This Image");
        }
        else {
            toolbar.setTitle("图像选择");
            sendButton.setText("拿起图像");
            printbutton.setText("打印此图像");

        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);

    }
    Uri imageUri;
    Bitmap imageBitmap;
    int  flag=0;
    String fileName;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            try {
                File path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                imageuri = data.getData();



                file = new File(imageuri.getPath());//create path from uri

                ////file path


                flag=1;
                if (flag==1) {
                    printbutton.setVisibility(View.VISIBLE);
                    sendButton.setVisibility(View.GONE);
                }
                else if (flag==0){
                    printbutton.setVisibility(View.GONE);
                    sendButton.setVisibility(View.VISIBLE);
                }

                try {
                    Picasso.get().load(imageuri).placeholder(R.drawable.upload).into(pick_image_loader);
                }catch (Exception e) {
                    Picasso.get().load(imageuri).placeholder(R.drawable.upload).into(pick_image_loader);
                }
            }catch (Exception e2) {
                e2.printStackTrace();
            }

        }
    }
File file;
    Uri imageuri;

    public void print_imageee(View view) {
        try {
            if (flag==0) {
                Toast.makeText(this, "please upload a image", Toast.LENGTH_SHORT).show();
            }
            else {
                ContentResolver resolver=getContentResolver();
                InputStream inputStream=resolver.openInputStream(imageuri);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                //Toast.makeText(this, imageuri+""+bitmap, Toast.LENGTH_SHORT).show();
                Bitmap bitmap1=BitmapFactory.decodeResource(getResources(),R.drawable.imagepost);

                PrintHelper photoPrinter = new PrintHelper(WifiImagePrint.this);
                photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

                photoPrinter.printBitmap(" "+getImageName(imageuri)+"-  print", bitmap);

            }


        }catch (Exception e) {
        }
    }
    public  String getImageName(Uri uri) {
        ContentResolver contentResolver=getContentResolver();
        Cursor returnCursor =
                contentResolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return  name;

    }
}