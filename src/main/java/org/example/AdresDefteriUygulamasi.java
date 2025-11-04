package org.example;

import java.util.Collection;
import java.util.Scanner;

public class AdresDefteriUygulamasi {

    private static final AdresDefteriService manager = new AdresDefteriService();
    private static final Scanner scanner = new Scanner(System.in);
    private static final InputManager inputManager = InputManager.getInstance(scanner);


    public static void main(String[] args) {
        manager.verileriYukle();
        System.out.println("--- Konsol Tabanlı Adres Defteri Uygulamasına Hoş Geldiniz ---");
        while (true) {
            menuGoster();
            int secim = inputManager.getIntInput("Seçiminizi yapın (1-7): ");
            islemYap(secim);
        }
    }

    private static void menuGoster() {
        System.out.println("\n--- Menü ---");
        System.out.println("1. Yeni Kişi Ekle");
        System.out.println("2. Tüm Kişileri Listele");
        System.out.println("3. Kişi Ara");
        System.out.println("4. Kişi Sil (E-posta ile)");
        System.out.println("5. Mükerrer Ad/Soyadları Bul");
        System.out.println("6. Verileri JSON Olarak Konsola Yazdır");
        System.out.println("7. GÜVENLİ ÇIKIŞ ve KAYDET");
        System.out.println("------------");
    }

    private static void islemYap(int secim) {
        switch (secim) {
            case 1 -> yeniKisiEkle();
            case 2 -> manager.tumKisileriListele();
            case 3 -> kisiAramaMenu();
            case 4 -> kisiSil();
            case 5 -> mukerrerKontrol();
            case 6 -> jsonCiktiGoster();
            case 7 -> {
                System.out.println("Uygulamadan çıkılıyor. Veriler kaydediliyor...");
                manager.verileriKaydet();
                System.exit(0);
            }
            default -> System.out.println("Geçersiz seçim. Lütfen menüdeki rakamlardan birini girin.");
        }
    }

    private static void yeniKisiEkle() {
        System.out.println("\n--- Yeni Kişi Ekleme ---");


        String ad = inputManager.getStringInput("Adı: ");
        String soyad = inputManager.getStringInput("Soyadı: ");
        String telefon = inputManager.getStringInput("Telefon Numarası: ");
        String ePosta = inputManager.getStringInput("E-posta Adresi: ");

        Kisi yeniKisi = new Kisi(ad, soyad, telefon, ePosta);


        IslemSonucu sonuc = manager.kisiEkleme(yeniKisi);


        switch (sonuc) {
            case BASARILI_EKLEME:
                mesajYazdir(sonuc, yeniKisi.getAd());
                break;

            case HATA_EPOSTA_GECERSIZ:
                String domainler = String.join(", ", Constants.GECERLI_EMAIL_DOMAINLERI);
                mesajYazdir(sonuc, domainler);
                break;

            case HATA_TELEFON_GECERSIZ:
                mesajYazdir(sonuc);
                break;

            case HATA_EPOSTA_MUKERRER:
                mesajYazdir(sonuc, yeniKisi.getEPosta());
                break;

            case HATA_TELEFON_MUKERRER:
                mesajYazdir(sonuc, yeniKisi.getTelefonNumarasi());
                break;

            case HATA_ISIM_SOYISIM_GECERSIZ:
                mesajYazdir(sonuc);
                break;

            default:
                System.err.println("KRİTİK HATA: Beklenmeyen işlem sonucu: " + sonuc);
                break;
        }
    }

    private static void kisiAramaMenu() {
        System.out.println("\n--- ARAMA TİPİ SEÇİMİ ---");
        System.out.println("1. İsim ile Ara");
        System.out.println("2. Soyisim ile Ara");
        System.out.println("3. Telefon Numarası ile Ara");
        System.out.println("4. E-Posta ile Hızlı Ara");
        System.out.println("0. Geri");

        int altSecim = inputManager.getIntInput("Seçiminiz (0-4): ");

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

    private static void kisiSil() {
        System.out.println("\n--- Kişi Silme ---");
        String ePosta = inputManager.getStringInput("Silmek istediğiniz kişinin E-posta adresini girin: ");
        manager.kisiSil(ePosta);
    }

    private static void mukerrerKontrol() {
        Collection<Kisi> mukerrerler = manager.mukerrerAdSoyadBul();
        System.out.println("\n--- MÜKERRER KAYITLAR ---");
        if (mukerrerler.isEmpty()) {
            System.out.println("Mükerrer kayıt bulunamadı.");
        } else {
            System.out.println(mukerrerler.size() + " adet mükerrer kayıt bulundu:");
            for (Kisi k : mukerrerler) {
                System.out.println(k);
            }
        }
    }

    private static void jsonCiktiGoster() {
        String jsonCikti = manager.defteriJsonaCevir();
        System.out.println("\n--- JSON ÇIKTISI ---");
        System.out.println(jsonCikti);
        System.out.println("--------------------");
    }

    private static void kisiAraVeYazdir(String aramaTipi) {
        String arananDeger = inputManager.getStringInput("Aranacak " + aramaTipi + " değerini girin: ");
        Collection<Kisi> sonuclar = manager.kisiAra(arananDeger, aramaTipi);

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
        String ePosta = inputManager.getStringInput("Aranacak E-Posta Adresini Girin: ");

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



    private static void mesajYazdir(IslemSonucu sonuc, String... args) {
        String mesajSablonu = "";

        switch (sonuc) {
            case BASARILI_EKLEME:
                mesajSablonu = Constants.MSG_BASARILI_EKLEME;
                break;
            case HATA_EPOSTA_GECERSIZ:
                mesajSablonu = Constants.MSG_HATA_EPOSTA_GECERSIZ;
                break;
            case HATA_TELEFON_GECERSIZ:
                mesajSablonu = Constants.MSG_HATA_TELEFON_GECERSIZ;
                break;
            case HATA_EPOSTA_MUKERRER:
                mesajSablonu = Constants.MSG_HATA_EPOSTA_MUKERRER;
                break;
            case HATA_TELEFON_MUKERRER:
                mesajSablonu = Constants.MSG_HATA_TELEFON_MUKERRER;
                break;
            case HATA_ISIM_SOYISIM_GECERSIZ:
                mesajSablonu = Constants.MSG_HATA_ISIM_SOYISIM_GECERSIZ;
                break;
            case BILGI_YUKLEME_YOK:
                mesajSablonu = Constants.MSG_BILGI_YUKLEME_YOK;
                break;
            case HATA_KAYIT_SIRASINDA:
                mesajSablonu = Constants.MSG_HATA_KAYIT_SIRASINDA;
                break;
            case BASARILI_SILME:
                mesajSablonu = Constants.MSG_BASARILI_SILME;
                break;
            case BILGI_KAYIT_BULUNAMADI:
                mesajSablonu = Constants.MSG_BILGI_KAYIT_BULUNAMADI;
                break;
            default:
                mesajSablonu = "HATA: İşlem sonucu bilinmiyor (" + sonuc.name() + ")";
                break;
        }
        System.out.println(String.format(mesajSablonu, args));
    }

}