package org.example;

import java.util.Scanner;
import java.util.Collection;

public class AdresDefteriUygulamasi {

    private static AdresDefteriService manager = new AdresDefteriService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        manager.verileriYukle();
        System.out.println("--- Konsol Tabanlı Adres Defteri Uygulamasına Hoş Geldiniz ---");
        int secim;

        do {
            menuGoster();
            System.out.print("Seçiminizi yapın (1-8): ");
            if (scanner.hasNextInt()) {
                secim = scanner.nextInt();
                scanner.nextLine();
                islemYap(secim);
            } else {
                System.out.println("Geçersiz giriş. Lütfen bir sayı girin.");
                scanner.nextLine();

            }
        }   while (true);


    }

    private static void menuGoster() {
        System.out.println("\n--- Menü ---");
        System.out.println("1. Yeni Kişi Ekle");
        System.out.println("2. Tüm Kişileri Listele");
        System.out.println("3. Kişi Ara (Ad/Soyad/Telefon)");
        System.out.println("4. Kişi Sil (E-posta ile)");
        System.out.println("5. Mükerrer Ad/Soyadları Bul (Veri Analizi)");
        System.out.println("6. Verileri JSON Olarak Konsola Yazdır");
        System.out.println("7. GÜVENLİ ÇIKIŞ ve KAYDET (Zorunlu)");
        System.out.println("------------");
    }

    private static void islemYap(int secim) {
        switch (secim) {
            case 1:
                yeniKisiEkle();
                break;
            case 2:
                manager.tumKisileriListele();
                break;
            case 3:
                kisiAramaMenu();
                break;
            case 4:
                kisiSil();
                break;
            case 5:
                Collection<Kisi> mukerrerler = manager.mukerrerAdSoyadBul();

                System.out.println("\n--- MÜKERRER KAYITLAR ---");
                if (mukerrerler.isEmpty()) {
                    System.out.println("Mükerrer kayıt bulunamadı.");
                } else {
                    for (Kisi k : mukerrerler) {
                        System.out.println(k);
                    }
                }
                break;
            case 6:
                String jsonCikti = manager.defteriJsonaCevir();
                System.out.println("\n--- JSON ÇIKTISI ---");
                System.out.println(jsonCikti);
                break;
            case 7:
                System.out.println("Uygulamadan çıkılıyor. Veriler kaydediliyor...");
                manager.verileriKaydet();
                System.exit(0);
            default:
                System.out.println("Geçersiz seçim. Lütfen menüdeki rakamlardan birini girin.");
        }
    }

    private static void yeniKisiEkle() {
        System.out.println("/n--- Yeni Kişi Ekleme ---");
        System.out.print("Adı: ");
        String ad = scanner.nextLine();
        System.out.print("Soyadı: ");
        String soyad = scanner.nextLine();
        System.out.print("Telefon Numarası: ");
        String telefon = scanner.nextLine();
        System.out.print("E-posta Adresi: ");
        String ePosta = scanner.nextLine();

        Kisi yeniKisi = new Kisi(ad, soyad, telefon, ePosta);
        manager.kisiEkleme(yeniKisi);
    }

    private static void kisiAramaMenu() {
        int altSecim = -1;
        System.out.println("\n--- ARAMA TİPİ SEÇİMİ ---");
        System.out.println("1. İsim ile Ara");
        System.out.println("2. Soyisim ile Ara");
        System.out.println("3. Telefon Numarası ile Ara");
        System.out.println("4. E-Posta ile Hızlı Ara");
        System.out.println("0. Geri");
        System.out.print("Seçiminiz: ");

        if (scanner.hasNextInt()) {
            altSecim = scanner.nextInt();
            scanner.nextLine();
        } else {
            System.out.println("Geçersiz giriş. Lütfen bir rakam girin.");
            scanner.nextLine();
            return;
        }

        switch (altSecim) {
            case 1:
                kisiAraVeYazdir("ad");
                break;
            case 2:
                kisiAraVeYazdir("soyisim");
                break;
            case 3:
                kisiAraVeYazdir("telefon");
                break;
            case 4:
                hizliKisiAraEpostaVeYazdir(); // O(1) arama
                break;
            case 0:
                System.out.println("Ana menüye dönülüyor.");
                break;
            default:
                System.out.println("Geçersiz seçim.");
        }
    }
    private static void kisiAraVeYazdir(String aramaTipi) {
        String arananDeger;
        System.out.print("Aranacak " + aramaTipi + " değerini girin: ");
        arananDeger = scanner.nextLine();
        Collection<Kisi> sonuclar = manager.kisiAraGenel(arananDeger, aramaTipi);

        System.out.println("\n--- ARAMA SONUÇLARI (" + aramaTipi.toUpperCase() + ") ---");
        if (sonuclar.isEmpty()) {
            System.out.println("'" + arananDeger + "' değerine eşleşen kayıt bulunamadı.");
        } else {
            System.out.println(sonuclar.size() + " adet kayıt bulundu:");
            for (Kisi kisi : sonuclar) {
                System.out.println(kisi);
            }
        }
        System.out.println("----------------------------------------");
    }

    private static void hizliKisiAraEpostaVeYazdir() {
        System.out.print("Aranacak E-Posta Adresini Girin: ");
        String ePosta = scanner.nextLine();


        Kisi bulunanKisi = manager.hizliKisiAraEposta(ePosta);

        System.out.println("\n--- E-POSTA ARAMA SONUCU ---");
        if (bulunanKisi != null) {
            System.out.println("Kişi Başarıyla Bulundu:");
            System.out.println(bulunanKisi);
        } else {
            System.out.println("HATA: '" + ePosta + "' e-posta adresiyle kayıt bulunamadı.");
        }
        System.out.println("----------------------------------");
    }


    private static void kisiSil() {
        System.out.println("\n--- Kişi Silme ---");
        System.out.print("Silmek istediğiniz kişinin E-posta adresini girin: ");
        String ePosta = scanner.nextLine();
        manager.kisiSil(ePosta);
    }
}