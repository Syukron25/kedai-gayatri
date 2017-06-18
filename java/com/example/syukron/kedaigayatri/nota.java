package com.example.syukron.kedaigayatri;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class nota extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private SQLiteDatabase db;
    private Cursor c;
    private int nomornota;
    private int hargatotal;
    private DatePicker almandak;
    private Button tanggal, print;
    private  TextView ket;
    private  TextView date;

    private ArrayList<HashMap<String, String>> ID;
    private testActivity adapter;
    public HashMap<String,String> temp;
    private ListView listView;
    private String notakeluar;
    private Button next;
    private Button prev;
    private TextView nomor;
    private TextView total;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("penjualan");

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
        setContentView(R.layout.activity_nota);
        nomornota = 1;
        notakeluar ="";
        db=openOrCreateDatabase("gayatri", Context.MODE_PRIVATE, null);

        ID=new ArrayList<HashMap<String,String>>();
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        adapter=new testActivity(this, ID);
        listView.setAdapter(adapter);
        temp=new HashMap<String, String>();

        next = (Button) findViewById(R.id.next);
        prev = (Button) findViewById(R.id.prev);
        nomor = (TextView) findViewById(R.id.nomor);
        tanggal = (Button) findViewById(R.id.tanggal);
        almandak = (DatePicker) findViewById(R.id.almandak);
        total = (TextView) findViewById(R.id.total);
        ket = (TextView) findViewById(R.id.ket);
        date = (TextView) findViewById(R.id.date);
        print = (Button) findViewById(R.id.print);

        next.setOnClickListener(this);
        prev.setOnClickListener(this);
        tanggal.setOnClickListener(this);
        print.setOnClickListener(this);

        lihatin(nomornota);


    }

    private void lihatin(int idnota) {
        ID.clear();
        final int[] i = {1};

        Query qref = myRef.orderByChild("nota").equalTo(idnota);
        qref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap<penjualan,penjualan> data = ( HashMap<penjualan,penjualan>) dataSnapshot.getValue();

    HashMap<String, String> temp = new HashMap();
    temp.put(testActivity.FIRST_COLUMN, String.valueOf(i[0]++));
    temp.put(testActivity.SECOND_COLUMN, String.valueOf(data.get("namabrg")));
    temp.put(testActivity.THIRD_COLUMN, String.valueOf(data.get("qty")));
    temp.put(testActivity.FOURTH_COLUMN, String.valueOf(data.get("harga")));
    hargatotal = hargatotal + (Integer.parseInt(String.valueOf(data.get("qty")))) * Integer.parseInt(String.valueOf(data.get("harga")));
    total.setText(String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(hargatotal)));
                notakeluar = String.valueOf(data.get("namabrg")) +"   " + String.valueOf(data.get("qty")) + "   \t " + String.valueOf(data.get("harga")) + "  \t  " + hargatotal + "\n" + notakeluar;
    ID.add(temp);

                listView.invalidateViews();

                String keterangan = String.valueOf(data.get("ket"));
                String tgl = String.valueOf(data.get("tgl"));
                String bln = String.valueOf(data.get("bln"));
                String thn = String.valueOf(data.get("thn"));

                ket.setText(keterangan);

                date.setText(tgl + "-" + bln + "-" + thn);
                hargatotal = 0;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                listView.invalidateViews();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listView.invalidateViews();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                listView.invalidateViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listView.invalidateViews();
    }

    private void ketanggal(String ddmmyy) {
        ID.clear();
        final int[] i = {1};

        Query qref = myRef.orderByChild("date").equalTo(ddmmyy).limitToFirst(1);
        qref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap<penjualan,penjualan> data = ( HashMap<penjualan,penjualan>) dataSnapshot.getValue();

               nomornota = Integer.parseInt(String.valueOf(data.get("nota")));
                nomor.setText(String.valueOf(nomornota));
                lihatin(nomornota);
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

    @Override
    public void onClick(View v) {
        if (v == next) {
notakeluar = "";
            hargatotal = 0;
            nomornota += 1;
            nomor.setText(String.valueOf(nomornota));
            lihatin(nomornota);


        }
        if (v == prev && nomornota != 1) {
            hargatotal = 0;
            nomornota -= 1;
            nomor.setText(String.valueOf(nomornota));
            lihatin(nomornota);
            notakeluar="";
        }

        if (v ==tanggal){
            int bulan = almandak.getMonth()+1;
String ddmmyy = almandak.getDayOfMonth()+"-"+bulan+"-"+almandak.getYear();
          ketanggal(ddmmyy);
          }

if (v == print){
   try {
        findBT();
        openBT();
        sendData();

    } catch (IOException e) {
        e.printStackTrace();
    }

}

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
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

            beginListenForData();


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
            String msg = "        Kedai Gayatri \n   Paramount Gading Serpong \n Tangerang Telp. 0877 8048 3888 \n "+"   Nota no."+ nomornota+"     Meja no. "+ "-"+"\n \n"
                    + "----------------------------- \n"

                    + "nama   qty    harga    total \n"
                    + notakeluar+"\n"
                    + "----------------------------- \n"
                    +" Total   : "+total.getText().toString().replace(",","")+"\n Bayar   : "+"-" +"\n kembali : "+"-"
                    + "\n ----------------------------- \n"
                    +" \n         Terima Kasih \n \n \n";

           mmOutputStream.write(msg.getBytes());

            // tell the user data were sent

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
