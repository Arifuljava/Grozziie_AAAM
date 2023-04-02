package com.grozziie.grozziie_aaam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class WifiImagePrint extends AppCompatActivity {
ImageView pick_image_loader;
Button sendButton;
Uri imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_image_print);
        pick_image_loader=findViewById(R.id.pick_image_loader);
        sendButton=findViewById(R.id.sendButton);
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
    }
    Bitmap bitmapImageUri;
    String displayName=null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            try {
                File path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                imageuri = data.getData();
                try {
                    ContentResolver resolver=getContentResolver();
                    InputStream inputStream=resolver.openInputStream(imageuri);
                    Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    ContentResolver contentResolver=getContentResolver();
                    Cursor cursor=contentResolver.query(imageuri,null,null,null,null);
                    if (cursor!=null && cursor.moveToFirst()) {
                        int displayIndex=cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        displayName=cursor.getString(displayIndex);
                    }
                    cursor.close();
                    Toast.makeText(this, ""+displayName, Toast.LENGTH_SHORT).show();


                    Toast.makeText(this, ""+bitmap, Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                ////file path
                try {
                    String[] projection = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(imageuri, projection, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String filePath = cursor.getString(column_index);
                    cursor.close();
                    Toast.makeText(this, ""+filePath, Toast.LENGTH_SHORT).show();
                    
                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }



                try {
                    Picasso.get().load(imageuri).into(pick_image_loader);
                }catch (Exception e) {
                    Picasso.get().load(imageuri).into(pick_image_loader);
                }
            }catch (Exception e2) {
                e2.printStackTrace();
            }

        }

    }

    String path;
    int PICK=12;
    boolean request=false;

    public void pickimageee(View view) {
      try {
          /*
          ContentResolver resolver=getContentResolver();
          InputStream inputStream=resolver.openInputStream(imageuri);
          Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
          inputStream.close();
          PrintHelper photoPrinter = new PrintHelper(WifiImagePrint.this);
          photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);

          photoPrinter.printBitmap(" "+displayName+"-  print", bitmap);
           */
          BluetoothSocket bluetoothSocket;
          BluetoothAdapter bluetoothAdapter;
          BluetoothDevice device;
          try {
              BluetoothManager bluetoothManager;
              bluetoothManager=(BluetoothManager)getSystemService(BLUETOOTH_SERVICE);
              bluetoothAdapter=bluetoothManager.getAdapter();
              if (!bluetoothAdapter.isEnabled()) {
                  Toast.makeText(WifiImagePrint.this, "Please turn on bluetooth", Toast.LENGTH_SHORT).show();
              }
              else {
                  device=bluetoothAdapter.getRemoteDevice("AB:0B:50:76:4C:06");
                  bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                  bluetoothSocket.connect();
                  if (bluetoothSocket.isConnected()) {
                      try {
                          Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.imagepost);

                          OutputStream outputStream = bluetoothSocket.getOutputStream();
                          ByteArrayOutputStream stream = new ByteArrayOutputStream();
                          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                          byte[] byteArray = stream.toByteArray();
                          outputStream.write(byteArray);

                          bluetoothSocket.close();

                      }catch (Exception e) {
                          Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  }
                  else {
                      Toast.makeText(WifiImagePrint.this, "Not Connected", Toast.LENGTH_SHORT).show();
                  }
              }


          }catch (Exception e) {
          }

      }catch (Exception e) {
      }



    }
}