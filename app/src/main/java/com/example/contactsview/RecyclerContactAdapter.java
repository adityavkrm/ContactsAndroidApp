package com.example.contactsview;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerContactAdapter extends RecyclerView.Adapter<RecyclerContactAdapter.ViewHolder> {
    Context context;
    TextView counttext;
    MyDBHelper dbHelper;
    ArrayList<ContactModel> arrContact;

    public RecyclerContactAdapter(Context context, ArrayList<ContactModel> arrContact,TextView counttext){
        this.context = context;
        this.arrContact = arrContact;
        this.counttext = counttext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final ContactModel temp = arrContact.get(position);
        holder.txtName.setText(arrContact.get(position).getName());
        holder.txtNumber.setText(arrContact.get(position).getNumber());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(),MainActivity2.class);
                intent.putExtra("name",temp.getName());
                intent.putExtra("number",temp.getNumber());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  //when you have to call the next activity from sn inner class, we set flags
                context.startActivity(intent);
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.update_delete);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText edtName = dialog.findViewById(R.id.edtName);
                EditText edtNumber = dialog.findViewById(R.id.edtNumber);
                Button updateButton = dialog.findViewById(R.id.updateButton);
                Button deleteButton = dialog.findViewById(R.id.deleteButton);

                edtName.setText((arrContact.get(position)).name);
                edtNumber.setText((arrContact.get(position)).number);


                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name ="", number="";
                        if(!edtName.getText().toString().equals("")){
                            name = edtName.getText().toString();
                        }else {
                            Toast.makeText(context,"Enter valid name !",Toast.LENGTH_LONG).show();
                        }

                        if(!edtNumber.getText().toString().equals("")){
                            number = edtNumber.getText().toString();
                        }else {
                            Toast.makeText(context,"Enter valid number !",Toast.LENGTH_LONG).show();
                        }



                        dbHelper = new MyDBHelper(context.getApplicationContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContactModel cm = new ContactModel();
                        cm.name = name;
                        cm.number = number;
                        dbHelper.updateContact(cm);
                        arrContact = dbHelper.fetchContact();
                        notifyDataSetChanged();
                        dialog.dismiss();
                        Toast.makeText(context,"Contact Updated.",Toast.LENGTH_SHORT).show();


                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("Delete Contact")  //.builder sets attributes such as .setMessage and .seTitle and AlertDialog shows this to user
                                .setMessage("Are you sure ?")
                                .setIcon(R.drawable.baseline_delete_24)
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MyDBHelper dbHelper1 = new MyDBHelper(context);
                                        String num = arrContact.get(position).number;
                                        dbHelper1.deleteContact(num);
                                        arrContact = dbHelper1.fetchContact();
                                        notifyItemRemoved(position);
                                        counttext.setText(dbHelper1.fetchContact().size()+ " Contacts");
                                        Toast.makeText(context,"Contact Deleted.",Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        builder.show();


                    }
                });

                dialog.show();
                return true;
            }


        });
   }
    @Override
    public int getItemCount() {
        return arrContact.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName , txtNumber;
      //  ImageView imgContact;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtNumber = itemView.findViewById(R.id.txtNumber);
            //imgContact = itemView.findViewById(R.id.imgContact);
        }

    }



}
