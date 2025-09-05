package Domein;

import java.time.LocalDate;
import java.util.Date;

public class Reiziger {

    private int id;
    private String voorletter;
    private String tussenvoegsel;
    private String achternaam;
    private LocalDate geboortedatum;

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
        return "Reiziger{" +
                "id= " + id +
                ", voorletters= '" + voorletter + '\'' +
                ", tussenvoegsel= '" + tussenvoegsel + '\'' +
                ", achternaam= '" + achternaam + '\'' +
                ", geboortedatum= " + geboortedatum +
                '}';
    }

}
