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

    public Collection<Kisi> mukerrerAdSoyadBul() {
        Set<String> gorulmusAdSoyadlar = new HashSet<>();
        Collection<Kisi> mukerrerKisiler = new java.util.ArrayList<>();

        for (Kisi kisi : defter.values()) {

            String anahtar = (kisi.getAd() + kisi.getSoyad()).toLowerCase();
            // burda Set kullanarak aynı isim soyisimdeki kişileri listeye 2. ekleyişimizde  bunları tespit edebilmek ıcın kllandk
            if (!gorulmusAdSoyadlar.add(anahtar)) {
                mukerrerKisiler.add(kisi);
            }

        }
        return mukerrerKisiler;
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



