package org.example;

// Service katmanının, UI katmanına döndüreceği tüm olası sonuçları (başarı/hata) temsil eder.(enum)
public enum IslemSonucu {

    // BAŞARI MESAJLARI
    BASARILI_EKLEME,
    BASARILI_SILME,


    BILGI_YUKLEME_YOK,
    BILGI_KAYIT_BULUNAMADI,

    // HATA MESAJLARI
    HATA_EPOSTA_MUKERRER,
    HATA_TELEFON_MUKERRER,
    HATA_EPOSTA_GECERSIZ,
    HATA_TELEFON_GECERSIZ,
    HATA_KAYIT_SIRASINDA,
    HATA_ISIM_SOYISIM_GECERSIZ,

}