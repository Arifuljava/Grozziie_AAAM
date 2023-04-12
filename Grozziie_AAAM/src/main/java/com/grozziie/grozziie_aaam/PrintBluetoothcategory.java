package com.grozziie.grozziie_aaam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.grozziie.grozziie_aaam.print_bluetooth.BluetoothImagePrinting;

import java.util.Locale;

public class PrintBluetoothcategory extends AppCompatActivity {
    TextView imageprint,pdf_print,settingstext,web_print;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_bluetoothcategory);
        Toolbar toolbar=findViewById(R.id.profile_toolbar);
        String  defaultlanguage= Locale.getDefault().getDisplayLanguage();
        imageprint=findViewById(R.id.imageprint);
        pdf_print=findViewById(R.id.pdf_print);
        web_print=findViewById(R.id.web_print);
        settingstext=findViewById(R.id.settingstext);

        if (defaultlanguage.toLowerCase().toString().equals("english")) {
            toolbar.setTitle("Printing Objects");
            imageprint.setText("Image Print");
            pdf_print.setText("Document Print");
            web_print.setText("Label Print");
            settingstext.setText("Print History");
        }
        else {
            toolbar.setTitle("打印对象");
            imageprint.setText("图像打印");
            pdf_print.setText("文件打印");
            web_print.setText("标签打印");
            settingstext.setText("打印历史");
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

    @Override
    public boolean onNavigateUp() {
        startActivity(new Intent(getApplicationContext(),BluetoothList.class));
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),BluetoothList.class));
    }

    public void image_Print(View view) {
        startActivity(new Intent(getApplicationContext(), BluetoothImagePrinting.class));
    }

    public void pdf_print(View view) {
    }

    public void web_Print(View view) {
    }

    public void historyprint(View view) {
    }
}