package org.example;
import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class AdresDefteriService {

    private static final int GECERLI_TELEFON_UZUNLUGU = 10;

    private static final String[] GECERLI_EMAIL_DOMAINLERI =
            { "@hotmail.com", "@gmail.com", "@outlook.com" };

    private Map<String, Kisi> defter;

    private static final String DOSYA_ADRES = "adres_defteri.json";

    public AdresDefteriService() {

        this.defter = new HashMap<>();
    }

    public Kisi hizliKisiAraEposta(String ePosta) {
        return defter.get(ePosta.toLowerCase());
    }

    private boolean epostaDogrula(String ePosta) {
        if (ePosta == null || ePosta.trim().isEmpty()) {
            return false;
        }
        String kucukHarfliEPosta = ePosta.toLowerCase();

        for (String domain : GECERLI_EMAIL_DOMAINLERI) {
            if (kucukHarfliEPosta.endsWith(domain)) {
                return true;
            }
        }
        return false;
    }

    private boolean telefonUzunluguDogrula(String telefonNumarasi) {
        if (telefonNumarasi == null) {
            return false;
        }
        // Girilen numaradan sadece rakamları alır
        String temizNumara = telefonNumarasi.replaceAll("[^0-9]", "");
        return temizNumara.length() == GECERLI_TELEFON_UZUNLUGU;
    }


    public boolean kisiEkleme(Kisi yeniKisi) {
        String anahtar = yeniKisi.getEPosta();

        if (!epostaDogrula(yeniKisi.getEPosta())) {
            System.out.println("HATA: E-posta adresi geçerli bir alana sahip değil. ("
                    + String.join(", ", GECERLI_EMAIL_DOMAINLERI) + " gibi alanlar kullanın)");
            return false;
        }

        if (!telefonUzunluguDogrula(yeniKisi.getTelefonNumarasi())) {
            System.out.println("HATA: Telefon numarası "
                    + GECERLI_TELEFON_UZUNLUGU + " haneli olmalıdır.");
            return false;
        }

        if (defter.containsKey(anahtar)) {
            System.out.println("HATA: Bu E-Posta (" + anahtar + ")adresi zaten kayıtlı.");
            return false;
        }

        defter.put(anahtar, yeniKisi);
        System.out.println("BAŞARILI: " + yeniKisi.getAd() + "kişisi adres defterine eklendi");
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

            if (kisi.getAd().toLowerCase().contains(aramaKucuk) ||
                    kisi.getSoyad().toLowerCase().contains(aramaKucuk) ||
                    kisi.getTelefonNumarasi().contains(aramaMetni)) {
                bulunanlar.add(kisi);
            }

        }
        return bulunanlar;
    }

    public boolean kisiSil(String ePosta) {
        if (defter.containsKey(ePosta)) {
            Kisi silinenKisi = defter.remove(ePosta);
            System.out.println("BAŞARILI: " + silinenKisi.getAd() + " kişisi silindi.");
            return true;
        }
        System.out.println("HATA: " + ePosta + " adresine sahip bir kişi bulunamadı.");
        return false;
    }

    public Collection<Kisi> kisiAraGenel(String arananDeger, String aramaTipi) {
        Collection<Kisi> bulunanKisiler = new java.util.ArrayList<>();
        String kucukAranan = arananDeger.toLowerCase();

        for (Kisi kisi : defter.values()) {
            boolean eslesti = false;

            // Hangi alana bakılacağını aramaTipi belirler.
            switch (aramaTipi.toLowerCase()) {
                case "ad":
                    if (kisi.getAd().toLowerCase().contains(kucukAranan)) {
                        eslesti = true;
                    }
                    break;
                case "soyisim":
                    if (kisi.getSoyad().toLowerCase().contains(kucukAranan)) {
                        eslesti = true;
                    }
                    break;
                case "telefon":
                    // Telefon aramalarında kısmi eşleşme kontrolü
                    if (kisi.getTelefonNumarasi().contains(kucukAranan)) {
                        eslesti = true;
                    }
                    break;
            }

            if (eslesti) {
                bulunanKisiler.add(kisi);
            }
        }
        return bulunanKisiler;
    }

    public Collection<Kisi> mukerrerAdSoyadBul() {
        // 1. Frekans Haritası: Anahtar (İsimSoyisim) -> Değer (O ismin kaç kez geçtiği)
        Map<String, Integer> frekansHaritasi = new HashMap<>();
        Collection<Kisi> tumMukerrerler = new java.util.ArrayList<>();

        // --- Aşama 1: Tüm isimlerin frekansını say ---
        for (Kisi kisi : defter.values()) {
            // Kontrol anahtarını oluştur: "yunusemre"
            String anahtar = (kisi.getAd() + kisi.getSoyad()).toLowerCase();

            // merge metodu: Anahtarı bul, sayıyı 1 artır. Yoksa 1 ile başlat.
            frekansHaritasi.merge(anahtar, 1, Integer::sum);
        }

        // --- Aşama 2: Frekansı 1'den büyük olanları topla ---
        for (Kisi kisi : defter.values()) {
            String anahtar = (kisi.getAd() + kisi.getSoyad()).toLowerCase();

            // Eğer bu ismin (anahtarın) frekansı 1'den büyükse (yani mükerrerse)...
            if (frekansHaritasi.get(anahtar) > 1) {
                // ...o kişiyi listeye ekle. Bu döngü her iki Yunus Emre'yi de ekler.
                tumMukerrerler.add(kisi);
            }
        }

        return tumMukerrerler;
    }


    public String defteriJsonaCevir() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        return gson.toJson(defter);
    }

    public boolean verileriKaydet() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter writer = new FileWriter(DOSYA_ADRES)) {
            gson.toJson(defter, writer);
            System.out.println("BAŞARILI: Adres defteri verileri dosyaya kaydedildi.");
            return true;
        } catch (IOException e) {
            System.out.println("HATA: Veri kaydı sırasında bir hata oluştu.");
            return false;
        }
    }

    public boolean verileriYukle() {
        File dosya = new File(DOSYA_ADRES);
        if (!dosya.exists()) {
            System.out.println("BİLGİ: Kayıt dosyası bulunamadı. Yeni boş defter oluşturuldu");
            return false;
        }
        try (FileReader reader = new FileReader(DOSYA_ADRES)) {
            Gson gson = new Gson();

            Type mapTipi = new TypeToken<HashMap<String, Kisi>>() {
            }.getType(); //deftere yazdırdıgmız veriler veri tiplerını kaybeder, geri kazandırmak ıcın bu methodu kullandık

            Map<String, Kisi> yuklenenDefter = gson.fromJson(reader, mapTipi);  //önceden deftere yazdırdıgımız json verileri tekrar belleğe gson olarak döndürüyoruz

            if (yuklenenDefter != null) {
                this.defter = yuklenenDefter;
                System.out.println("BAŞARILI: Adres defterine " + defter.size() + " kişi yüklendi.");
                return true;
            } else {
                System.out.println("BİLGİ: Kayıt dosyası boş veya okunamadı. Yeni boş defter kullanılıyor.");
                return false;
            }


        } catch (Exception e) {
            System.err.println("KRİTİK HATA: Veri yükleme sırasında bir okuma/format hatası oluştu. Dosya bozuk olabilir. Hata: " + e.getMessage());

            return false;
        }
    }
}



