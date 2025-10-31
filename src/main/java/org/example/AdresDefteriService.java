package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.stream.Collectors;

import static org.example.Constants.*;


public class AdresDefteriService {


    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Map<String, Kisi> defter;
    private final Set<String> kullanilanTelefonlar;

    public AdresDefteriService() {
        this.defter = new HashMap<>();
        this.kullanilanTelefonlar = new HashSet<>();
    }

    public Kisi hizliKisiAraEposta(String ePosta) {
        return defter.get(ePosta.toLowerCase());
    }

    public boolean kisiEkleme(Kisi yeniKisi) {
        String anahtar = yeniKisi.getEPosta().toLowerCase();
        String telefonNumarasi = yeniKisi.getTelefonNumarasi();
        String validationError = validateKisi(yeniKisi);

        if (validationError != null) {
            System.out.println(validationError);
            return false;
        }
        if (kullanilanTelefonlar.contains(telefonNumarasi)) {
            System.out.println("HATA: Bu telefon numarası (" + telefonNumarasi + ") zaten kayıtlı.");
            return false;
        }

        if (defter.containsKey(anahtar)) {
            System.out.println(String.format(ERR_MEVCUT, anahtar));
            return false;
        }

        defter.put(anahtar, yeniKisi);
        kullanilanTelefonlar.add(telefonNumarasi);
        System.out.println("BAŞARILI: " + yeniKisi.getAd() + " kişisi adres defterine eklendi.");
        return true;
    }

    public void tumKisileriListele() {
        if (defter.isEmpty()) {
            System.out.println("Adres defteri şu anda boş.");
            return;
        }

        System.out.println("\n--- Adres Defteri Listesi (" + defter.size() + " Kişi)---");

        defter.values().stream()
                .sorted(Comparator.comparing(Kisi::getAd)) // Ada göre sıralama
                .forEach(System.out::println);

        System.out.println("-------------------------------");
    }

    public boolean kisiSil(String ePosta) {
        String anahtar = ePosta.toLowerCase();
        if (defter.containsKey(anahtar)) {
            Kisi silinenKisi = defter.remove(anahtar);
            kullanilanTelefonlar.remove(silinenKisi.getTelefonNumarasi());
            System.out.println("BAŞARILI: " + silinenKisi.getAd() + " kişisi silindi.");
            return true;
        }
        System.out.println("HATA: " + ePosta + " adresine sahip bir kişi bulunamadı.");
        return false;
    }

    public Collection<Kisi> kisiAra(String arananDeger, String aramaTipi) {
        String kucukAranan = arananDeger.toLowerCase();
        return defter.values().stream()
                .filter(kisi -> {
                    switch (aramaTipi.toLowerCase()) {
                        case "ad":
                            return kisi.getAd().toLowerCase().contains(kucukAranan);
                        case "soyisim":
                            return kisi.getSoyad().toLowerCase().contains(kucukAranan);
                        case "telefon":
                            // Telefon aramalarında kısmi eşleşme kontrolü
                            return kisi.getTelefonNumarasi().contains(arananDeger);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public Collection<Kisi> mukerrerAdSoyadBul() {
        // GÜNCELLEME: Tekrar eden isimleri bulmak için daha kısa Stream API yaklaşımı.

        // İsim+Soyisim'i bir anahtar olarak alıp frekansını say
        Map<String, Long> frekansHaritasi = defter.values().stream()
                .collect(Collectors.groupingBy(
                        kisi -> (kisi.getAd() + kisi.getSoyad()).toLowerCase(),
                        Collectors.counting()
                ));

        // Frekansı 1'den büyük olan (mükerrer) kişileri filtrele
        return defter.values().stream()
                .filter(kisi -> {
                    String anahtar = (kisi.getAd() + kisi.getSoyad()).toLowerCase();
                    return frekansHaritasi.getOrDefault(anahtar, 0L) > 1;
                })
                .collect(Collectors.toList());
    }

    public String defteriJsonaCevir() {
        return gson.toJson(defter.values());
    }

    public boolean verileriKaydet() {
        try (FileWriter writer = new FileWriter(DOSYA_ADRES)) {
            gson.toJson(defter, writer);
            System.out.println("BAŞARILI: Adres defteri verileri dosyaya kaydedildi.");
            return true;
        } catch (IOException e) {
            System.err.println("HATA: Veri kaydı sırasında bir hata oluştu. Hata: " + e.getMessage());
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
            Type mapTipi = new TypeToken<HashMap<String, Kisi>>() {}.getType();
            Map<String, Kisi> yuklenenDefter = gson.fromJson(reader, mapTipi);

            if (yuklenenDefter != null) {
                this.defter = yuklenenDefter;
                this.kullanilanTelefonlar.clear();
                for (Kisi kisi : defter.values()) {
                    this.kullanilanTelefonlar.add(kisi.getTelefonNumarasi());
                }
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

    private String validateKisi(Kisi kisi) {
        String eposta = kisi.getEPosta();
        String telefon = kisi.getTelefonNumarasi();

        if (!epostaDogrula(eposta)) {
            // Hata mesajını formatlayarak tek bir yerde tutma
            String gecerliDomainler = String.join(", ", GECERLI_EMAIL_DOMAINLERI);
            return String.format(ERR_EPOSTA, gecerliDomainler);
        }

        if (!telefonUzunluguDogrula(telefon)) {
            return ERR_TELEFON;
        }
        return null;
    }

    private boolean epostaDogrula(String ePosta) {
        if (ePosta == null || (ePosta = ePosta.trim()).isEmpty()) {
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
        String temizNumara = telefonNumarasi.replaceAll("[^0-9]", "");
        return temizNumara.length() == GECERLI_TELEFON_UZUNLUGU;
    }
}