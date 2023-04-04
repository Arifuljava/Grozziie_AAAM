package com.grozziie.grozziie_aaam.wifi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.grozziie.grozziie_aaam.R;

import java.util.Locale;

public class Wifi_CreatePdfActivity extends AppCompatActivity {
EditText witepdf_text;
Button create_pdfff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi__create_pdf);
        witepdf_text=findViewById(R.id.witepdf_text);
        create_pdfff=findViewById(R.id.create_pdfff);

        Toolbar toolbar=findViewById(R.id.profile_toolbar);
        String  defaultlanguage= Locale.getDefault().getDisplayLanguage();





        if (defaultlanguage.toLowerCase().toString().equals("english")) {
            witepdf_text.setHint("Enter details about  pdf");
            toolbar.setTitle("Printing Objects");
            create_pdfff.setText("Create");
        }
        else {
            witepdf_text.setText("输入有关 pdf 的详细信息");
             toolbar.setTitle("打印对象");
            create_pdfff.setText("创造");

        }

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_myarrow);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_myarrow);
        getSupportActionBar().setElevation(10.0f);
        getSupportActionBar().setElevation(10.0f);
        create_pdfff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pdfff=witepdf_text.getText().toString();
                if (TextUtils.isEmpty(pdfff)) {
                    String  defaultlanguage= Locale.getDefault().getDisplayLanguage();





                    if (defaultlanguage.toLowerCase().toString().equals("english")) {
                        Toast.makeText(Wifi_CreatePdfActivity.this, "Enter information then you can create pdf", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Wifi_CreatePdfActivity.this, "输入信息然后你可以创建pdf", Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                }
            }
        });
    }
}