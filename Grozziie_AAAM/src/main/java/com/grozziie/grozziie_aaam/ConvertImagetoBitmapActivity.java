package com.grozziie.grozziie_aaam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class ConvertImagetoBitmapActivity extends AppCompatActivity {
ImageView selectImage;
Button convertimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_imageto_bitmap);
        uploadedimage=findViewById(R.id.uploadedimage);
        ///imageview take and getting

        selectImage=findViewById(R.id.selectImage);
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent,102);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                permissionDeniedResponse.getPermissionName();

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                if (permissionRequest.getName().equals("shout down")) {
                                    permissionToken.continuePermissionRequest();
                                }
                                else if (permissionRequest.getName().equals("granted")) {
                                    permissionToken.continuePermissionRequest();
                                }
                                else {
                                    permissionToken.continuePermissionRequest();
                                }
                                permissionToken.continuePermissionRequest();


                            }
                        }).check();

            }
        });
        //
        convertimage=findViewById(R.id.convertimage);
        convertimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ConvertImagetoBitmapActivity.this);
                builder.setTitle("Confirmation")
                        .setMessage("Are you want to convert this image to bitmap?")
                        .setPositiveButton("Not Now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goto_Convert(imageUri);

                    }
                }).create();
                builder.show();
            }
        });

    }

    private void goto_Convert(Uri imageUri) {
       try {
           ContentResolver contentResolver=getContentResolver();
           //ContentValues contentValues=getContentScene();
         InputStream inputStream=contentResolver.openInputStream(imageUri);
         Bitmap bitmap=BitmapFactory.decodeStream(inputStream);
         inputStream.close();
           Toast.makeText(this, ""+bitmap, Toast.LENGTH_SHORT).show();

       }catch (Exception e) {
       }

    }

    ImageView uploadedimage;
Uri imageUri;
    Bitmap imageBitmap;
    int flag=1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==102 && resultCode==RESULT_OK && data.getData()!=null )
        {
           try {
               imageUri=data.getData();
               Picasso.get().load(imageUri).into(selectImage);
               Canvas canvas=new Canvas();
               ContentResolver contentResolver=getContentResolver();
               InputStream inputStream=contentResolver.openInputStream(imageUri);
               imageBitmap= BitmapFactory.decodeStream(inputStream);
               inputStream.close();
               uploadedimage.setImageBitmap(imageBitmap);
               flag=1;
               takingorNot=2;
             ///  uploadedimage.set
               ///Toast.makeText(this, ""+imageBitmap, Toast.LENGTH_SHORT).show();


           }catch (Exception e)
           {
               Picasso.get().load(R.drawable.error).into(selectImage);
               Picasso.get().load(R.drawable.error).into(uploadedimage);
           }
        }
        else
        {

        }
    }
    int takingorNot=1;

}