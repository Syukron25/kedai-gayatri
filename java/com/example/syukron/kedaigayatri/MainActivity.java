package com.example.syukron.kedaigayatri;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends Activity  implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private SQLiteDatabase db;
    private Cursor c;
    private ArrayList<HashMap<String, String>> ID;
    private ListView listView;
    private Button satu,dua,tiga,empat, lima,enam, tujuh,delapan,sembilan,ok,selesai,next,prev,logout;
    private String namalog,jabatanlog, keterangan, notakeluar ;
    private EditText qty,input;
    private TextView item,total,nonota,jabatan, myLabel;
    public  HashMap<String,String>  temp;
    private int kode,pembayaran,kembali,hargatotal, tgl, bln,thn, ket,nomornotas,incitem, nomormeja, totalbayar;
    private View dialogView;
    private testActivity adapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("penjualan");
    FirebaseDatabase database2 = FirebaseDatabase.getInstance();
    DatabaseReference stokRef = database2.getReference("stok");
    private String[] arraySpinner;
    private Spinner meja;
    private List<String> masteritem;
    private RadioButton debit, cash, comp, employ;
    private Button refresh;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=openOrCreateDatabase("gayatri", Context.MODE_PRIVATE, null);
        ket = 1;
        keterangan = "cash";
        notakeluar = "";
        ID=new ArrayList<HashMap<String,String>>();
        listView = (ListView) findViewById(R.id.no);
        listView.setOnItemClickListener(this);
        adapter=new testActivity(this, ID);
        listView.setAdapter(adapter);
        temp=new HashMap<String, String>();
        masteritem =  new ArrayList<String>();
        bukamasteritem();

        satu = (Button) findViewById(R.id.satu);
        dua = (Button) findViewById(R.id.dua);
        tiga = (Button) findViewById(R.id.tiga);
        empat = (Button) findViewById(R.id.empat);
        lima = (Button) findViewById(R.id.lima);
        enam = (Button) findViewById(R.id.enam);
        tujuh = (Button) findViewById(R.id.tujuh);
        delapan = (Button) findViewById(R.id.delapan);
        sembilan = (Button) findViewById(R.id.sembilan);
        ok = (Button) findViewById(R.id.ok);
        selesai = (Button) findViewById(R.id.selesai);
        next = (Button) findViewById(R.id.next);
        prev = (Button) findViewById(R.id.prev);
        logout = (Button) findViewById(R.id.logout);
        refresh = (Button) findViewById(R.id.refresh);

        qty = (EditText) findViewById(R.id.qty);
        item = (TextView) findViewById(R.id.item);
        total = (TextView) findViewById(R.id.total);
        nonota = (TextView) findViewById(R.id.nomornota);
        jabatan = (TextView) findViewById(R.id.jabatan);
        myLabel = (TextView) findViewById(R.id.printer);
        debit = (RadioButton) findViewById(R.id.debit);
        cash = (RadioButton) findViewById(R.id.cash);
        comp = (RadioButton) findViewById(R.id.comp);
        employ = (RadioButton) findViewById(R.id.pegawai);
        meja = (Spinner) findViewById(R.id.meja);

        updatenomornota();

        satu.setOnClickListener(this);
        dua.setOnClickListener(this);
        tiga.setOnClickListener(this);
        empat.setOnClickListener(this);
        lima.setOnClickListener(this);
        enam.setOnClickListener(this);
        tujuh.setOnClickListener(this);
        delapan.setOnClickListener(this);
        sembilan.setOnClickListener(this);
        debit.setOnClickListener(this);
        cash.setOnClickListener(this);
        comp.setOnClickListener(this);
        employ.setOnClickListener(this);
        ok.setOnClickListener(this);
        selesai.setOnClickListener(this);
        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        meja.setOnItemSelectedListener(this);
        logout.setOnClickListener(this);
        refresh.setOnClickListener(this);

        try {
            findBT();
            openBT();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.bayar, null);
        input = (EditText) dialogView.findViewById(R.id.nilaibayar);
        input.addTextChangedListener(onTextChangedListener());

        showRecords();
        gantiTextTombol();

        Calendar calendar = Calendar.getInstance();
        tgl = calendar.get(Calendar.DAY_OF_MONTH);
        bln = calendar.get(Calendar.MONTH)+1;
        thn = calendar.get(Calendar.YEAR);

        jabatanlog = getIntent().getStringExtra("EXTRA_SESSION_JABATAN");
        namalog = getIntent().getStringExtra("EXTRA_SESSION_NAMA");
        jabatan.setText(namalog+" - " + jabatanlog);

        if (jabatanlog.equals("kasir")){
            comp.setVisibility(View.INVISIBLE);
            employ.setVisibility(View.INVISIBLE);
        }

        this.arraySpinner = new String[] {
                "1", "2", "3", "4", "5","6", "7", "8", "9", "10"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        meja.setAdapter(adapter);
        bacadb();
    }

    private void tambahdb() {
notakeluar = "";
        c = db.rawQuery("SELECT  datamaster.nama, penjualan.qty, datamaster.harga FROM penjualan INNER JOIN datamaster ON penjualan.kode = datamaster.kode WHERE penjualan.idnota =" + nomornotas + ";", null);
        if (c.moveToFirst()) {
            do {
                String nama = c.getString(0);
                int qty = c.getInt(1);
                int harga = c.getInt(2);
                int totalharga = qty * harga;
                String date =tgl+"-"+bln+"-"+thn;
                if (ket == 3 || ket == 4){
                penjualan jual = new penjualan(nomornotas, nama, qty, 0, 0, keterangan,tgl,bln,thn, date);
                myRef.push().setValue(jual);
                notakeluar =nama +"   " + qty + "   \t " + 0 + "  \t  " + 0 + "\n" + notakeluar;}
                else if (ket == 1 || ket == 2)
                {
                    penjualan jual = new penjualan(nomornotas, nama, qty, harga, totalharga, keterangan,tgl,bln,thn, date);
                    myRef.push().setValue(jual);
                    notakeluar =nama +"   " + qty + "   \t " + harga + "  \t  " + totalharga + "\n" + notakeluar;
                }

Log.d("cekidot",notakeluar);
            } while (c.moveToNext());
        }
    }

private void bacadb() {
      // Read from the database
stokRef.addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {


        HashMap<stokbarang,stokbarang> data = ( HashMap<stokbarang,stokbarang>) dataSnapshot.getValue();
        db.execSQL("UPDATE stokbarang set stok = "+String.valueOf(data.get("stok"))+" where nama = '"+String.valueOf(data.get("nama"))+"' ;");
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


    private  void  keyboardmuncul(){
    InputMethodManager inputMethodManager =
            (InputMethodManager) getSystemService(
                    Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.showSoftInput(getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
}


    private void beres() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("kembalian = Rp."+NumberFormat.getNumberInstance(Locale.US).format(kembali)+", Buat Nota Lagi ?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        nomornotas = nomornotas+1;
                        nonota.setText(String.valueOf(nomornotas));

                        try {
                            findBT();
                            openBT();
                            sendData();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        total.setText("0");
                        showRecords();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        nomornotas = nomornotas+1;
                        nonota.setText(String.valueOf(nomornotas));
                        total.setText("0");
                        showRecords();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void popup() {
        ok.setVisibility(View.VISIBLE);
        qty.setVisibility(View.VISIBLE);
        item.setText("jumlah item");
        qty.requestFocus();
        keyboardmuncul();
    }


    public void ilang() {
        ok.setVisibility(View.GONE);
        qty.setVisibility(View.GONE);
        qty.clearFocus();

    }


    /////////// set nomor nota //////////
    protected void updatenomornota() {
      c =db.rawQuery("select MAX (idnota) FROM penjualan",null);
        c.moveToFirst();
     nomornotas = c.getInt(0) + 1;
        nonota.setText(String.valueOf(nomornotas));
        }

    protected void bukamasteritem(){

    c = db.rawQuery("SELECT NAMA FROM DATAMASTER", null);
    if (c.moveToFirst()) {
        do {
            String nama = c.getString(0);
            masteritem.add(nama);
        } while (c.moveToNext());
    }
}


    protected void gantiTextTombol (){

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

}
    ////////////////// DATABASE //////////////////

    protected void insertIntoDB(){
            String kuantitas = qty.getText().toString();
            String query = "INSERT INTO penjualan (kode,qty,idnota,ket, tgl,bln,thn) VALUES('"+kode+"', '"+kuantitas+"','"+nomornotas+"','"+ket+"','"+tgl+"','"+bln+"','"+thn+"');";
        db.execSQL(query);
            showRecords();
        }

    protected void updatestok() {

            String kuantitas = qty.getText().toString();
            int qtys = Integer.parseInt(kuantitas);
            String querypengurangan = "SELECT stokbarang.nama, stokbarang.stok - pengurangan.jumlah *"+qtys+" as sisa from stokbarang inner join  pengurangan on stokbarang.nama = pengurangan.namastok where pengurangan.kode='"+kode+"';";
            c = db.rawQuery(querypengurangan,null);
            if (c.moveToFirst()) {
                do {
                  String   nama = c.getString(0);
                   int  sisa = c.getInt(1);
                    db.execSQL("UPDATE stokbarang set stok = "+sisa+" where nama = '"+nama+"' ;");


                    final Query qref = stokRef.orderByChild("nama").equalTo(nama).limitToFirst(1);
                    final int finalSisa = sisa;
                    qref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                snapshot.getRef().child("stok").setValue(finalSisa);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } while (c.moveToNext());
                     }

        }


    protected void balikstok(int qtys) {
        String querypengurangan = "SELECT stokbarang.nama, stokbarang.stok + pengurangan.jumlah *"+qtys+" as sisa from stokbarang inner join  pengurangan on stokbarang.nama = pengurangan.namastok where pengurangan.kode='"+kode+"';";
        c = db.rawQuery(querypengurangan,null);
        if (c.moveToFirst()) {
            do {
                String nama = c.getString(0);
                int sisa = c.getInt(1);

                db.execSQL("UPDATE stokbarang set stok = "+sisa+" where nama = '"+nama+"' ;");
                final Query qref = stokRef.orderByChild("nama").equalTo(nama).limitToFirst(1);
                final int finalSisa = sisa;
                qref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            snapshot.getRef().child("stok").setValue(finalSisa);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } while (c.moveToNext());
        }
    }

    protected void showRecords() {
        ID.clear();
        listView.setAdapter(adapter);
         int i = 0;
         hargatotal = 0;
         c = db.rawQuery("SELECT  datamaster.nama, penjualan.qty, datamaster.harga FROM penjualan INNER JOIN datamaster ON penjualan.kode = datamaster.kode WHERE penjualan.idnota =" +nomornotas+";", null);
         if (c.moveToFirst()) {
            do {
                 i++;
                String nama = c.getString(0);
                int qty = c.getInt(1);
                int harga = c.getInt(2);
                int totalharga = qty*harga;
                String qtys = String.valueOf(qty);
                String hargas = String.valueOf(harga);
                String nomorator= String.valueOf(i);
                String totalhargas = String.valueOf(totalharga);
                HashMap<String, String> temp = new HashMap();
                temp.put(testActivity.FIRST_COLUMN, nomorator);
                temp.put(testActivity.SECOND_COLUMN, nama);
                temp.put(testActivity.THIRD_COLUMN, qtys);
                temp.put(testActivity.FOURTH_COLUMN, hargas);
                temp.put(testActivity.FIFTH_COLUMN, totalhargas);
                ID.add(temp);
                if (ket ==1 || ket==2) {
                hargatotal = hargatotal + (qty*harga);
                total.setText(String.valueOf( NumberFormat.getNumberInstance(Locale.US).format(hargatotal)));}
            } while (c.moveToNext());
        }
        listView.invalidateViews();
      }

    protected void popupmenu(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Total belanja = Rp."+total.getText().toString());
        alert.setMessage("Jumlah Pembayaran");
        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.bayar, null);
        input = (EditText) dialogView.findViewById(R.id.nilaibayar);
        input.addTextChangedListener(onTextChangedListener());
        alert.setView(dialogView);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (input.getText().toString().equals("")){kembali = 0;}else{
                String bayarstring = input.getText().toString().replace(",","");
                pembayaran = Integer.parseInt( bayarstring);
                kembali = pembayaran - hargatotal;}
                beres();
                tambahdb();
                try {
                    findBT();
                    openBT();
                    sendData();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               // Canceled.
            }
        });

        alert.show();}

    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, final long id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Hapus transaksi?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
String isi = listView.getItemAtPosition(position).toString();
                        String nama = isi.substring(isi.indexOf("Second=")+7,isi.indexOf("First")-2);
                        String qty = isi.substring(isi.indexOf("Third=")+6,isi.indexOf("Second=")-2);
                        int qtys = Integer.parseInt(qty);
                        Toast.makeText(MainActivity.this,qty, Toast.LENGTH_SHORT).show();
                        String sqldelete = "DELETE FROM penjualan WHERE idnota='" + nomornotas + "' AND kode=(SELECT kode FROM datamaster WHERE nama='"+nama+"');";
                        balikstok(qtys);
                        db.execSQL(sqldelete);
                        listView.setAdapter(null);
                        total.setText("0");
                        showRecords();
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

    @Override
    public void onClick(View v) {
        if (v == satu) {
            popup();
            kode = 1+incitem;
        }

        if (v == dua) {
           popup();
            kode = 2+incitem;
        }

        if (v == tiga) {
            popup();
            kode = 3+incitem;
        }

        if (v == empat) {
            popup();
            kode = 4+incitem;
        }

        if (v == lima) {
            popup();
            kode = 5+incitem;
        }

        if (v == enam) {
            popup();
            kode = 6+incitem;
        }

        if (v == tujuh) {
            popup();
            kode = 7+incitem;
        }

        if (v == delapan) {
            popup();
            kode = 8+incitem;
        }

        if (v == sembilan) {
            popup();
            kode = 9+incitem;
        }


        if (v == ok) {
            InputMethodManager inputManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                    this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        if (qty.getText().toString().equals("")){
            Toast.makeText(MainActivity.this,"Mohon Isi Jumlah Quantity",Toast.LENGTH_SHORT).show();
        } else {
           insertIntoDB();
           ilang();
            updatestok();
            item.setText(" ");
            qty.setText("");}
        }

        if (v == selesai) {
            popupmenu();
            keyboardmuncul();


        }

        if (v == next) {


        if(masteritem.size()-9 > incitem) {
            incitem += 9;
            gantiTextTombol();
                }
        }

        if (v == prev) {
            if(incitem >0) {
                incitem -= 9;
                gantiTextTombol();
            }
        }

        if (v == debit){
            ket = 2;
            keterangan = "debit";

        }

        if (v == cash){
            ket = 1;
            keterangan = "cash";

        }

        if (v == comp){
            ket = 3;
            keterangan = "comp";
            total.setText("0");
         }

        if (v == employ){
            ket = 4;
            keterangan = "pegawai";
            total.setText("0");
        }

        if (v == logout) {
            if (total.getText().equals("0")){
            Intent intent = new Intent(MainActivity.this, halamanutama.class);
                intent.putExtra("EXTRA_SESSION_JABATAN", jabatanlog);
                intent.putExtra("EXTRA_SESSION_NAMA", namalog);
            startActivity(intent);
        } else { Toast.makeText(MainActivity.this,"Mohon hapus transaksi terlebih dahulu",Toast.LENGTH_LONG).show();}
        }

if (v == refresh)
{
    try {
        closeBT();
    } catch (IOException e) {
        e.printStackTrace();
    }
    findBT();

    try {
        openBT();
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    }

    @Override
    public void onBackPressed(){

    }

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                input.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                   input.setText(formattedString);
                    input.setSelection(input.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                input.addTextChangedListener(this);
            }
        };

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         nomormeja = position+1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                myLabel.setText("No bluetooth adapter available");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals("EP5802AI")) {
                        mmDevice = device;
                        break;
                    }
                }
            }

          //  myLabel.setText("Bluetooth device found.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();
            Log.d("cekidot",mmSocket.isConnected()+"");

            beginListenForData();

            myLabel.setText("Bluetooth Opened");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // this will send text data to be printed by the bluetooth printer
    void sendData() throws IOException {
        try {

            // the text typed by the user
            String msg = "        Kedai Gayatri \n   Paramount Gading Serpong \n Tangerang Telp. 0877 8048 3888 \n "+"   Nota no."+ nomornotas+"     Meja no. "+ nomormeja+"\n \n"
                    + "----------------------------- \n"

                    + "nama   qty    harga    total \n"
                    + notakeluar+"\n"
                    + "----------------------------- \n"
                    +" Total   : "+total.getText().toString().replace(",","")+"\n Bayar   : "+ pembayaran +"\n kembali : "+kembali
                    + "\n ----------------------------- \n"
                    +" \n         Terima Kasih \n \n \n";
       //     msg += "\n";

Log.d ("cekidot",msg);
            mmOutputStream.write(msg.getBytes());

            // tell the user data were sent
            myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
