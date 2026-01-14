package Domein;

public class Adres {

    private int id;

    private String  postcode;

    private String huisnummer;

    private String straat;


    private String woonplaats;


    private Reiziger reiziger;

    public Adres(){}

    public Adres(int id, String postcode, String huisnummer, String straat, String woonplaats ,Reiziger reiziger) {
        this.id = id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat =    straat;
        this.woonplaats = woonplaats;
        this.reiziger = reiziger;
    }


    public int getId()
    {return id;}
    public void setId(int id)
    {this.id = id;}

    public void setPostcode(String postcode)
    {this.postcode = postcode;}
    public String getPostcode()
    {return postcode;}

    public String getHuisnummer()
    {return huisnummer;}

    public String getStraat()
    {return straat;}


    public String getWoonplaats()
    {return woonplaats;}

    public Reiziger getReiziger()
    {return reiziger;}
    public void setReiziger(Reiziger reiziger)
    {this.reiziger = reiziger;}



    @Override
    public String toString() {
        String rId = (reiziger == null) ? "geen reiziger" : String.valueOf(reiziger.getId());
        return "Adres [" +
                "id=" + id +
                ", " + straat + " " + huisnummer +
                ", " + postcode + " " + woonplaats +
                ", reizigerId=" + rId +
                "]";
    }

}

