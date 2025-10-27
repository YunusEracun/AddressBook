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
    public String getePosta() {
        return ePosta;
    }

    @Override    //daha okunaklı olması için bu şekilde yazdım.
    public String toString(){
        return  "Ad: " + ad +
                ", Soyad: " + soyad +
                ", Telefon: " + telefonNumarasi +
                ", E-Posta: " + ePosta;
    }
    @Override // Burda e posta adreslerinin eşit olup olmadıgını aynı hafıza adresını kontrol etmek yerıne e-posta adresi eşitliğini sorguladık
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kisi kisi = (Kisi) o;
        return Objects.equals(ePosta, kisi.ePosta);
    }
    @Override
    public int hashCode() {
        return Objects.hash(ePosta);
    }
}
