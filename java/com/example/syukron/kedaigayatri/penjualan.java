package com.example.syukron.kedaigayatri;

public  class penjualan {
    public  String namabrg;
    public int qty;
    public int harga;
    public int totalharga;
    public String ket;
    public int tgl;
    public int bln;
    public int thn;
    public int nota;
    public String date;

    public penjualan ( ){

    }

    public penjualan(int nota, String namabrg, int qty, int harga, int totalharga, String ket, int tgl, int bln, int thn, String date){
        this.namabrg = namabrg;
        this.qty = qty;
        this.harga = harga;
        this.totalharga = totalharga;
        this.nota = nota;
        this.ket = ket;
        this.tgl = tgl;
        this.bln = bln;
        this.thn = thn;
        this.date = date;
    }

    public String getNamabrg(){
        return namabrg;
    }

    public int getQty(){
        return qty;
    }

    public int getHarga(){
        return harga;
    }

    public int getTotalharga(){
        return totalharga;
    }

    public int getNota(){
        return nota;
    }

}
