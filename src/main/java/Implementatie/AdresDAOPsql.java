package Implementatie;

import Domein.Adres;
import Domein.Reiziger;
import Interfaces.AdresDAO;
import Interfaces.ReizigerDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdresDAOPsql implements AdresDAO {
    private Connection conn;
    private ReizigerDAO reizigerDAO;
    public AdresDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Adres adres) {
        try {
            String sql = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, adres.getId());
            pst.setString(2, adres.getPostcode());
            pst.setString(3, adres.getHuisnummer());
            pst.setString(4, adres.getStraat());
            pst.setString(5, adres.getWoonplaats());
            pst.setInt(6, adres.getReiziger().getId());
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(" e save Adres " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        try {
            String sql = "UPDATE adres SET postcode=?, huisnummer=?, straat=?, woonplaats=?, reiziger_id=? WHERE adres_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, adres.getPostcode());
            pst.setString(2, adres.getHuisnummer());
            pst.setString(3, adres.getStraat());
            pst.setString(4, adres.getWoonplaats());
            pst.setInt(5, adres.getReiziger().getId());
            pst.setInt(6, adres.getId());
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(" e update Adres " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        try {
            String sql = "DELETE FROM adres WHERE adres_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, adres.getId());
            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(" e delete Adres " + e.getMessage());
            return false;
        }
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        if (reiziger == null) return null;
        String sql = "SELECT * FROM adres WHERE reiziger_id=?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, reiziger.getId());
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int adresId     = rs.getInt("adres_id");
                    String pc       = rs.getString("postcode");
                    String huisnr   = rs.getString("huisnummer");
                    String straat   = rs.getString("straat");
                    String woonpl   = rs.getString("woonplaats");
                    return new Adres(adresId, pc, huisnr, straat, woonpl, reiziger);
                }
            }
        } catch (SQLException e) {
            System.err.println(" e findByReiziger  " + e.getMessage());
        }
        return null;
    }
    @Override
    public List<Adres> findAll() {
        List<Reiziger> reizigers = reizigerDAO.findAll();
        List<Adres> result = new ArrayList<>();

        for (Reiziger reiziger : reizigers) {
            Adres adres = reiziger.getAdres();
            if (adres != null) {
                if (adres.getReiziger() == null) {
                    adres.setReiziger(reiziger);
                }
                result.add(adres);
            }
        }
        return result;
    }

}