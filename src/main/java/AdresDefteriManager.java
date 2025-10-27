import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
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


















}


