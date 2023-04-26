package com.example.contactsview;

import androidx.appcompat.app.AppCompatActivity;

public class refreshclass {
    public static void refreshApp(AppCompatActivity appCompatActivity){
        appCompatActivity.recreate();
    }

    public static void finishApp(AppCompatActivity appCompatActivity){
        appCompatActivity.finish();
    }
}
