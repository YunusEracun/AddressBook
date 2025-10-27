package org.example;

import java.util.*;

public class AdresDefteriManager {

    private Map<String, Kisi> defter;
    public AdresDefteriManager() {
        this.defter = new HashMap<>();
    }

    public boolean kisiEkleme(Kisi yeniKisi) {
        String anahtar = yeniKisi.getePosta();

        if(defter.containsKey(anahtar)) {
            System.out.println("HATA: Bu E-Posta (" + anahtar + ")adresi zaten kayıtlı.");
            return false;
        }

        defter.put(anahtar, yeniKisi);
        System.out.println("BAŞARILI: "+ yeniKisi.getAd() + "kişisi adres defterine eklendi");
        return true;
    }
    public void tumKisileriListele() {
        if (defter.isEmpty()) {
            System.out.println("Adres defteri şu anda boş.");
            return;
        }

        System.out.println("/n--- Adres Defteri Listesi (" + defter.size() + " Kişi)---");

        for (Kisi kisi : defter.values()) {
            System.out.println(kisi);
        }
        System.out.println("-------------------------------");
    }

    public Collection<Kisi> kisiAra(String aramaMetni) {
        Collection<Kisi> bulunanlar = new java.util.ArrayList<>();
        String aramaKucuk = aramaMetni.toLowerCase();

        for (Kisi kisi : defter.values()) {

            if(kisi.getAd().toLowerCase().contains(aramaKucuk) ||
               kisi.getSoyad().toLowerCase().contains(aramaKucuk) ||
               kisi.getTelefonNumarasi().contains(aramaMetni))  {
               bulunanlar.add(kisi);
            }

        }
    return bulunanlar;
    }

    public boolean kisiSil(String ePosta) {
        if(defter.containsKey(ePosta)) {
            Kisi silinenKisi = defter.remove(ePosta);
            System.out.println("BAŞARILI: " + silinenKisi.getAd() + " kişisi silindi.");
            return true;
        }
        System.out.println("HATA: " + ePosta + " adresine sahip bir kişi bulunamadı.");
        return false;
    }
















}


