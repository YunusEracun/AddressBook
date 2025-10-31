package org.example;

public final class Constants {

    // Bu sınıfın nesnesinin oluşturulmasını engeller.
    private Constants() {}



    // --- KISITLAMALAR VE AYARLAR ---
    public static final int GECERLI_TELEFON_UZUNLUGU = 10;

    public static final String[] GECERLI_EMAIL_DOMAINLERI =
            { "@hotmail.com", "@gmail.com", "@outlook.com" };

    public static final String DOSYA_ADRES = "adres_defteri.json";



    public static final String MSG_HATA_EPOSTA_MUKERRER =
            "HATA: Bu E-Posta (%s) adresi zaten kayıtlı.";

    public static final String MSG_HATA_TELEFON_MUKERRER =
            "HATA: Bu telefon numarası (%s) zaten kayıtlı."; // (Yeni kuralın mesajı)

    public static final String MSG_HATA_EPOSTA_GECERSIZ =
            "HATA: E-posta adresi geçerli bir alana sahip değil. (%s gibi alanlar kullanın)";

    public static final String MSG_HATA_TELEFON_GECERSIZ =
            "HATA: Telefon numarası " + GECERLI_TELEFON_UZUNLUGU + " haneli olmalıdır.";

    public static final String MSG_BASARILI_EKLEME =
            "BAŞARILI: %s kişisi adres defterine eklendi."; // %s = Kişinin Adı

    public static final String MSG_BASARILI_SILME =
            "BAŞARILI: %s kişisi silindi."; // %s = Kişinin Adı

    public static final String MSG_BILGI_YUKLEME_YOK =
            "BİLGİ: Kayıt dosyası bulunamadı. Yeni boş defter oluşturuldu";

    public static final String MSG_BILGI_KAYIT_BULUNAMADI =
            "BİLGİ: Aranan kriterlere uygun kayıt bulunamadı.";

    public static final String MSG_HATA_KAYIT_SIRASINDA =
            "HATA: Veri kaydı sırasında bir hata oluştu. Hata: %s"; // %s = e.getMessage()
}