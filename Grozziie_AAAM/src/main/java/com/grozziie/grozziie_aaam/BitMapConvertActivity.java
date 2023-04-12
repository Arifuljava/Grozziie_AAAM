package com.grozziie.grozziie_aaam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.ygoular.bitmapconverter.BitmapConverter;
import com.ygoular.bitmapconverter.BitmapFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

import static android.content.ContentValues.TAG;

public class BitMapConvertActivity extends AppCompatActivity {
    Uri imageuri;
    int flag = 0;
    BluetoothSocket m5ocket;
    BluetoothManager mBluetoothManager;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice device;
    ImageView imageposit;

    Button printimageA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bit_map_convert);
        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        toolbar.setTitle("CPCL Printer");
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        ///imageprinting
        imageposit = findViewById(R.id.imageposit);
        printimageA=findViewById(R.id.printimageA);
        printimageA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String BlueMac="AD:24:92:48:DB:7F";
                mBluetoothManager= (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
                mBluetoothAdapter=mBluetoothManager.getAdapter();
                final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BlueMac);
                ///Toasty.info(getApplicationContext(),"Please active bluetooth"+mBluetoothAdapter.isEnabled(),Toasty.LENGTH_SHORT,true).show();
                if(!mBluetoothAdapter.isEnabled()) {
                    Toasty.info(getApplicationContext(),"Please active bluetooth",Toasty.LENGTH_SHORT,true).show();
                    android.app.AlertDialog.Builder mybuilder=new android.app.AlertDialog.Builder(BitMapConvertActivity.this);
                    mybuilder.setTitle("Confirmation")
                            .setMessage("Do you want to active bluetooth");
                    mybuilder.setPositiveButton("Not Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setNegativeButton("Right Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mBluetoothAdapter.enable();
                            Toasty.info(getApplicationContext(),"Bluetooth is active now.",Toasty.LENGTH_SHORT,true).show();
                        }
                    }).create().show();

                    return;
                }
                else {
                    printImage();
                }


            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Device_CategoryActivity.class));
    }

    @Override
    public boolean onNavigateUp() {
        startActivity(new Intent(getApplicationContext(),Device_CategoryActivity.class));
        return true;
    }
    /*========================================================================================================
    ==================================PRINT SECTION===========================================================
     */
    int bitmapHeight=1080;
    OutputStream os = null;
    private void printImage()
    {
        String imagePathString =newDrawableUri.getPath();
        File imageFile = new File(imagePathString);
       /// Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
       // Toast.makeText(this, ""+bitmap, Toast.LENGTH_SHORT).show();
      final Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.imagepost);

        Toast.makeText(this, ""+bitmap, Toast.LENGTH_SHORT).show();
        final byte[] bitmapGetByte=BitmapToRGBbyte(bitmap);
        String BlueMac="AD:24:92:48:DB:7F";
        mBluetoothManager= (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        mBluetoothAdapter=mBluetoothManager.getAdapter();
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(BlueMac);
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /// Toast.makeText(AssenTaskDounwActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    m5ocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    m5ocket.connect();

                    os = m5ocket.getOutputStream();

                    if(bitmap.getHeight()>bitmapHeight)
                    {
                        bitmapHeight=1080;
                    }
                    else
                    {
                        bitmapHeight=bitmap.getHeight();
                    }
                    Random random=new Random();
                    int sendingnumber=random.nextInt(10);
                    int mimisecond=sendingnumber*1000;


                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            // write your code here
                            countDownTimer =new CountDownTimer(10000,1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    KProgressHUD kProgressHUD;
                                    double seconddd=millisUntilFinished/1000;
                                    printimageA.setText("Sending Data : "+seconddd+" S");



                                }

                                @Override
                                public void onFinish() {
                                    try {
                                        String t_line1 = "! 0 200 200 "+bitmapHeight+" 1 \r\n";//bitmap.getHeight()
                                        String t_line2 = "pw "+384+"\r\n";
                                        String t_line3 = "DENSITY 12\r\n";
                                        String t_line4 = "SPEED 3\r\n";
                                        String t_line5 = "CG "+384/8+" "+bitmapHeight+" 0 0 ";
                                        String t_line6 ="PR 0\r\n";
                                        String t_line7= "FORM\r\n";
                                        String t_line8 = "PRINT\r\n";
                                        String t_line9 = "\r\n";
                                        os.write(t_line1.getBytes());
                                        os.write(t_line2.getBytes());
                                        os.write(t_line3.getBytes());
                                        os .write(t_line4.getBytes());
                                        os .write(t_line5.getBytes());
                                        os .write(t_line4.getBytes());
                                        os.write(bitmapGetByte);
                                        os .write(t_line9.getBytes());
                                        os .write(t_line6.getBytes());
                                        os.write(t_line7.getBytes());
                                        os.write(t_line8.getBytes());
                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    countDownTimer1=new CountDownTimer(2000,1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            long second=  (millisUntilFinished/1000);
                                            int mysecond=Integer.parseInt(String.valueOf(second));



                                        }

                                        @Override
                                        public void onFinish() {

                                            printimageA.setText("Print Image");
                                            try {
                                                os.flush();
                                                os.flush();
                                                m5ocket.close();
                                            }catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            Toasty.success(getApplicationContext(),"Data Sending Complete",Toasty.LENGTH_SHORT,true).show();
                                            return;
                                        }
                                    }.start();
                                    countDownTimer1.start();


                                }
                            };
                            countDownTimer.start();
                        }
                    });



                } catch (IOException e) {
                    Log.e(TAG, "e");

                }
            }
        });
        thread.start();
    }
    CountDownTimer countDownTimer,countDownTimer1;
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


    public void pickimage(View view) {
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
    Uri bitmapUri;
    Bitmap mainimageBitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            try {
                File path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                bitmapUri = data.getData();
                handleGalleryIntent(data);


                try {
                    Picasso.get().load(bitmapUri).into(imageposit);
                }catch (Exception e) {
                    Picasso.get().load(bitmapUri).into(imageposit);
                }
            }catch (Exception e2) {
                e2.printStackTrace();
            }

        }

    }

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
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(newDrawableUri);
        sendBroadcast(mediaScanIntent);
    }
    Uri newDrawableUri;
    int PICK=12;
    boolean request=false;
}