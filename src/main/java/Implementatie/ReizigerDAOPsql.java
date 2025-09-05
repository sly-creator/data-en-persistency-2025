package Implementatie;

import Domein.Reiziger;
import Interfaces.ReizigerDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }

    //lange regels querys over meerdere regels
    //geen primary key veranderen
    @Override
    public boolean save(Reiziger reiziger) {
        try {
            String sql = "INSERT INTO reiziger(reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, reiziger.getId());
            pst.setString(2, reiziger.getVoorletter());
            pst.setString(3, reiziger.getTussenvoegsel());
            pst.setString(4, reiziger.getAchternaam());
            pst.setDate(5, Date.valueOf(reiziger.getGeboortedatum()));
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("e save " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        try {
            String sql = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, reiziger.getVoorletter());
            pst.setString(2, reiziger.getTussenvoegsel());
            pst.setString(3, reiziger.getAchternaam());
            pst.setDate(4, Date.valueOf(reiziger.getGeboortedatum()));
            pst.setInt(5, reiziger.getId());
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
                return new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum").toLocalDate()
                );
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
                reizigers.add(new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum").toLocalDate()
                ));
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
                reizigers.add(new Reiziger(
                        rs.getInt("reiziger_id"),
                        rs.getString("voorletters"),
                        rs.getString("tussenvoegsel"),
                        rs.getString("achternaam"),
                        rs.getDate("geboortedatum").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.err.println(" e findAll " + e.getMessage());
        }
        return reizigers;
    }
}
