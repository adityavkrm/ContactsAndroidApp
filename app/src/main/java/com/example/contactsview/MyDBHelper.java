package com.example.contactsview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ContactsDB";
    private static final int DATABASE_VERSION = 1;
    public final String TABLE_CONTACT = "Contacts";
//    public final String KEY_ID = "id";
    public final String KEY_NAME = "Name";
    public final String KEY_PHONE_NO = "phone_no";



    public MyDBHelper(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_CONTACT+"( "+ KEY_NAME+" TEXT ,"+KEY_PHONE_NO+" INTEGER PRIMARY KEY "+ ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if schema will be changed , the existing table will be dropped and new table will be created
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CONTACT);
        onCreate(db);

    }


    // Insertion

    public void addContact(String name , String phone_no){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME,name);
        values.put(KEY_PHONE_NO, phone_no);

        db.insert(TABLE_CONTACT, null, values);

    }

    public ArrayList<ContactModel> fetchContact(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_CONTACT+" ORDER BY "+KEY_NAME, null); //if we have to select data on some conditions then we use selectionArgs otherwise if we want all, use null

        ArrayList<ContactModel> arrContacts = new ArrayList<>();

        //to move cursor continously ,use while loop

            while(cursor.moveToNext()){

                ContactModel model = new ContactModel(cursor.getString(0),cursor.getString(1));

                arrContacts.add(model);
            }
      return arrContacts;
    }


    public void updateContact(ContactModel contactModel){

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_NAME, contactModel.getName());
        contentValues.put(KEY_PHONE_NO, contactModel.getNumber());

        database.update(TABLE_CONTACT, contentValues, KEY_PHONE_NO +" = "+contactModel.number, null);


    }

    public void deleteContact(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACT,KEY_PHONE_NO+ " = "+id, null);
        ArrayList<ContactModel> arrContacts = fetchContact();

    }



}
