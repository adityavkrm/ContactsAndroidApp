package com.example.contactsview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyDBHelper dbHelper ;
    RecyclerView recyclerView;
    ArrayList<ContactModel> arrContact;
    FloatingActionButton btnOpenDialog;
    TextView counttext;
    SearchView searchView;

    RecyclerContactAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerContact);
        dbHelper = new MyDBHelper(MainActivity.this);


        counttext = findViewById(R.id.count);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        arrContact = dbHelper.fetchContact();


        adapter = new RecyclerContactAdapter(MainActivity.this,arrContact,counttext);
        recyclerView.setAdapter(adapter);
        counttext.setText(dbHelper.fetchContact().size()+" Contacts");

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus(); //remove cursor from the searchview


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                filterList(text);
                return true;
            }
        });

        btnOpenDialog = findViewById(R.id.OpenDialogBtn);
        btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();

                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_update);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



                EditText edtName = dialog.findViewById(R.id.edtName);
                EditText edtNumber = dialog.findViewById(R.id.edtNumber);
                Button btnAction = dialog.findViewById(R.id.btnAction);

                btnAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    String name ="", number="";
                    if(!edtName.getText().toString().equals("")){
                        name = edtName.getText().toString();
                    }else {
                        Toast.makeText(MainActivity.this,"Enter valid name !",Toast.LENGTH_LONG).show();
                    }

                    if(!edtNumber.getText().toString().equals("")){
                        number = edtNumber.getText().toString();
                    }else {
                        Toast.makeText(MainActivity.this,"Enter valid number !",Toast.LENGTH_LONG).show();
                    }

                    if(!name.equals("")) {
                        dbHelper.addContact(name, number);
                    }
                    adapter.notifyItemInserted(arrContact.size()-1);
                    dialog.dismiss();
                    adapter = new RecyclerContactAdapter(MainActivity.this, dbHelper.fetchContact(),counttext);
                    recyclerView.setAdapter(adapter);
                    int size = dbHelper.fetchContact().size();
                    counttext.setText(size+" Contacts");

                    }
                });

                dialog.show();

            }
        });


        }

    private void filterList(String text) {
        ArrayList<ContactModel> filteredList = new ArrayList<>();
        for(ContactModel item : dbHelper.fetchContact()){
            if(item.getName().toLowerCase().contains(text) | item.getName().toUpperCase().contains(text)) {
                filteredList.add(item);
            }
            adapter = new RecyclerContactAdapter(this,filteredList,counttext);
            if(filteredList.isEmpty()){
                filteredList.clear();
                recyclerView.setAdapter(adapter);
            }else{
                recyclerView.setAdapter(adapter);
            }

        }
    }

}


