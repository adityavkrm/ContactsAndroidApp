package com.example.contactsview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    ImageView img1;
    TextView txt1, txt2;
    ImageButton callbtn, smsbtn, sharebutton;
    MyDBHelper dbHelper;

    RecyclerContactAdapter adapter;
    ArrayList<ContactModel> arrContact;
    Button backbutton, deletebutton, editbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        img1 = findViewById(R.id.personimage);
        txt1 = findViewById(R.id.personname);
        txt2 = findViewById(R.id.personnumber);
        callbtn = findViewById(R.id.callbutton);
        smsbtn = findViewById(R.id.smsbutton);
        backbutton = findViewById(R.id.backbutton);
        sharebutton = findViewById(R.id.sharebutton);
        txt1.setText(getIntent().getStringExtra("name"));
        txt2.setText(getIntent().getStringExtra("number"));


        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhoneNumber();

                }
        });


        smsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txt2.getText().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", num, null));
                startActivity(intent);
            }
        });

        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setType("text/plain");
                it.putExtra(Intent.EXTRA_SUBJECT, getIntent().getStringExtra("name"));
                it.putExtra(Intent.EXTRA_TEXT, getIntent().getStringExtra("number"));
                startActivity(Intent.createChooser(it, "Share via"));
            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhoneNumber();
            }
        }
    }
    private void callPhoneNumber() {

        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity2.this, new String[]{android.Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + getIntent().getStringExtra("number")));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + getIntent().getStringExtra("number")));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}




