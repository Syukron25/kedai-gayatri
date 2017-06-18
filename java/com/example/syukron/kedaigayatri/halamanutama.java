package com.example.syukron.kedaigayatri;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class halamanutama extends AppCompatActivity implements View.OnClickListener {
    public Button nota;
    public Button master;
    public Button inv;
    public Button kasir;
    private Button laporan;
    private Button pengguna;
    private SQLiteDatabase db;
    private TextView jabatan, target;
    private  String namaLog;
    private  String jabatanLog;
    private Cursor c;
    private  int penjualan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halamanutama);

        createDatabase();

        kasir = (Button) findViewById(R.id.kasir);
        nota = (Button) findViewById(R.id.nomor);
        master = (Button) findViewById(R.id.master);
        inv = (Button) findViewById(R.id.inventory);
        laporan = (Button) findViewById(R.id.laporan);
        pengguna = (Button) findViewById(R.id.penggunA);

        jabatan = (TextView) findViewById(R.id.jabatan);
        target = (TextView) findViewById(R.id.target);

        kasir.setOnClickListener(this);
        nota.setOnClickListener(this);
        master.setOnClickListener(this);
        inv.setOnClickListener(this);
        laporan.setOnClickListener(this);
        pengguna.setOnClickListener(this);

        jabatanLog = getIntent().getStringExtra("EXTRA_SESSION_JABATAN");
        namaLog = getIntent().getStringExtra("EXTRA_SESSION_NAMA");
        jabatan.setText(namaLog+" - " + jabatanLog);

        if (jabatanLog.equals("kasir")){
            laporan.setVisibility(View.GONE);
            master.setVisibility(View.GONE);
            inv.setVisibility(View.GONE);
            pengguna.setVisibility(View.GONE);
        }
        hitungpenjualansebulan();
        hitungtarget();
    }

    @Override
    public void onClick(View v) {
        if (v == kasir){
            Intent intent = new Intent(halamanutama.this, MainActivity.class);
            intent.putExtra("EXTRA_SESSION_JABATAN", jabatanLog);
            intent.putExtra("EXTRA_SESSION_NAMA", namaLog);
            startActivity(intent);
        }
        if (v == nota){
            Intent intent = new Intent(halamanutama.this, nota.class);
            intent.putExtra("EXTRA_SESSION_JABATAN", jabatanLog);
            intent.putExtra("EXTRA_SESSION_NAMA", namaLog);
            startActivity(intent);
        }
        if (v == master){
            Intent intent = new Intent(halamanutama.this, datamaster.class);
            intent.putExtra("EXTRA_SESSION_JABATAN", jabatanLog);
            intent.putExtra("EXTRA_SESSION_NAMA", namaLog);
            startActivity(intent);
        }
        if (v == inv){
            Intent intent = new Intent(halamanutama.this, stok.class);
            intent.putExtra("EXTRA_SESSION_JABATAN", jabatanLog);
            intent.putExtra("EXTRA_SESSION_NAMA", namaLog);
            startActivity(intent);
        }
        if (v == laporan){
            Intent intent = new Intent(halamanutama.this, laporan.class);
            intent.putExtra("EXTRA_SESSION_JABATAN", jabatanLog);
            intent.putExtra("EXTRA_SESSION_NAMA", namaLog);
            startActivity(intent);
        }
        if (v == pengguna){
            Intent intent = new Intent(halamanutama.this, edituser.class);
            intent.putExtra("EXTRA_SESSION_JABATAN", jabatanLog);
            intent.putExtra("EXTRA_SESSION_NAMA", namaLog);
            startActivity(intent);
        }

    }



    protected void createDatabase(){
        db=openOrCreateDatabase("gayatri", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS penjualan(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, kode INTEGER,qty INTEGER, idnota INTEGER, ket INTEGER, tgl INTEGER, bln INTEGER, thn INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS datamaster(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, kode INTEGER, nama VARCHAR, harga INTEGER);");
        db.execSQL("CREATE TABLE IF NOT EXISTS pengurangan(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, kode INTEGER, namastok VARCHAR, jumlah INTEGER);");
    }


    private void hitungpenjualansebulan(){
        Date date = new Date();
        String tahun = new SimpleDateFormat("yyyy").format(date);
        String bulan = new SimpleDateFormat("MM").format(date);
        c = db.rawQuery("SELECT sum (penjualan.qty * datamaster.harga) FROM penjualan INNER JOIN datamaster ON penjualan.kode = datamaster.kode WHERE penjualan.bln ="+ bulan +" and penjualan.thn =" +tahun+" and penjualan.ket = 1 or penjualan.ket =2",null);
        c.moveToFirst();
        penjualan = c.getInt(0);

    }

    private void hitungtarget(){
        c = db.rawQuery("select targetrp from target", null);
        if (c.moveToFirst()) {
            c.moveToFirst();
            int targetrp = c.getInt(0);
            target.setText("Target bulan ini = " + NumberFormat.getNumberInstance(Locale.US).format(targetrp) + ", Penjualan = " + NumberFormat.getNumberInstance(Locale.US).format(penjualan));
        }
    }
}
