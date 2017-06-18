package com.example.syukron.kedaigayatri;

public class stokbarang{
    String nama;
    int stok;

    public  stokbarang(){

    }

    public  stokbarang(String nama, int stok){
        this.nama = nama;
        this.stok = stok;
    }

    public String getNama(){
        return  nama;
    }

    public int getStok(){
        return stok;
    }
}
