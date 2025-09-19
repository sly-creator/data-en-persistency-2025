package Domein;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Date;

public class OV_Chipkaart {
    private int kaartNummer;
    private LocalDate geldig_Tot;
    private int klasse;
    private BigDecimal saldo;
    private Reiziger reiziger;

    public OV_Chipkaart() {}

    public OV_Chipkaart(int kaartNummer, LocalDate geldig_Tot, int klasse, BigDecimal saldo) {
        this.kaartNummer = kaartNummer;
        this.geldig_Tot = geldig_Tot;
        this.klasse = klasse;
        this.saldo = saldo;
    }

    public OV_Chipkaart(int kaartNummer, LocalDate geldig_Tot, int klasse, BigDecimal saldo, Reiziger reiziger) {
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

    public BigDecimal getSaldo() {
        if(saldo == null){
            return saldo = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public Reiziger getReiziger() { return reiziger; }
    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger; }

    @Override
    public String toString() {
        return "OVChipkaart{" +
                "kaartNummer=" + kaartNummer +
                ", geldigTot=" + geldig_Tot +
                ", klasse=" + klasse +
                ", saldo=" + saldo +
                ", reiziger" +reiziger.getAchternaam() +
                '}';
    }
}



