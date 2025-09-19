package Domein;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Reiziger {

    private int id;
    private String voorletter;
    private String tussenvoegsel;
    private String achternaam;
    private LocalDate geboortedatum;
    private Adres adres;

    private final List<OV_Chipkaart> ovChipkaarten = new ArrayList<>();
    public Reiziger(){

    }

    public Reiziger(int id, String voorletter, String tussenvoegsel, String achternaam,LocalDate geboortedatum){
        this.achternaam = achternaam;
        this.id= id;
        this.geboortedatum = geboortedatum;
        this.voorletter = voorletter;
        this.tussenvoegsel = tussenvoegsel;

    }

    //getter en setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getVoorletter() {
        return voorletter;
    }

    public void setVoorletter(String voorletter) {
        this.voorletter = voorletter;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public LocalDate getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(LocalDate geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    @Override
    public String toString() {
        String adresInfo;
        if (adres == null) {
            adresInfo = "geen adres";
        } else {
            adresInfo = adres.toString();
        }

        return "Reiziger{" +
                "id= " + id +
                ", voorletters= '" + voorletter + '\'' +
                ", tussenvoegsel= '" + tussenvoegsel + '\'' +
                ", achternaam= '" + achternaam + '\'' +
                ", geboortedatum= " + geboortedatum +
                ", adres= " + adresInfo +
                ", ovchipkaart "+ ovChipkaarten +
                '}';
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public Adres getAdres() {
        return adres;
    }

    public void addOvChipkaart(OV_Chipkaart kaart) {
        if (kaart == null) return;
        if (!ovChipkaarten.contains(kaart)) {
            ovChipkaarten.add(kaart);
        }
        if (kaart.getReiziger() != this) {
            kaart.setReiziger(this);
        }
    }

    public void removeOvChipkaart(OV_Chipkaart kaart) {
        if (kaart == null) return;
        ovChipkaarten.remove(kaart);
        if (kaart.getReiziger() == this) {
            kaart.setReiziger(null);
        }

    }

    public List<OV_Chipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }
}
