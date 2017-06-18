package com.example.syukron.kedaigayatri;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class login extends AppCompatActivity implements View.OnClickListener{
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("penjualan");
    private EditText username, password;
    private Button login;
    private  List<String>  listuser;
    private  List<String>  listpass;
    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    DatabaseReference stokRef = database2.getReference("stok");
    private SQLiteDatabase db;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db=openOrCreateDatabase("gayatri", Context.MODE_PRIVATE, null);
        bacadb();
        db.execSQL("CREATE TABLE IF NOT EXISTS stokbarang(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nama VARCHAR, stok INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS penjualanlap(nama VARCHAR, qty INTEGER, harga INTEGER, keterangan VARCHAR, tgl INTEGER, bln INTEGER, thn INTEGER);");
        db.execSQL("DROP TABLE penjualanlap");
        db.execSQL("DROP TABLE stokbarang");

        db.execSQL("CREATE TABLE IF NOT EXISTS stokbarang(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nama VARCHAR, stok INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS penjualanlap(nama VARCHAR, qty INTEGER, harga INTEGER, keterangan VARCHAR, tgl INTEGER, bln INTEGER, thn INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS user(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nama VARCHAR, pass VARCHAR, jabatan VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS target(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, targetrp INTEGER);");

        int i =0;

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap<penjualan,penjualan> data = ( HashMap<penjualan,penjualan>) dataSnapshot.getValue();
                String nama = String.valueOf(data.get("namabrg"));
                String qty = String.valueOf(data.get("qty"));
                String harga = String.valueOf(data.get("harga"));
                String ket = String.valueOf(data.get("ket"));
                String tgl = String.valueOf(data.get("tgl"));
                String bln = String.valueOf(data.get("bln"));
                String thn = String.valueOf(data.get("thn"));

                db.execSQL("INSERT INTO penjualanlap (nama,qty,harga, keterangan, tgl,bln,thn) VALUES('"+nama+"', '"+qty+"','"+harga+"','"+ket+"','"+tgl+"','"+bln+"','"+thn+"');");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        username = (EditText) findViewById( R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == login){
            if (username.getText().toString().equals("sukrongayatri2507"))
            {
                Intent intent = new Intent(login.this, halamanutama.class);
                intent.putExtra("EXTRA_SESSION_JABATAN", "Super Admin");
                intent.putExtra("EXTRA_SESSION_NAMA", "Sukron");
                startActivity(intent);
            }

            c = db.rawQuery("SELECT jabatan,nama from user where nama ='" + username.getText().toString() + "' and pass ='"+password.getText().toString()+"' ;", null);

            if (c.moveToFirst()) {
                String jabatanLog = c.getString(0);
                String namaLog = c.getString(1);

               Toast.makeText(login.this, "login berhasil", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(login.this, halamanutama.class);
                intent.putExtra("EXTRA_SESSION_JABATAN", jabatanLog);
                intent.putExtra("EXTRA_SESSION_NAMA", namaLog);
                startActivity(intent);



            } else {
                Toast.makeText(login.this, "login gagal", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void bacadb() {

        stokRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                HashMap<stokbarang,stokbarang> data = ( HashMap<stokbarang,stokbarang>) dataSnapshot.getValue();
                db.execSQL("INSERT INTO stokbarang (nama,stok) values ( '"+String.valueOf(data.get("nama"))+"' , '"+String.valueOf(data.get("stok"))+"') ;");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

