package com.example.syukron.kedaigayatri;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class edituser extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<String> namaList;
    private List<String> ID;
    private SQLiteDatabase db;
    private Cursor c;
    private ArrayAdapter<String> adapter;
    private String[] arraySpinner;

    private EditText nama;
    private EditText stok;
    private Button tambah;
    private Spinner jabatan;
    private  String jabatanbaru;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituser);

        db=openOrCreateDatabase("gayatri", Context.MODE_PRIVATE, null);

        ID = new ArrayList<String>();
        ID.add(" ");
        adapter = new ArrayAdapter<String>
                (this, R.layout.activity_listview, ID);


        namaList = new ArrayList<String>();
        namaList.add(" ");

        listView = (ListView) findViewById(R.id.stoklist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        nama = (EditText) findViewById(R.id.namastok);
        stok = (EditText) findViewById(R.id.jumlahstok);
        tambah = (Button) findViewById(R.id.tambahstok);
        jabatan = (Spinner) findViewById(R.id.jabatan);
        this.arraySpinner = new String[] {
"kasir","admin"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        jabatan.setAdapter(adapter);
        jabatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
jabatanbaru = jabatan.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
jabatanbaru = "kasir";
            }
        });


        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namastok = nama.getText().toString();
                String jumlahsrok = stok.getText().toString();
                if (namastok.equals("")){
                    Toast.makeText(edituser.this,"masukan nama",Toast.LENGTH_SHORT).show();
                } else  {
                    db.execSQL("INSERT INTO user (nama,pass,jabatan) values ('"+namastok+"','"+jumlahsrok+"','"+jabatanbaru+"')");
                    lihatin();
                    Toast.makeText(edituser.this,"Data tersimpan",Toast.LENGTH_SHORT).show();
                    nama.setText("");
                    stok.setText("");

                }
            }
        });

        lihatin();
    }

    private void lihatin() {
        ID.clear();
        namaList.clear();
        c = db.rawQuery("SELECT id, nama, jabatan from user ", null);
        if (c.moveToFirst()) {
            do {
                String nama = c.getString(1);
                String jabatan = c.getString(2);
                int id = c.getInt(0);
                String teks = String.format("%-10s%-13s%-3s",id+".",nama,jabatan);
                ID.add(teks);
                namaList.add(nama);
            } while (c.moveToNext());
        } else {                            Toast.makeText(edituser.this,"null", Toast.LENGTH_SHORT).show();
        }
        listView.invalidateViews();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Hapus transaksi?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String namaterpilih = namaList.get(position);
                        String sqldelete = "DELETE FROM user WHERE nama = '"+namaterpilih+"'; ";
                        Toast.makeText(edituser.this,namaterpilih, Toast.LENGTH_SHORT).show();
                        db.execSQL(sqldelete);
                        lihatin();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}
