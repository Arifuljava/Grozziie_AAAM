package com.grozziie.grozziie_aaam.print_bluetooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.bluetooth.BluetoothSocket;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.grozziie.grozziie_aaam.DeviceList;
import com.grozziie.grozziie_aaam.PrinterCommands;
import com.grozziie.grozziie_aaam.R;
import com.grozziie.grozziie_aaam.Utils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Locale;

public class BluetoothImagePrinting extends AppCompatActivity {
ImageView imageview;
    private String TAG = "Main Activity";
    EditText message;
    Button btnPrint, btnBill, btnDonate;


    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream outputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_image_printing);
        Toolbar toolbar=findViewById(R.id.profile_toolbar);
        String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
        imageview=findViewById(R.id.imageview);
        btnScan=findViewById(R.id.btnScan);
        if (defaultlanguage.toLowerCase().toString().equals("english")) {
            toolbar.setTitle("Pick Up Image");
        }
        else {
            toolbar.setTitle("拾取图像");
        }
        message = (EditText)findViewById(R.id.txtMessage);
        btnPrint = (Button)findViewById(R.id.btnPrint);
        btnBill = (Button)findViewById(R.id.btnBill);
        btnDonate = (Button)findViewById(R.id.btnDonate);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        imageview.setOnClickListener(new View.OnClickListener() {
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
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==1) {
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
                else {
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(BluetoothImagePrinting.this);

                    bottomSheetDialog.setContentView(R.layout.simpllayout);
                    btnScwan= (Button)bottomSheetDialog.findViewById(R.id.btnScwan);
                    widthhh=(EditText)bottomSheetDialog.findViewById(R.id.widthhh);
                    heighjttt=(EditText) bottomSheetDialog.findViewById(R.id.heighjttt);
                    amount_page=(EditText)bottomSheetDialog.findViewById(R.id.amount_page);
                    widthhh.addTextChangedListener(widthwatcher);
                    heighjttt.addTextChangedListener(heightwatcher);
                    widthhh.setText("180");
                    heighjttt.setText("250");
                    amount_page.setText("1");
                    btnScwan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(widthhh.getText().toString())||TextUtils.isEmpty(heighjttt.getText().toString())
                            ||TextUtils.isEmpty(amount_page.getText().toString())) {
                                Toast.makeText(BluetoothImagePrinting.this, "Enter all information", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                printDemo();
                            }

                        }
                    });



                    bottomSheetDialog.show();
                }
            }
        });


    }
    TextWatcher heightwatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            check=s.toString();
            if (TextUtils.isEmpty(check)) {
            }
            else {
                if (Integer.parseInt(check)>250) {
                    heighjttt.setError("Invalid Height Maxnimum is 250");
                    btnScwan.setVisibility(View.GONE);
                }
                else {
                    btnScwan.setVisibility(View.VISIBLE);
                }
            }
        }
    };
    Button btnScwan;
    String check;
    TextWatcher widthwatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
check=s.toString();
if (TextUtils.isEmpty(check)) {
}
else {
    if (Integer.parseInt(check)>190) {
        widthhh.setError("Invalid Width Maxnimum is 190");
        btnScwan.setVisibility(View.GONE);
    }
    else {
        btnScwan.setVisibility(View.VISIBLE);
    }
}
        }
    };
    EditText widthhh,heighjttt,amount_page;
    int flag=1;
    Button btnScan;
    Uri imageUri;
    Bitmap imageBitmap;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==102 && resultCode==RESULT_OK && data.getData()!=null )
        {
            try {
                imageUri=data.getData();
                Picasso.get().load(imageUri).into(imageview);
                Canvas canvas=new Canvas();
                flag=2;
                handleGalleryIntent(data);
                btnScan.setText("Go to print Page");
                ///Toast.makeText(this, ""+imageBitmap, Toast.LENGTH_SHORT).show();


            }catch (Exception e)
            {

            }
        }
        else {
            try {
                btsocket = DeviceList.getSocket();
                if(btsocket != null){
                    printText(message.getText().toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
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
        //Toast.makeText(this, ""+newDrawableUri, Toast.LENGTH_SHORT).show();
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(newDrawableUri);
        sendBroadcast(mediaScanIntent);
    }
    Uri newDrawableUri;
    protected void printDemo() {
        if(btsocket == null){
            Intent BTIntent = new Intent(getApplicationContext(), DeviceList.class);
            this.startActivityForResult(BTIntent, DeviceList.REQUEST_CONNECT_BT);
        }
        else{
            int n=Integer.parseInt(amount_page.getText().toString());
            for (int i=0;i<n;i++) {
                OutputStream opstream = null;
                try {
                    opstream = btsocket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
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
                    printCustom(message.getText().toString(),0,0);
                    printPhoto(newDrawableUri);
                    printNewLine();
                    printText("     >>>>   Thank you  <<<<     "); // total 32 char in a single line
                    //resetPrint(); //reset printer
                    printUnicode();
                    printNewLine();
                    printNewLine();
                    printNewLine();
                    printNewLine();
                    printNewLine();
                    printNewLine();
                    printNewLine();



                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
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
                    outputStream.write(com.grozziie.grozziie_aaam.PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(com.grozziie.grozziie_aaam.PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(com.grozziie.grozziie_aaam.PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(com.grozziie.grozziie_aaam.PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print photo
    public void printPhoto(Uri img) {
        try {
            String im=""+img;
            String imagePathString = img.getPath();
            File imageFile = new File(imagePathString);
            Bitmap bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

           /// Toast.makeText(this, ""+bmp, Toast.LENGTH_SHORT).show();
            int newHeight = Integer.parseInt(heighjttt.getText().toString());
            int newWidth = Integer.parseInt(widthhh.getText().toString());

// Create a new bitmap with the new size
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
            if(resizedBitmap!=null){
                byte[] command = Utils.decodeBitmap(resizedBitmap);
                outputStream.write(com.grozziie.grozziie_aaam.PrinterCommands.ESC_ALIGN_CENTER);
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
            outputStream.write(com.grozziie.grozziie_aaam.PrinterCommands.ESC_ALIGN_CENTER);
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
            outputStream.write(com.grozziie.grozziie_aaam.PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void resetPrint() {
        try{
            outputStream.write(com.grozziie.grozziie_aaam.PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            outputStream.write(com.grozziie.grozziie_aaam.PrinterCommands.FS_FONT_ALIGN);
            outputStream.write(com.grozziie.grozziie_aaam.PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(com.grozziie.grozziie_aaam.PrinterCommands.ESC_CANCEL_BOLD);
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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


    private String leftRightAlign(String str1, String str2) {
        String ans = str1 +str2;
        if(ans.length() <31){
            int n = (31 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }


    private String[] getDateTime() {
        final Calendar c = Calendar.getInstance();
        String dateTime [] = new String[2];
        dateTime[0] = c.get(Calendar.DAY_OF_MONTH) +"/"+ c.get(Calendar.MONTH) +"/"+ c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) +":"+ c.get(Calendar.MINUTE);
        return dateTime;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if(btsocket!= null){
                outputStream.close();
                btsocket.close();
                btsocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}