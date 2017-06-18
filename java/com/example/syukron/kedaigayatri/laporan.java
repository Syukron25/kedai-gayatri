package com.example.syukron.kedaigayatri;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class laporan extends AppCompatActivity implements View.OnClickListener {

    private Button lapharian;
    private Button lapmingguan;
    private Button lapbulanan;

    private  Button lapstok, item, target, itembulan;

    private RadioButton cust, comp, pegawai;

    private EditText tgla,tglb,bln,thn;
    private EditText targetku;


    private SQLiteDatabase db;
    private Cursor c;
    private List<String> miningharian;
    private DatePicker kalender;

    private String  ket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);
        ket = "penjualan.ket = 1 or penjualan.ket = 2";

        lapharian = (Button) findViewById(R.id.lapharian);
        lapmingguan = (Button) findViewById(R.id.lapmingguan);
        lapbulanan = (Button) findViewById(R.id.lapbulanan);
        itembulan = (Button) findViewById(R.id.itembulan);
        tgla = (EditText) findViewById(R.id.tgla);
        tglb = (EditText) findViewById(R.id.tglb);
        bln = (EditText) findViewById(R.id.bln);
        thn = (EditText) findViewById(R.id.thn);
        lapstok = (Button) findViewById(R.id.lapstok);
        item = (Button) findViewById(R.id.item);
        target = (Button) findViewById(R.id.target);
        lapstok.setOnClickListener(this);
        item.setOnClickListener(this);
        target.setOnClickListener(this);
        itembulan.setOnClickListener(this);
        targetku = (EditText) findViewById(R.id.targetku);

        cust = (RadioButton) findViewById(R.id.customer);
        comp = (RadioButton) findViewById(R.id.comp);
        pegawai = (RadioButton) findViewById(R.id.pegawai);

        lapharian.setOnClickListener(this);
        lapmingguan.setOnClickListener(this);
        lapbulanan.setOnClickListener(this);
        cust.setOnClickListener(this);
        comp.setOnClickListener(this);
        pegawai.setOnClickListener(this);

        db = openOrCreateDatabase("gayatri", Context.MODE_PRIVATE, null);
        kalender = (DatePicker) findViewById(R.id.kalender);

    }

    @Override
    public void onClick(View v) {
        if (v == lapharian) {
            try {
                String jenislaporan = "LAPORAN HARIAN";
                int tgl = kalender.getDayOfMonth();
                int thn = kalender.getYear();
                int bulan = kalender.getMonth() + 1 ;
                String sqlharian ="SELECT * from penjualanlap where tgl = '"+tgl+"' and bln = '"+bulan+"' and thn = '"+thn+"' ";
                String tanggal ="Tanggal : " + tgl +" - "+ bulan+ " - " + thn;
                createPdf(sqlharian, jenislaporan, tanggal);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        if (v == lapmingguan) {
            try {
                String jenislaporan = "LAPORAN MINGGUAN";
                String sqlmingguan ="SELECT  * FROM penjualanlap WHERE tgl >=" +tgla.getText().toString()+" and tgl <=" +tglb.getText().toString()+" and bln ="+ bln.getText().toString()+" and thn =" +thn.getText().toString();
                String tanggal = "Tanggal : " + tgla.getText()+ " s/d " + tglb.getText() + " - " + bln.getText() + " - " + thn.getText();
                createPdf(sqlmingguan, jenislaporan,tanggal);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        if (v == lapbulanan) {
            try {
                String jenislaporan = "LAPORAN BULANAN";
                String sqlbulanan ="SELECT  * FROM penjualanlap where bln ="+ bln.getText().toString() +" and thn =" +thn.getText().toString();
                String tanggal = "Bulan : " + bln.getText() +" - " + thn.getText();
                createPdf(sqlbulanan, jenislaporan, tanggal);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        if (v==lapstok) {
            try {
                String jenislaporan = "LAPORAN Stok Barang";
                String sqlbulanan ="SELECT  nama, stok FROM stokbarang;";
                Date date = new Date();
                String tanggal = new SimpleDateFormat("dd - MM - yyyy").format(date);
                createPdf3kolom(sqlbulanan, jenislaporan, tanggal);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        if (v==item) {
            try {
                String jenislaporan = "LAPORAN Jumlah Penjualan";
                String sqlbulanan ="SELECT  nama, sum(qty) FROM penjualanlap GROUP BY nama ORDER BY sum(qty) DESC;";
                Date date = new Date();
                String tanggal = new SimpleDateFormat("dd - MM - yyyy").format(date);
                createPdf3kolom(sqlbulanan, jenislaporan, tanggal);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        if (v==itembulan) {
            try {
                Date date = new Date();
                String tanggal = new SimpleDateFormat("MM - yyyy").format(date);
                String bulan = new  SimpleDateFormat("MM").format(date);
                String tahun = new SimpleDateFormat("yyyy").format(date);
                String jenislaporan = "LAPORAN Jumlah Penjualan Bulan Ini";
                String sqlbulanan ="SELECT  nama, sum(qty) FROM penjualanlap WHERE bln ='"+ bulan +"' and thn = '" +tahun+"' GROUP BY nama ORDER BY sum(qty) DESC;";

                createPdf3kolom(sqlbulanan, jenislaporan, tanggal);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }


        if (v == target){
            db.execSQL("UPDATE target set targetrp = '"+targetku.getText().toString()+"' ;");
            String target = NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(targetku.getText().toString()));
            Toast.makeText(laporan.this,"Target berhasil di update menjadi ="+target+"",Toast.LENGTH_SHORT).show();
        }

        if (v==cust){
            ket = "penjualan.ket = 1 or penjualan.ket = 2";
        }

        if (v==comp){
            ket = "penjualan.ket = 3";
        }

        if (v==pegawai){
            ket = "penjualan.ket = 4";
        }
    }




    private void createPdf(String sql, String jenislaporan, String tanggal) throws FileNotFoundException, DocumentException {

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "Laporan");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
            Toast.makeText(laporan.this, "pdf berhasil dibuat" + pdfFolder, Toast.LENGTH_SHORT).show();
        }

        //Create time stamp
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);


        File myFile = new File(pdfFolder + timeStamp + ".pdf");
        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document(PageSize.A5);
        PdfWriter.getInstance(document, output);
        document.open();

        float fntSize, lineSpacing;
        fntSize = 12f;
        lineSpacing = 10f; new Phrase();
        Paragraph isi = new Paragraph(new Phrase(lineSpacing,"", FontFactory.getFont(FontFactory.COURIER, fntSize)));
        isi.add(new Paragraph(jenislaporan));
        isi.add(new Paragraph(tanggal));
        isi.add(new Paragraph(" "));
        isi.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setTotalWidth(300);
        table.setLockedWidth(true);
        table.setHeaderRows(1);
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(1);
        table.addCell("NO");
        table.addCell("NAMA");
        table.addCell("HARGA");
        table.addCell("QTY");
        table.addCell("TOTAL");
        table.addCell("KET");

        int i = 0;
        int totalharian = 0;
        c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                i++;
                String nama = c.getString(0);
                int qty = c.getInt(1);
                int harga = c.getInt(2);
                int totalharga = qty * harga;
                String qtys = String.valueOf(qty);
                String hargas = String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(harga));
                String nomorator = String.valueOf(i);
                String totalhargas = String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(totalharga));
                String keterangan = c.getString(3);
                totalharian = totalharian + totalharga;
                table.addCell(nomorator);
                table.addCell(nama);
                table.addCell(hargas);
                table.addCell(qtys);
                table.addCell(totalhargas);
                table.addCell(keterangan);
            } while (c.moveToNext());
        }

        isi.setAlignment(Element.ALIGN_CENTER);
        document.add(isi);
        document.add(table);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total pendapatan : "+String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(totalharian))));

        document.close();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


    private void createPdf3kolom(String sql, String jenislaporan, String tanggal) throws FileNotFoundException, DocumentException {

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "Laporan");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
            Toast.makeText(laporan.this, "pdf berhasil dibuat" + pdfFolder, Toast.LENGTH_SHORT).show();
        }

        //Create time stamp
        Date date = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);


        File myFile = new File(pdfFolder + timeStamp + ".pdf");
        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document(PageSize.A5);
        PdfWriter.getInstance(document, output);
        document.open();

        float fntSize, lineSpacing;
        fntSize = 12f;
        lineSpacing = 10f; new Phrase();
        Paragraph isi = new Paragraph(new Phrase(lineSpacing,"", FontFactory.getFont(FontFactory.COURIER, fntSize)));
        isi.add(new Paragraph(jenislaporan));
        isi.add(new Paragraph(tanggal));
        isi.add(new Paragraph(" "));
        isi.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setTotalWidth(300);
        table.setLockedWidth(true);
        table.setHeaderRows(1);
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(1);
        table.addCell("NO");
        table.addCell("NAMA");
        table.addCell("JUMLAH");


        int i = 0;
        int totalharian = 0;
        c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                i++;
                String nama = c.getString(0);
                int jumlah = c.getInt(1);
                String jumlahs = String.valueOf(jumlah);
                String nomorator = String.valueOf(i);
                table.addCell(nomorator);
                table.addCell(nama);
                table.addCell(jumlahs);
            } while (c.moveToNext());
        }

        isi.setAlignment(Element.ALIGN_CENTER);
        document.add(isi);
        document.add(table);
        // document.add(new Paragraph(" "));
        //  document.add(new Paragraph("Total pendapatan : "+String.valueOf(NumberFormat.getNumberInstance(Locale.US).format(totalharian))));

        document.close();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFile), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


}
