package com.example.syukron.kedaigayatri;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class datamaster extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView listView;
    private ListView stoklist;
    private ListView stoklistok;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter2;
    private ArrayAdapter<String> adapter3;
    private List<String> ID;
    private List<String> stoklistlist;
    private List<String> stoklistoklist;
    private SQLiteDatabase db;
    private Cursor c;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("stok");
    private TextView kode;
    private TextView lastkode;
    private EditText nama;
    private EditText harga;

    private Button simpan;
    private Button tambah;
    private Button hapus;
    private String kodeterpilih;
    private Button resetapplikasi;
    private int kodebaru;

    private View dialogView;
    private EditText input;
    private String jabatanLog;
    private String namaLog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datamaster);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        db = openOrCreateDatabase("gayatri", Context.MODE_PRIVATE, null);
        ID = new ArrayList<String>();
        ID.add(" ");
        jabatanLog = getIntent().getStringExtra("EXTRA_SESSION_JABATAN");
        namaLog = getIntent().getStringExtra("EXTRA_SESSION_NAMA");
        stoklistlist = new ArrayList<String>();
        stoklistlist.add(" ");

        stoklistoklist = new ArrayList<String>();
        stoklistoklist.add(" ");

        adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, ID);

        adapter2 = new ArrayAdapter<String>(this,
                R.layout.activity_listview, stoklistlist);

        adapter3 = new ArrayAdapter<String>(this,
                R.layout.activity_listview, stoklistoklist);


        listView = (ListView) findViewById(R.id.masterlist);
        stoklist = (ListView) findViewById(R.id.stoklist);
        stoklistok = (ListView) findViewById(R.id.stoklistok);
        listView.setAdapter(adapter);
        stoklist.setAdapter(adapter2);
        stoklistok.setAdapter(adapter3);
        listView.setOnItemClickListener(this);
        stoklistok.setOnItemClickListener(this);
        stoklist.setOnItemClickListener(this);
        lihatin();
        lihatinstok();

        hapus = (Button) findViewById(R.id.hapus);
        kode = (TextView) findViewById(R.id.kode);
        nama = (EditText) findViewById(R.id.nama);
        harga = (EditText) findViewById(R.id.harga);
        simpan = (Button) findViewById(R.id.simpan);
        tambah = (Button) findViewById(R.id.tambah);
        resetapplikasi = (Button) findViewById(R.id.reset);
        lastkode = (TextView) findViewById(R.id.lastkode);

        simpan.setOnClickListener(this);
        tambah.setOnClickListener(this);
        hapus.setOnClickListener(this);
        resetapplikasi.setOnClickListener(this);


        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.bayar, null);
        input = (EditText) dialogView.findViewById(R.id.nilaibayar);


        tambah();
    }


    private void lihatin() {
        ID.clear();
        c = db.rawQuery("SELECT * from datamaster ", null);
        if (c.moveToFirst()) {
            do {
                String kode = c.getString(1);
                String nama = c.getString(2);
                int harga = c.getInt(3);
                ID.add("\t " + kode + " \t" + "\t" + "\t " + nama + "\t" + "\t" + "\t" + harga);
            } while (c.moveToNext());
        }
        listView.invalidateViews();
    }

    private void lihatinstok() {
        stoklistlist.clear();
        c = db.rawQuery("SELECT nama from stokbarang ", null);
        if (c.moveToFirst()) {
            do {
                String nama = c.getString(0);
                stoklistlist.add(nama);
            } while (c.moveToNext());
        }
        stoklist.invalidateViews();
    }

    private void lihatinstokok() {
        stoklistoklist.clear();
        c = db.rawQuery("SELECT kode, namastok, jumlah from pengurangan where kode =' " + kodeterpilih + "'", null);
        if (c.moveToFirst()) {
            do {
                int kode = c.getInt(0);
                String nama = c.getString(1);
                int jumlah = c.getInt(2);
                stoklistoklist.add("\t " + kode + "\t " + nama + "\t " + jumlah);
            } while (c.moveToNext());
        }
        stoklistok.invalidateViews();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (parent.getId() == R.id.masterlist) {

            String selected = (String) (listView.getItemAtPosition(position));
            String[] separated = selected.split(" ");
            kodeterpilih = separated[1];
            c = db.rawQuery("select * from datamaster where kode=" + kodeterpilih + "", null);
            c.moveToFirst();

            kode.setText(String.valueOf(c.getInt(1)));
            nama.setText(String.valueOf(c.getString(2)));
            harga.setText(String.valueOf(c.getInt(3)));

            lihatinstokok();
        }

        if (parent.getId() == R.id.stoklist) {
            String selected = (String) (stoklist.getItemAtPosition(position));
            popupmenu(selected);
        }

        if (parent.getId() == R.id.stoklistok) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Hapus transaksi?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            String kodepengurangan = (String) (stoklistok.getItemAtPosition(position));
                            String[] separated = kodepengurangan.split(" ");
                            String namastok = separated[2].substring(0, separated[2].length() - 1);
                            String sqldelete = "DELETE FROM pengurangan WHERE kode = " + separated[1] + " AND namastok = '" + namastok + "' ";
                            Toast.makeText(datamaster.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
                            db.execSQL(sqldelete);
                            lihatinstokok();
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


    protected void popupmenu(final String selected) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("tambahkan " + selected);
        alert.setMessage("Jumlah Stok Berkurang");
        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.bayar, null);
        input = (EditText) dialogView.findViewById(R.id.nilaibayar);
        alert.setView(dialogView);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                db.execSQL("insert into pengurangan (kode, namastok, jumlah) VALUES ('" + kode.getText() + "', '" + selected + "' , '" + input.getText() + "')");
                lihatinstokok();

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }


    @Override
    public void onClick(View v) {

        if (v == simpan) {
            db.execSQL("update datamaster set kode ='" + kode.getText() + "', nama='" + nama.getText() + "', harga='" + harga.getText() + "' where kode = " + kodeterpilih + "");
            lihatin();
            tambah.setVisibility(View.VISIBLE);
            Toast.makeText(datamaster.this, "Data Telah Tersimpan", Toast.LENGTH_SHORT).show();
            lihatin();
        }

        if (v == tambah) {
            tambah();
            db.execSQL("INSERT INTO DATAMASTER (kode, nama, harga) values ('" + kodebaru + "','','') ");
            kode.setText(String.valueOf(kodebaru));
            kodeterpilih = String.valueOf(kodebaru);
            nama.setText("");
            harga.setText("");
            tambah.setVisibility(View.GONE);
        }

        if (v == hapus) {
            nama.setText("");
            harga.setText("");
        }

        if (v == resetapplikasi) {
            if (namaLog.equals("Sukron")) {
                reset();
            }
        } else {
            Toast.makeText(datamaster.this, "Only Super Admin can reset", Toast.LENGTH_SHORT).show();
        }
    }

    private void tambah() {
        c = db.rawQuery("select MAX(kode) from datamaster ", null);
        c.moveToFirst();
        kodebaru = c.getInt(0) + 1;
        lastkode.setText(String.valueOf(kodebaru));
    }


    private void reset() {
        db.execSQL("CREATE TABLE IF NOT EXISTS datamaster(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, kode INTEGER, nama VARCHAR, harga INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS pengurangan(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, kode INTEGER, namastok VARCHAR, jumlah INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS stokbarang(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nama VARCHAR, stok INTEGER);");
        db.execSQL("DROP TABLE datamaster");
        db.execSQL("DROP TABLE pengurangan");
        db.execSQL("DROP TABLE stokbarang");
        db.execSQL("CREATE TABLE IF NOT EXISTS datamaster(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, kode INTEGER, nama VARCHAR, harga INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS pengurangan(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, kode INTEGER, namastok VARCHAR, jumlah INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS stokbarang(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, nama VARCHAR, stok INTEGER);");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('1', 'Paket Ikan','25000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('2', 'Paket Ayam','20000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('3', 'Soto Ayam','17000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('4', 'Gado - Gado','17000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('5', 'Sayur Asem','8000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('6', 'Nasi Goreng','17000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('7', 'Mie Goreng','17000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('8', 'kwetiaw Goreng','17000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('9', 'Sambal Jontor','30000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('10', 'Nasi Tumpeng','500000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('11', 'Nasi Kotak','20000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('12', 'Nasi Liwet','7000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('13', 'Kerupuk','3000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('14', 'Emping','5000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('15', 'Tahu','1000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('16', 'Tempe','1000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('17', 'Hati & Ampela','4000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('18', 'Tahu Jeletot','3000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('19', 'Tempe Goreng Tepung','2000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('20', 'Teh Tawar','2500');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('21', 'Teh Manis','3500');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('22', 'Kopi Hitam','3500');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('23', 'Kopi Susu','4000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('24', 'capuccino','4000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('25', 'Coffie Jelly','6000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('26', 'Aqua 600ml','6000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('27', 'Aqua 330ml','4000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('28', 'Lemon squase','10000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('29', 'lemon s Anggur','10000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('30', 'lemon S jeruk','10000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('31', 'lemon S Mangga','10000');");
        db.execSQL("INSERT INTO datamaster (kode,nama,harga) VALUES('32', 'Milo','7000');");


        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('1','nasi liwet','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('1','ikan','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('1','tahu','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('1','tempe','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('1','sambel jontor','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('1','lalapan','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('1','bawang goreng','1');");

        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('2','nasi liwet','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('2','ayam','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('2','tahu','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('2','tempe','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('2','sambel jontor','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('2','lalapan','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('2','bawang goreng','1');");

        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('3','bumbu','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('3','ayam suir','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('3','soun','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('3','kol','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('3','toge','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('3','daun bawang','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('3','saledri','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('3','bawang goreng','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('3','telur','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('3','jeruk nipis','1');");

        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('4','bumbu kacang','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('4','bawang goreng','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('4','kangkung','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('4','toge','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('4','ketimun','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('4','kentang','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('4','labu siam','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('4','lontong','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('4','telur','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('4','jagung','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('4','bawang goreng','1');");

        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('5','bumbu','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('5','asem','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('5','sayuran asem','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('5','jagung','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('5','bawang goreng','1');");

        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('6','nasi liwet','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('6','bumbu','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('6','telur','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('6','ayam suir','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('6','daun caisin','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('6','bakso','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('6','bawang goreng','1');");

        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('7','mie telur','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('7','bumbu','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('7','telur','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('7','ayam suir','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('7','daun caisin','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('7','bakso','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('7','bawang goreng','1');");

        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('8','bumbu','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('8','kwetiau','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('8','daun caisin','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('8','ayam suir','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('8','telur','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('8','bakso','1');");
        db.execSQL("INSERT INTO pengurangan (kode, namastok, jumlah) values ('8','bawang goreng','1');");

        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('ayam','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('ayam suir','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('asem','0');");

        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('bumbu kacang','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('bumbu','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('bawang goreng','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('bakso','0');");

        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('daun caisin','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('daun bawang','0');");

        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('ikan','0');");

        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('jagung','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('jeruk nipis','0');");

        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('kangkung','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('ketimun','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('kentang','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('kwetiau','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('kol','0');");

        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('labu siam','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('lontong','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('mie telur','0');");

        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('nasi liwet','0');");

        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('sambel jontor','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('sayuran asem','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('soun','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('saledri','0');");

        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('tahu','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('tempe','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('telur','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('toge','0');");

        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('alpukat','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('guava','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('tomejer','0');");
        db.execSQL("INSERT INTO stokbarang (nama,stok) values ('sanas','0');");


        myRef.removeValue();

        c = db.rawQuery("SELECT nama,stok from stokbarang", null);
        if (c.moveToFirst()) {
            do {
                String nama = c.getString(0);
                int stok = c.getInt(1);
                stokbarang jual = new stokbarang(nama, stok);
                myRef.push().setValue(jual);
            }  while (c.moveToNext());
        }



    }
}