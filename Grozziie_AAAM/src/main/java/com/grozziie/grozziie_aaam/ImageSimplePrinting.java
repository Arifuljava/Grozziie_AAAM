package com.grozziie.grozziie_aaam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.grozziie.grozziie_aaam.print_bluetooth.BitmapHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class ImageSimplePrinting extends AppCompatActivity {
    ImageView imageviewselectimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_simple_printing);
        imageviewselectimage=findViewById(R.id.imageviewselectimage);
        imagepick=findViewById(R.id.imagepick);
        printImage=findViewById(R.id.printImage);
        imageviewselectimage.setOnClickListener(new View.OnClickListener() {
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
permissionDeniedResponse.isPermanentlyDenied();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        ///image pick
        imagepick.setOnClickListener(new View.OnClickListener() {
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
                                permissionDeniedResponse.isPermanentlyDenied();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        ////image printt

        printImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (flag==1) {
                   Toast.makeText(ImageSimplePrinting.this, "Please pick up image", Toast.LENGTH_SHORT).show();
               }
               else {
                  try {
                      Print();
                     /*
                      BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                      BluetoothDevice printerDevice = bluetoothAdapter.getRemoteDevice("28:56:A5:69:EF:E8");

                      BluetoothSocket printerSocket = printerDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                      printerSocket.connect();

                      Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imagepost);

                      PrintHelper printHelper = new PrintHelper(ImageSimplePrinting.this);
                      printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                      printHelper.printBitmap("My Image", bitmap);

                      printerSocket.close();
                      */
                  }catch (Exception e) {
                      Toast.makeText(ImageSimplePrinting.this, "error "+e.getMessage(), Toast.LENGTH_SHORT).show();
                  }
               }
            }
        });
    }
    public  void Print() {
       try {
           BitmapHelper bitmapHelper=new BitmapHelper();

           Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imagepost);
          /// Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imagepost);
           Bitmap monochromeBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
           Canvas canvas = new Canvas(monochromeBitmap);
           Paint paint = new Paint();
           ColorMatrix colorMatrix = new ColorMatrix();
           colorMatrix.setSaturation(0);
           ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
           paint.setColorFilter(colorFilter);
           canvas.drawBitmap(bitmap, 0, 0, paint);
           int width = monochromeBitmap.getWidth();
           int height = monochromeBitmap.getHeight();
           int[] pixels = new int[width * height];
           monochromeBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
           byte[] imageBytes = new byte[(width + 7) / 8 * height];
           int index = 0;
           for (int y = 0; y < height; y++) {
               int byteValue = 0;
               for (int x = 0; x < width; x++) {
                   int pixel = pixels[y * width + x];
                   if (Color.red(pixel) < 128) {
                       byteValue |= 1 << (7 - x % 8);
                   }
                   if (x % 8 == 7) {
                       imageBytes[index++] = (byte) byteValue;
                       byteValue = 0;
                   }
               }
               if (width % 8 != 0) {
                   imageBytes[index++] = (byte) byteValue;
               }
           }
           final Bitmap bitmap5= BitmapFactory.decodeResource(getResources(),R.drawable.imagepost);
           final byte[] bitmapGetByte=BitmapToRGBbyte(bitmap5);
           BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
           BluetoothDevice printerDevice = bluetoothAdapter.getRemoteDevice("AB:0B:50:76:4C:06");
           BluetoothSocket printerSocket = printerDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
           printerSocket.connect();
           OutputStream outputStream = printerSocket.getOutputStream();
           outputStream.write(27);
           outputStream.write('v');
           outputStream.write(48);
           outputStream.write((width + 7) / 8);
           outputStream.write(height);
           outputStream.write(bitmapGetByte);
           outputStream.write(10);
           outputStream.write('v');
           outputStream.write('v');

           outputStream.flush();
           printerSocket.close();
       }catch (Exception e) {
           Toast.makeText(ImageSimplePrinting.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
       }



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

    int flag=1;
    Button imagepick,printImage;
    Uri  imageUri;

    Bitmap bitmaphh;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==102&& resultCode==RESULT_OK&& data.getData()!=null)
        {
            try {
                imageUri=data.getData();
                Picasso.get().load(imageUri).into(imageviewselectimage);
                ContentResolver contentResolver=getContentResolver();
                InputStream inputStream=contentResolver.openInputStream(imageUri);
                bitmaphh= BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                BitmapHelper bitmapHelper=new BitmapHelper();
                bitmapHelper.decodeBitmap(bitmaphh);
                Toast.makeText(this, ""+bitmapHelper.decodeBitmap(bitmaphh), Toast.LENGTH_SHORT).show();
                flag=2;

            }catch (Exception e) {
                e.printStackTrace();
                e.printStackTrace();
            }
        }
    }

}