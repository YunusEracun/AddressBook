package org.example;

import java.util.Scanner;
import java.util.Collection;

public class AdresDefteriUygulamasi {
1
    private static AdresDefteriManager manager = new AdresDefteriManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("--- Konsol Tabanlı Adres Defteri Uygulamasına Hoş Geldiniz ---");
        int secim;

        do {
            menuGoster();
            System.out.print("Seçiminizi yapın (1-5): ");
            if (scanner.hasNextInt()) {
                secim = scanner.nextInt();
                scanner.nextLine(); // Yeni satır karakterini tüket
                islemYap(secim);
            } else {
                System.out.println("Geçersiz giriş. Lütfen bir sayı girin.");
                scanner.nextLine(); // Hatalı girişi tüket
                secim = 0;
            }
        } while (secim != 5);

        System.out.println("Uygulamadan çıkılıyor. İyi günler!");
    }

    private static void menuGoster() {
        System.out.println("\n--- Menü ---");
        System.out.println("1. Yeni Kişi Ekle");
        System.out.println("2. Tüm Kişileri Listele");
        System.out.println("3. Kişi Ara (Ad/Soyad/Telefon)");
        System.out.println("4. Kişi Sil (E-posta ile)");
        System.out.println("5. Çıkış");
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
                kisiAra();
                break;
            case 4:
                kisiSil();
                break;
            case 5:
                // Çıkış main metodu tarafından yönetiliyor.
                break;
            default:
                System.out.println("Geçersiz seçim. Lütfen menüdeki rakamlardan birini girin.");
        }
    }

    private static void yeniKisiEkle() {
        System.out.println("\n--- Yeni Kişi Ekleme ---");
        System.out.print("Adı: ");
        String ad = scanner.nextLine();
        System.out.print("Soyadı: ");
        String soyad = scanner.nextLine();
        System.out.print("Telefon Numarası: ");
        String telefon = scanner.nextLine();
        System.out.print("E-posta Adresi (Benzersiz Olmalı): ");
        String ePosta = scanner.nextLine();

        Kisi yeniKisi = new Kisi(ad, soyad, telefon, ePosta);
        manager.kisiEkleme(yeniKisi);
    }

    private static void kisiAra() {
        System.out.println("\n--- Kişi Arama ---");
        System.out.print("Aranacak metni girin (Ad, Soyad veya Telefon): ");
        String aramaMetni = scanner.nextLine();

        Collection<Kisi> sonuclar = manager.kisiAra(aramaMetni);

        if (sonuclar.isEmpty()) {
            System.out.println("Arama kriterinize uygun kişi bulunamadı.");
        } else {
            System.out.println("--- Bulunan Kişiler (" + sonuclar.size() + ") ---");
            for (Kisi kisi : sonuclar) {
                System.out.println(kisi);
            }
            System.out.println("---------------------------------");
        }
    }

    private static void kisiSil() {
        System.out.println("\n--- Kişi Silme ---");
        System.out.print("Silmek istediğiniz kişinin E-posta adresini girin: ");
        String ePosta = scanner.nextLine();
        manager.kisiSil(ePosta);
    }
}