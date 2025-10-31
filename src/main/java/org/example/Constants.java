package org.example;

public final class Constants {

    // Bu sınıfın nesnesinin oluşturulmasını engeller.
    private Constants() {}



    // --- KISITLAMALAR VE AYARLAR ---
    public static final int GECERLI_TELEFON_UZUNLUGU = 10;

    public static final String[] GECERLI_EMAIL_DOMAINLERI =
            { "@hotmail.com", "@gmail.com", "@outlook.com" };

    public static final String DOSYA_ADRES = "adres_defteri.json";



    // --- HATA VE BİLGİ MESAJLARI ---

    public static final String ERR_EPOSTA =
            "HATA: E-posta adresi geçerli bir alana sahip değil. (%s gibi alanlar kullanın)";

    public static final String ERR_TELEFON =
            "HATA: Telefon numarası " + GECERLI_TELEFON_UZUNLUGU + " haneli olmalıdır.";

    public static final String ERR_MEVCUT =
            "HATA: Bu E-Posta (%s) adresi zaten kayıtlı.";

}