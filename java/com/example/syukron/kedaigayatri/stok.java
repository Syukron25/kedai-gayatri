package com.example.syukron.kedaigayatri;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class stok extends AppCompatActivity implements  View.OnClickListener {


    private SQLiteDatabase db;
    private Cursor c;
    private List<String> masteritem;
    private List<String> masternama;
    private int incitem;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("stok");
    private TextView satu;
    private TextView dua;
    private TextView tiga;
    private TextView empat;
    private TextView lima;
    private TextView enam;
    private TextView tujuh;
    private TextView delapan;
    private TextView sembilan;
    private TextView sepuluh;

    private TextView satun;
    private TextView duan;
    private TextView tigan;
    private TextView empatn;
    private TextView liman;
    private TextView enamn;
    private TextView tujuhn;
    private TextView delapann;
    private TextView sembilann;
    private TextView sepuluhn;

    private TextView satuq;
    private TextView duaq;
    private TextView tigaq;
    private TextView empatq;
    private TextView limaq;
    private TextView enamq;
    private TextView tujuhq;
    private TextView delapanq;
    private TextView sembilanq;
    private TextView sepuluhq;

    private EditText a;
    private EditText b;
    private EditText c1;
    private EditText d;
    private EditText e;
    private EditText f;
    private EditText g;
    private EditText h;
    private EditText i;
    private EditText j;

    private Button next;
    private Button prev;
    private Button tambah;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stok);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
           db=openOrCreateDatabase("gayatri", Context.MODE_PRIVATE, null);
        masteritem =  new ArrayList<String>();
        masternama =  new ArrayList<String>();


        satu = (TextView) findViewById(R.id.satu);
        dua = (TextView) findViewById(R.id.dua);
        tiga = (TextView) findViewById(R.id.tiga);
       empat = (TextView) findViewById(R.id.empat);
       lima = (TextView) findViewById(R.id.lima);
        enam = (TextView) findViewById(R.id.enam);
       tujuh = (TextView) findViewById(R.id.tujuh);
        delapan = (TextView) findViewById(R.id.delapan);
        sembilan = (TextView) findViewById(R.id.sembilan);
        sepuluh = (TextView) findViewById(R.id.sepuluh);

        satun = (TextView) findViewById(R.id.satun);
        duan = (TextView) findViewById(R.id.duan);
        tigan = (TextView) findViewById(R.id.tigan);
        empatn = (TextView) findViewById(R.id.empatn);
        liman = (TextView) findViewById(R.id.liman);
        enamn = (TextView) findViewById(R.id.enamn);
        tujuhn = (TextView) findViewById(R.id.tujuhn);
        delapann = (TextView) findViewById(R.id.delapann);
        sembilann = (TextView) findViewById(R.id.sembilann);
        sepuluhn = (TextView) findViewById(R.id.sepuluhn);

        satuq = (TextView) findViewById(R.id.satuq);
        duaq = (TextView) findViewById(R.id.duaq);
        tigaq = (TextView) findViewById(R.id.tigaq);
        empatq = (TextView) findViewById(R.id.empatq);
        limaq = (TextView) findViewById(R.id.limaq);
        enamq = (TextView) findViewById(R.id.enamq);
        tujuhq = (TextView) findViewById(R.id.tujuhq);
        delapanq = (TextView) findViewById(R.id.delapanq);
        sembilanq = (TextView) findViewById(R.id.sembilanq);
        sepuluhq = (TextView) findViewById(R.id.sepuluhq);

        a = (EditText) findViewById(R.id.a);
        b = (EditText) findViewById(R.id.b);
        c1 = (EditText) findViewById(R.id.c1);
        d = (EditText) findViewById(R.id.d);
        e = (EditText) findViewById(R.id.e);
        f = (EditText) findViewById(R.id.f);
        h = (EditText) findViewById(R.id.h);
        g = (EditText) findViewById(R.id.g);
        i = (EditText) findViewById(R.id.i);
        j = (EditText) findViewById(R.id.j);


        next = (Button) findViewById(R.id.nextq);
        prev = (Button) findViewById(R.id.prevq);
        tambah = (Button) findViewById(R.id.tambah);

        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        tambah.setOnClickListener(this);
        bukamasteritem();
        setStok();
        setNama();
        setNomor();
        clearText();

    }


    protected void bukamasteritem(){
            masternama.clear();
            masteritem.clear();

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap<stokbarang,stokbarang> data = ( HashMap<stokbarang,stokbarang>) dataSnapshot.getValue();
               masternama.add(String.valueOf(data.get("nama")));
                masteritem.add(String.valueOf(data.get("stok")));
                setStok();
                setNama();
                setNomor();
                Log.d("cekidot",masteritem.size()+"uk");

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
        Log.d("cekidot",masteritem.size()+" ukuran akhir");


    }

    protected void setStok(){

        if (masteritem.size() > 0+incitem){
            satu.setText(masteritem.get(0+incitem));}
        else { satu.setText(""); }

        if (masteritem.size() > 1+incitem){
            dua.setText(masteritem.get(1+incitem));}
        else { dua.setText(""); }

        if (masteritem.size() > 2+incitem){
            tiga.setText(masteritem.get(2+incitem));}
        else { tiga.setText(""); }

        if (masteritem.size() > 3+incitem){
            empat.setText(masteritem.get(3+incitem));}
        else { empat.setText(""); }

        if (masteritem.size() > 4+incitem){
            lima.setText(masteritem.get(4+incitem));}
        else { lima.setText(""); }

        if (masteritem.size() > 5+incitem){
            enam.setText(masteritem.get(5+incitem));}
        else { enam.setText(""); }

        if (masteritem.size() > 6+incitem){
            tujuh.setText(masteritem.get(6+incitem));}
        else { tujuh.setText(""); }

        if (masteritem.size() > 7+incitem){
            delapan.setText(masteritem.get(7+incitem));}
        else { delapan.setText(""); }

        if (masteritem.size() > 8+incitem){
            sembilan.setText(masteritem.get(8+incitem));}
        else { sembilan.setText(""); }

        if (masteritem.size() > 9+incitem){
            sepuluh.setText(masteritem.get(9+incitem));}
        else { sepuluh.setText(""); }

    }

    protected void setNama(){

        if (masternama.size() > 0+incitem){
            satun.setText(masternama.get(0+incitem));}
        else { satun.setText(""); }

        if (masternama.size() > 1+incitem){
            duan.setText(masternama.get(1+incitem));}
        else { duan.setText(""); }

        if (masternama.size() > 2+incitem){
            tigan.setText(masternama.get(2+incitem));}
        else { tigan.setText(""); }

        if (masternama.size() > 3+incitem){
            empatn.setText(masternama.get(3+incitem));}
        else { empatn.setText(""); }

        if (masternama.size() > 4+incitem){
            liman.setText(masternama.get(4+incitem));}
        else { liman.setText(""); }

        if (masternama.size() > 5+incitem){
            enamn.setText(masternama.get(5+incitem));}
        else { enamn.setText(""); }

        if (masternama.size() > 6+incitem){
            tujuhn.setText(masternama.get(6+incitem));}
        else { tujuhn.setText(""); }

        if (masternama.size() > 7+incitem){
            delapann.setText(masternama.get(7+incitem));}
        else { delapann.setText(""); }

        if (masternama.size() > 8+incitem){
            sembilann.setText(masternama.get(8+incitem));}
        else { sembilann.setText(""); }

        if (masternama.size() > 9+incitem){
            sepuluhn.setText(masternama.get(9+incitem));}
        else { sepuluhn.setText(""); }

    }

    private void setNomor(){
        satuq.setText(String.valueOf(1+incitem));
        duaq.setText(String.valueOf(2+incitem));
        tigaq.setText(String.valueOf(3+incitem));
        empatq.setText(String.valueOf(4+incitem));
        limaq.setText(String.valueOf(5+incitem));
        enamq.setText(String.valueOf(6+incitem));
        tujuhq.setText(String.valueOf(7+incitem));
        delapanq.setText(String.valueOf(8+incitem));
        sembilanq.setText(String.valueOf(9+incitem));
        sepuluhq.setText(String.valueOf(10+incitem));

       if ( masternama.size() - incitem > 1){
            b.setVisibility(View.VISIBLE);
        } else {b.setVisibility(View.INVISIBLE);}

        if ( masternama.size() - incitem > 2){
            c1.setVisibility(View.VISIBLE);
        } else {c1.setVisibility(View.INVISIBLE);}

      if ( masternama.size() - incitem > 3){
            d.setVisibility(View.VISIBLE);
        } else {d.setVisibility(View.INVISIBLE);}

         if ( masternama.size() - incitem > 4){
            e.setVisibility(View.VISIBLE);
        } else {e.setVisibility(View.INVISIBLE);}

        if ( masternama.size() - incitem > 5){
            f.setVisibility(View.VISIBLE);
        } else {f.setVisibility(View.INVISIBLE);}

        if ( masternama.size() - incitem > 6){
            g.setVisibility(View.VISIBLE);
        } else {g.setVisibility(View.INVISIBLE);}

        if ( masternama.size() - incitem > 7){
            h.setVisibility(View.VISIBLE);
        } else {h.setVisibility(View.INVISIBLE);}

        if ( masternama.size() - incitem > 8){
            i.setVisibility(View.VISIBLE);
        } else {i.setVisibility(View.INVISIBLE);}

        if ( masternama.size() - incitem > 9){
            j.setVisibility(View.VISIBLE);
        } else {j.setVisibility(View.INVISIBLE);}



    }

    private void clearText(){
    a.setText("");
    b.setText("");
    c1.setText("");
    d.setText("");
    e.setText("");
    f.setText("");
    g.setText("");
    h.setText("");
    i.setText("");
    j.setText("");
}

    private void insertJumlahStokfb(String nama, final int stok){
    final Query qref = myRef.orderByChild("nama").equalTo(nama).limitToFirst(1);
    qref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                snapshot.getRef().child("stok").setValue(stok);

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}

private void insertJumlahStok(){
        if (a.getText().toString().equals("")){}else {
        int pertama = Integer.parseInt(satu.getText().toString()) + Integer.parseInt(a.getText().toString());
        String query = "UPDATE stokbarang SET stok ='" + pertama + "' where nama = '" + satun.getText().toString() + "'";
        db.execSQL(query);
        insertJumlahStokfb(satun.getText().toString(),pertama);}

        if (b.getText().toString().equals("")){}else {
        int kedua = Integer.parseInt(dua.getText().toString()) + Integer.parseInt(b.getText().toString());
        String query2 = "UPDATE stokbarang SET stok ='" + kedua + "' where nama = '" + duan.getText().toString() + "'";
        db.execSQL(query2);
       insertJumlahStokfb(duan.getText().toString(),kedua);}

        if (c1.getText().toString().equals("")){}else {
        int ketiga = Integer.parseInt(tiga.getText().toString()) + Integer.parseInt(c1.getText().toString());
        String query3 = "UPDATE stokbarang SET stok ='" + ketiga + "' where nama = '" + tigan.getText().toString() + "'";
        db.execSQL(query3);}

        if (d.getText().toString().equals("")){}else {
        int keempat = Integer.parseInt(empat.getText().toString()) + Integer.parseInt(d.getText().toString());
        String query4 = "UPDATE stokbarang SET stok ='" + keempat + "' where nama = '" + empatn.getText().toString() + "'";
        db.execSQL(query4);}

        if (e.getText().toString().equals("")){}else {

        int kelima = Integer.parseInt(lima.getText().toString()) + Integer.parseInt(e.getText().toString());
        String query5 = "UPDATE stokbarang SET stok ='" + kelima + "' where nama = '" + liman.getText().toString() + "'";
        db.execSQL(query5);}

        if (f.getText().toString().equals("")){}else {
        int keenam = Integer.parseInt(enam.getText().toString()) + Integer.parseInt(f.getText().toString());
        String query6 = "UPDATE stokbarang SET stok ='" + keenam + "' where nama = '" + enamn.getText().toString() + "'";
        db.execSQL(query6);}

        if (g.getText().toString().equals("")){}else {
        int ketujuh = Integer.parseInt(tujuh.getText().toString()) + Integer.parseInt(g.getText().toString());
        String query7 = "UPDATE stokbarang SET stok ='" + ketujuh + "' where nama = '" + tujuhn.getText().toString() + "'";
        db.execSQL(query7);}

        if (h.getText().toString().equals("")){}else {
        int kedelapan = Integer.parseInt(delapan.getText().toString()) + Integer.parseInt(h.getText().toString());
        String query8 = "UPDATE stokbarang SET stok ='" + kedelapan + "' where nama = '" + delapann.getText().toString() + "'";
        db.execSQL(query8);}

        if (i.getText().toString().equals("")){}else {
        int kesembilan = Integer.parseInt(sembilan.getText().toString()) + Integer.parseInt(i.getText().toString());
        String query9 = "UPDATE stokbarang SET stok ='" + kesembilan + "' where nama = '" + sembilann.getText().toString() + "'";
        db.execSQL(query9);}

        if (j.getText().toString().equals("")){}else {
        int kesepuluh = Integer.parseInt(sepuluh.getText().toString()) + Integer.parseInt(j.getText().toString());
        String query10 = "UPDATE stokbarang SET stok ='" + kesepuluh + "' where nama = '" + sepuluhn.getText().toString() + "'";
        db.execSQL(query10);}

}


    @Override
    public void onClick(View v) {
        if (v == next) {


            insertJumlahStok();
            setNomor();
            setStok();
            setNama();
            clearText();
            bukamasteritem();


             incitem+=10;
Log.d("cekidot",masternama.size()+" uk - incitem : "+incitem);
        }

        if (v == prev) {
            if ( incitem > 0)
                incitem-=10;
            setNomor();
            setStok();
            setNama();
            clearText();
            bukamasteritem();
        }

        if (v == tambah){
            Intent intent = new Intent(stok.this, tambahstok.class);

            startActivity(intent);

        }
    }


}
