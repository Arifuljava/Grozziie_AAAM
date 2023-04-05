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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.grozziie.grozziie_aaam.print_bluetooth.PrinterCommands;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.UUID;

public class WifiImagePrint extends AppCompatActivity {
ImageView pick_image_loader;
Button sendButton;
Uri imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_image_print);
        WifiGettingError wifiGettingError=new WifiGettingError(WifiImagePrint.this);
        wifiGettingError.ischeckinError("454545454");
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
    File file;
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
                    Toast.makeText(this, "ff"+getPixelsSlow(bitmap), Toast.LENGTH_SHORT).show();


                  //  Toast.makeText(this, ""+displayName, Toast.LENGTH_SHORT).show();



                    ///Toast.makeText(this, ""+bitmap, Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                 file = new File(imageuri.getPath());//create path from uri

                ////file path




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

          /*
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
           */
          String printermac="c8:9e:43:a0:09:84";

      }catch (Exception e) {
      }



    }
    int mWidth,mHeight;
    String mStatus;
    public String convertBitmap(Bitmap inputBitmap) {

        mWidth = inputBitmap.getWidth();
        mHeight = inputBitmap.getHeight();

        convertArgbToGrayscale(inputBitmap, mWidth, mHeight);
        mStatus = "ok";
        return mStatus;
    }
    private  byte[]  BitmapToRGBbyte(Bitmap bitmapOrg) {

        ArrayList<Byte> Gray_ArrayList;
        Gray_ArrayList =new ArrayList<Byte>();
        int height = 1080;
        if(bitmapOrg.getHeight()>height)
        {
            height=1080;
        }
        else
        {
            height=bitmapOrg.getHeight();
        }
        int width = 384;
        int R = 0, B = 0, G = 0;
        int pixles;
        int x = 0, y = 0;
        Byte[] Gray_Send;
        //bitSet = new BitSet();
        try {
            int k = 0;
            int Send_i = 0;
            int x_GetR;
            for (x = 0; x < height; x++) {
                k=0;
                for (y = 0; y < width; y++) {
                    pixles = bitmapOrg.getPixel(x, y);
                    R = Color.red(pixles);
                    G = Color.green(pixles);
                    B = Color.blue(pixles);
                    //setcolor
                    R = G = B = (int) ((0.299 * R) + (0.587 * G) + (0.114 * B));

                    if (R < 120) {
                        //bitSet.set(k);
                        x_GetR = 0;
                    } else {
                        x_GetR = 1;

                    }
                    ///texting cde
                    k++;
                    if (k == 1) {
                        Send_i=0;
                        Send_i = Send_i + x_GetR | 0x80;

                    } else if (k == 2) {

                        Send_i = Send_i + x_GetR | 0x40;

                    } else if (k == 3) {

                        Send_i = Send_i + x_GetR | 0x20;

                    } else if (k == 4) {

                        Send_i = Send_i + x_GetR | 0x10;

                    } else if (k == 5) {

                        Send_i = Send_i + x_GetR | 0x08;

                    } else if (k == 6) {

                        Send_i = Send_i + x_GetR | 0x04;

                    } else if (k == 7) {

                        Send_i = Send_i + x_GetR | 0x02;

                    } else if (k == 8) {

                        Send_i = Send_i + x_GetR | 0x01;

                        Gray_ArrayList.add((byte) Send_i);
                        Send_i = 0;
                        k = 0;
                        // byte b = (byte) Send_i; // replace with your byte value

                        //i control it


                    }


                    /////////////////////=====================================


                }
            }

            byte[] sss=new byte[Gray_ArrayList.size()];
            Gray_Send=new Byte[Gray_ArrayList.size()];
            Gray_ArrayList.toArray(Gray_Send);
            for(int xx=0;xx<Gray_Send.length;xx++){
                sss[xx]=Gray_Send[xx];
            }
            return  sss;
        } catch (Exception e) {

        }
        return null;

     /*
        BitmapConverter bitmapConverter = new BitmapConverter();
        byte [] bmpFile = bitmapConverter.convert(bitmapOrg, BitmapFormat.BITMAP_8_BIT_COLOR);
        return bmpFile;
      */
    }
BitSet dots;
    private void convertArgbToGrayscale(Bitmap bmpOriginal, int width,
                                        int height) {
        int pixel;
        int k = 0;
        int B = 0, G = 0, R = 0;
        dots = new BitSet();
        try {

            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    // get one pixel color
                    pixel = bmpOriginal.getPixel(y, x);

                    // retrieve color of all channels
                    R = Color.red(pixel);
                    G = Color.green(pixel);
                    B = Color.blue(pixel);
                    // take conversion up to one single value by calculating
                    // pixel intensity.
                    R = G = B = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                    // set bit into bitset, by calculating the pixel's luma
                    if (R < 55) {
                        dots.set(k);//this is the bitset that i'm printing
                    }
                    k++;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
           /// Log.e(TAG, e.toString());
        }
    }


    private void print_image(String file) {
       /*
        File fl = new File(file);
        if (fl.exists()) {
            Bitmap bmp = BitmapFactory.decodeFile(file);
            convertBitmap(bmp);
            mService.write(PrinterCommands.SET_LINE_SPACING_24);

            int offset = 0;
            while (offset < bmp.getHeight()) {
                mService.write(PrinterCommands.SELECT_BIT_IMAGE_MODE);
                for (int x = 0; x < bmp.getWidth(); ++x) {

                    for (int k = 0; k < 3; ++k) {

                        byte slice = 0;
                        for (int b = 0; b < 8; ++b) {
                            int y = (((offset / 8) + k) * 8) + b;
                            int i = (y * bmp.getWidth()) + x;
                            boolean v = false;
                            if (i < dots.length()) {
                                v = dots.get(i);
                            }
                            slice |= (byte) ((v ? 1 : 0) << (7 - b));
                        }
                        mService.write(slice);
                    }
                }
                offset += 24;
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
                mService.write(PrinterCommands.FEED_LINE);
            }
            mService.write(PrinterCommands.SET_LINE_SPACING_30);


        } else {
            Toast.makeText(this, "file doesn't exists", Toast.LENGTH_SHORT)
                    .show();
        }
        */
    }
    private int[][] getPixelsSlow(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[][] result = new int[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                result[row][col] = getRGB(image, col, row);
            }
        }

        return result;
    }

    private int getRGB(Bitmap bmpOriginal, int col, int row) {
        // get one pixel color
        int pixel = bmpOriginal.getPixel(col, row);
        // retrieve color of all channels
        int R = Color.red(pixel);
        int G = Color.green(pixel);
        int B = Color.blue(pixel);
        return Color.rgb(R, G, B);
    }
}