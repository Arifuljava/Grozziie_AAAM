package com.grozziie.grozziie_aaam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.UUID;

public class DefaultPrintingSection extends AppCompatActivity {
    ImageView imageviewselectimage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_printing_section);
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
                if (flag==2) {
                    Toast.makeText(DefaultPrintingSection.this, "Please pick up image", Toast.LENGTH_SHORT).show();
                }
                else {


                    try {
                        fileimage();
                     /*
                      BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                      BluetoothDevice printerDevice = bluetoothAdapter.getRemoteDevice("AB:0B:50:76:4C:06");

                      BluetoothSocket printerSocket = printerDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                      printerSocket.connect();

                      Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.imagepost);

                      PrintHelper printHelper = new PrintHelper(ImageSimplePrinting.this);
                      printHelper.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                      printHelper.printBitmap("My Image", bitmap);

                      printerSocket.close();
                      */
                    }catch (Exception e) {
                        Toast.makeText(DefaultPrintingSection.this, "error "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(DefaultPrintingSection.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
    Uri imageUri;

    Bitmap bitmaphh;
    private void handleGalleryIntent(Intent data) {
        // Get the URI of the selected image
        Uri selectedImage = data.getData();
        // Get a Bitmap object from the selected image
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create a new Drawable object from the Bitmap
        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
        // Get the resources object
        Resources resources = getResources();
        // Get the name of the new drawable resource
        String resourceName = "new_image_image";
        // Get the identifier of the drawable resource
        int resourceId = resources.getIdentifier(resourceName, "drawable", getPackageName());
        // Save the drawable to a file in the drawable directory
        try {
            FileOutputStream outputStream = openFileOutput(resourceName + ".png", Context.MODE_PRIVATE);

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // Add the new drawable resource to the drawable directory
        File newDrawableFile = new File(getFilesDir(), resourceName + ".png");

         newDrawableUri = Uri.fromFile(newDrawableFile);
        Toast.makeText(this, ""+newDrawableUri, Toast.LENGTH_SHORT).show();
        printImage.setText(""+newDrawableUri);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(newDrawableUri);
        sendBroadcast(mediaScanIntent);
    }
    Uri newDrawableUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==102&& resultCode==RESULT_OK&& data.getData()!=null)
        {
            try {
                handleGalleryIntent(data);
                imageUri=data.getData();
                Picasso.get().load(imageUri).into(imageviewselectimage);
              /*
                ContentResolver contentResolver=getContentResolver();
                InputStream inputStream=contentResolver.openInputStream(imageUri);
                bitmaphh= BitmapFactory.decodeStream(inputStream);
                inputStream.close();
                BitmapHelper bitmapHelper=new BitmapHelper();
                bitmapHelper.decodeBitmap(bitmaphh);
                Toast.makeText(this, ""+bitmapHelper.decodeBitmap(bitmaphh), Toast.LENGTH_SHORT).show();
                flag=2;
                //
                try {
                    Uri imageUri = data.getData();
                    InputStream inputStream1 = getContentResolver().openInputStream(imageUri);
                    OutputStream outputStream = getResources().openRawResource(R.drawable.my_image).openOutputStream();

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    inputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
               */
                Uri selectedImage = data.getData();
                Toast.makeText(this, ""+selectedImage, Toast.LENGTH_SHORT).show();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                    // Convert bitmap to drawable
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                    // Save drawable to drawable folder



                } catch (IOException e) {
                    Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }catch (Exception e) {
                e.printStackTrace();
                e.printStackTrace();
            }
        }
    }
    ///print

    public  void prinnt()
    {

        OutputStream opstream = null;
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice printerDevice = bluetoothAdapter.getRemoteDevice("AB:0B:50:76:4C:06");

            btsocket = printerDevice.createRfcommSocketToServiceRecord(UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66"));
            btsocket.connect();
            opstream = btsocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        outputStream = opstream;

        //print command
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            outputStream = btsocket.getOutputStream();

            byte[] printformat = { 0x1B, 0*21, FONT_TYPE };
            //outputStream.write(printformat);

            //print title
            printUnicode();
            //print normal text
            printCustom("message.getText().toString()",0,0);
            printPhoto(R.drawable.ic_aorroi);
            printNewLine();
            printText("     >>>>   Thank you  <<<<     "); // total 32 char in a single line
            //resetPrint(); //reset printer
            printUnicode();
            printNewLine();
            printNewLine();

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    byte FONT_TYPE;
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        msg="asss";
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }

            switch (align){
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  void fileimage() {
      try {
          BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
          BluetoothDevice printerDevice = bluetoothAdapter.getRemoteDevice("AB:0B:50:76:4C:06"); // replace printerAddress with the MAC address of your printer
          BluetoothSocket socket = printerDevice.createRfcommSocketToServiceRecord(UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")); // replace uuid with the UUID of your printer
          socket.connect();

// Load the image from the file path
          String imagePath = ""+newDrawableUri; // replace with the file path of your image
          Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

// Convert the Bitmap image to a printable format
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
          byte[] imageData = outputStream.toByteArray();

// Send the image data to the printer
          OutputStream printerOutputStream = socket.getOutputStream();
          printerOutputStream.write(imageData);

// Close the Bluetooth socket and output stream
          printerOutputStream.close();
          socket.close();
      }catch (Exception e) {
      }
    }
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            int newHeight = 250;
            int newWidth = 190;

// Create a new bitmap with the new size
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
            if(resizedBitmap!=null){
                byte[] command = Utils.decodeBitmap(resizedBitmap);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print unicode
    public void printUnicode(){
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //print new line
    private void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void resetPrint() {
        try{
            outputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            outputStream.write(PrinterCommands.FS_FONT_ALIGN);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(PrinterCommands.ESC_CANCEL_BOLD);
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            msg="MMM";
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static BluetoothSocket btsocket;
    private static OutputStream outputStream;
    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}