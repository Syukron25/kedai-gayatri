package com.example.syukron.kedaigayatri;

import android.content.Context;
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
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class tambahstok extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<String> namaList;
    private List<String> ID;
    private SQLiteDatabase db;
    private Cursor c;
    private ArrayAdapter<String> adapter;
    private EditText nama;
    private EditText stok;
    private Button tambah;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("stok");

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambahstok);
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
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namastok = nama.getText().toString();
                String jumlahsrok = stok.getText().toString();
                if (namastok.equals("")){
                    Toast.makeText(tambahstok.this,"masukan nama",Toast.LENGTH_SHORT).show();
                } else  {
                    db.execSQL("INSERT INTO stokbarang (nama,stok) values ('"+namastok+"','"+jumlahsrok+"')");
                    lihatin();
                    Toast.makeText(tambahstok.this,"Data tersimpan",Toast.LENGTH_SHORT).show();
                    nama.setText("");
                    stok.setText("");
                    stokbarang stoktambah = new stokbarang(namastok,Integer.parseInt(jumlahsrok));
                    myRef.push().setValue(stoktambah);

                }
            }
        });

        lihatin();
    }

    private void lihatin() {
        ID.clear();
        namaList.clear();

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap<stokbarang,stokbarang> data = ( HashMap<stokbarang,stokbarang>) dataSnapshot.getValue();

                String nama =String.valueOf(data.get("nama"));
                String stok =String.valueOf(data.get("stok"));
                ID.add("  "+nama+"  jumlah stok : "+stok);

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

        listView.invalidateViews();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

    }

}
