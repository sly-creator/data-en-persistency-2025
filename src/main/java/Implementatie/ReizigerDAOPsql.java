package Implementatie;

import Domein.Adres;
import Domein.OV_Chipkaart;
import Domein.Reiziger;
import Interfaces.AdresDAO;
import Interfaces.OVChipkaartDAO;
import Interfaces.ReizigerDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;
    private AdresDAO adresDAO;
    private OVChipkaartDAO ovChipkaartDAO;

    public ReizigerDAOPsql(Connection conn,AdresDAO adresDAO) {
        this.conn = conn;
        this.adresDAO = adresDAO;
    }

    public ReizigerDAOPsql(Connection conn, AdresDAO adresDAO, OVChipkaartDAO ovChipkaartDAO) {
        this.conn = conn;
        this.adresDAO = adresDAO;
        this.ovChipkaartDAO = ovChipkaartDAO;
    }


    //lange regels querys over meerdere regels
    //geen primary key veranderen
    @Override
    public boolean save(Reiziger reiziger) {
//        Adres huidigAdres = adresDAO.findByReiziger(reiziger);
//        Adres nieuwAdres  = reiziger.getAdres();

        try {
            String sql = "INSERT INTO reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, reiziger.getId());
            pst.setString(2, reiziger.getVoorletter());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, Date.valueOf(reiziger.getGeboortedatum()));

            if (reiziger.getAdres() != null) {
                Adres adres = reiziger.getAdres();
                adres.setReiziger(reiziger);
                adresDAO.update(adres);
                for (OV_Chipkaart kaart : reiziger.getOvChipkaarten()) {
                    kaart.setReiziger(reiziger);
                    ovChipkaartDAO.update(kaart);
                }

//            if (nieuwAdres != null) {
//                nieuwAdres.setReiziger(reiziger);
//                adresDAO.save(nieuwAdres);
//                for (OV_Chipkaart kaart : reiziger.getOvChipkaarten()) {
//                    kaart.setReiziger(reiziger);
//                    ovChipkaartDAO.update(kaart);
//                }
            }
                return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("e save " + e.getMessage());
            return false;
        }
    }



    @Override
    public boolean update(Reiziger reiziger) {
        Adres huidigAdres = adresDAO.findByReiziger(reiziger);
        Adres nieuwAdres  = reiziger.getAdres();

        try {
            String sql = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, reiziger.getVoorletter());
            pst.setString(2, reiziger.getTussenvoegsel());
            pst.setString(3, reiziger.getAchternaam());
            pst.setDate(4, Date.valueOf(reiziger.getGeboortedatum()));
            pst.setInt(5, reiziger.getId());
            if (nieuwAdres == null && huidigAdres != null) {
                adresDAO.delete(huidigAdres);
            }
            if (nieuwAdres != null) {
                nieuwAdres.setReiziger(reiziger);
                adresDAO.save(nieuwAdres);
                for (OV_Chipkaart kaart : reiziger.getOvChipkaarten()) {
                    kaart.setReiziger(reiziger);
                    ovChipkaartDAO.update(kaart);
                }

            }
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("e update " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger) {

        try {
            String sql = "DELETE FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, reiziger.getId());

            Adres adres = adresDAO.findByReiziger(reiziger);
            if (adres != null) {
                adresDAO.delete(adres);
            }
            for (OV_Chipkaart kaart : reiziger.getOvChipkaarten()) {
                kaart.setReiziger(reiziger);
                ovChipkaartDAO.delete(kaart);
            }
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("e delete " + e.getMessage());
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) {
        try {
            String sql = "SELECT * FROM reiziger WHERE reiziger_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Reiziger reiziger = new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum").toLocalDate()
                );
                reiziger.setAdres(adresDAO.findByReiziger(reiziger));
                for (OV_Chipkaart kaart : ovChipkaartDAO.findByReiziger(reiziger)) {
                    reiziger.addOvChipkaart(kaart);
                }
                return reiziger;

            }
        } catch (SQLException e) {
            System.err.println("e findById " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(LocalDate geboortedatum) {
        List<Reiziger> reizigers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM reiziger WHERE geboortedatum = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDate(1, Date.valueOf(geboortedatum));
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Reiziger reiziger = new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum").toLocalDate()
                );
                reiziger.setAdres(adresDAO.findByReiziger(reiziger));
                reizigers.add(reiziger);
                for (OV_Chipkaart kaart : ovChipkaartDAO.findByReiziger(reiziger)) {
                    reiziger.addOvChipkaart(kaart);
                }
            }
        } catch (SQLException e) {
            System.err.println(" e findByGbdatum " + e.getMessage());
        }
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() {
        List<Reiziger> reizigers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM reiziger";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Reiziger reiziger = new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum").toLocalDate()
                );
                reiziger.setAdres(adresDAO.findByReiziger(reiziger));
                reizigers.add(reiziger);
                for (OV_Chipkaart kaart : ovChipkaartDAO.findByReiziger(reiziger)) {
                    reiziger.addOvChipkaart(kaart);
                }
            }
        } catch (SQLException e) {
            System.err.println(" e findAll " + e.getMessage());
        }
        return reizigers;
    }

    public void setAdresDao(AdresDAO adresDAO){
        this.adresDAO = adresDAO;
    }
}
