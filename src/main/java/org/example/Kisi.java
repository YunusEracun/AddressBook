package org.example;

import java.util.Objects;
public class Kisi {

    private String ad;
    private String soyad;
    private String telefonNumarasi;
    private String ePosta;


    public Kisi(String ad, String soyad, String telefonNumarasi, String ePosta) {

        this.ad = ad;
        this.soyad = soyad;
        this.telefonNumarasi = telefonNumarasi;
        this.ePosta = ePosta;
    }

    public String getAd() {
        return ad;
    }
    public String getSoyad() {
        return soyad;
    }
    public String getTelefonNumarasi() {
        return telefonNumarasi;
    }
    public String getEPosta() {
        return ePosta;
    }

    @Override    // org.example.Kisi@7a81197d çıktısı bu şekilde olmasın diye override ettık
    public String toString(){
        return  "Ad: " + ad +
                ", Soyad: " + soyad +
                ", Telefon: " + telefonNumarasi +
                ", E-Posta: " + ePosta;
    }
    @Override // equals normalde aynı hafıza adreslerınde mı oldugunu kontrole eder burada override edip içeriklerini karşılaştırıyoruz
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kisi kisi = (Kisi) o;
        return Objects.equals(ePosta, kisi.ePosta);
    }
    @Override
    public int hashCode() { // kisinin hash değeri e posta adresıne bağlıdır.
        return Objects.hash(ePosta);
    }
}
