package Domein;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private long productNummer;
    private String naam;
    private String beschrijving;
    private double prijs;

    public Product() {}

    public Product(long productNummer, String naam, String beschrijving, double prijs) {
        this.productNummer = productNummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    private List<OV_Chipkaart> ovChipkaarten = new ArrayList<>();

    public long getProductNummer() {
        return productNummer;
    }

    public void setProductNummer(long productNummer) {
        this.productNummer = productNummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }
    public List<OV_Chipkaart> getOvChipkaarten() { return ovChipkaarten; }

    public void addOvChipkaart(OV_Chipkaart kaart) {
        if (kaart == null) return;
        if (!ovChipkaarten.contains(kaart)) {
            ovChipkaarten.add(kaart);
            if (!kaart.getProducten().contains(this)) {
                kaart.getProducten().add(this);
            }
        }
    }

    public void removeOvChipkaart(OV_Chipkaart kaart) {
        if (kaart == null) return;
        if (ovChipkaarten.remove(kaart)) {
            kaart.removeProduct(this);
        }
    }
    @Override
    public String toString() {
        return "Product{" +
                "productNummer=" + productNummer +
                ", naam='" + naam + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                ", prijs=" + prijs +
                '}';
    }
}
