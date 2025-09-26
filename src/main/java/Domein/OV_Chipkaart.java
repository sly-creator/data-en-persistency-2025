package Domein;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OV_Chipkaart {
    private int kaartNummer;
    private LocalDate geldig_Tot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;

    public OV_Chipkaart() {}

    private List<Product> producten = new ArrayList<>();

    public OV_Chipkaart(int kaartNummer, LocalDate geldig_Tot, int klasse, double saldo) {
        this.kaartNummer = kaartNummer;
        this.geldig_Tot = geldig_Tot;
        this.klasse = klasse;
        this.saldo = saldo;
    }

    public OV_Chipkaart(int kaartNummer, LocalDate geldig_Tot, int klasse, double saldo, Reiziger reiziger) {
        this.kaartNummer = kaartNummer;
        this.geldig_Tot = geldig_Tot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }



    // getters & setters
    public long getKaartNummer() { return kaartNummer; }
    public void setKaartNummer(int kaartNummer) { this.kaartNummer = kaartNummer; }
    public LocalDate getGeldigTot() {
        if (geldig_Tot == null) {
            return null;
        }
        return geldig_Tot; }
    public void setGeldigTot(LocalDate geldig_Tot) {
        this.geldig_Tot = geldig_Tot; }

    public int getKlasse() { return klasse; }
    public void setKlasse(int klasse) { this.klasse = klasse; }

    public double getSaldo() {
        return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public Reiziger getReiziger() { return reiziger; }
    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger; }

    public void addProduct(Product product) {
        if (product == null) return;
        if (!producten.contains(product)) {
            producten.add(product);
            if (!product.getOvChipkaarten().contains(this)) {
                product.getOvChipkaarten().add(this);
            }
        }
    }

    public List<Product> getProducten() {
        return producten;
    }

    public void removeProduct(Product product) {
        if (product == null) return;
        if (producten.remove(product)) {
            product.removeOvChipkaart(this);
        }
    }

    @Override
    public String toString() {
        return "OVChipkaart{" +
                "kaartNummer=" + kaartNummer +
                ", geldigTot=" + geldig_Tot +
                ", klasse=" + klasse +
                ", saldo=" + saldo +
                ", reiziger" +reiziger.getAchternaam() +
                ". producten"+ getProducten() +
                '}';
    }
}



