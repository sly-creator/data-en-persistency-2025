package Domein;

import java.time.LocalDate;
import java.util.ArrayList;
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

    public long getKaartNummer() { return kaartNummer; }

    public LocalDate getGeldigTot() {
        if (geldig_Tot == null) {
            return null;
        }
        return geldig_Tot; }

    public int getKlasse() { return klasse; }

    public void setKlasse(int klasse) { this.klasse = klasse; }

    public double getSaldo() {return saldo; }

    public void setSaldo(double saldo) { this.saldo = saldo; }

    public Reiziger getReiziger() { return reiziger; }

    public void setReiziger(Reiziger reiziger) {this.reiziger = reiziger; }


    public List<Product> getProducten() {return producten;}

    public void removeProduct(Product product) {
        if (product == null) return;
        if (producten.remove(product)) {
            product.removeOvChipkaart(this);
        }}

    @Override
    public String toString() {
        String reizigerNaam = (reiziger == null ? "null" : reiziger.getAchternaam());
        return "OVChipkaart{" +
                "kaartNummer=" + kaartNummer +
                ", geldigTot=" + geldig_Tot +
                ", klasse=" + klasse +
                ", saldo=" + saldo +
                ", reiziger=" + reizigerNaam +
                ", producten=" + producten +
                '}';
    }

}



