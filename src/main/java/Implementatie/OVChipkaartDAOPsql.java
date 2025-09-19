package Implementatie;

import Domein.OV_Chipkaart;
import Domein.Reiziger;
import Interfaces.AdresDAO;
import Interfaces.OVChipkaartDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private final Connection conn;
    private  AdresDAO adresDAO;

    public OVChipkaartDAOPsql(Connection conn, AdresDAO adresDAO) {
        this.conn = conn;
    }
    @Override
    public boolean save(OV_Chipkaart kaart) {
        String sql = "INSERT INTO ov_chipkaart(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, kaart.getKaartNummer());
            ps.setDate(2, Date.valueOf(kaart.getGeldigTot()));
            ps.setInt(3, kaart.getKlasse());
            ps.setBigDecimal(4, kaart.getSaldo());
            if (kaart.getReiziger() == null) throw new SQLException("reiziger is null bij save(OV_Chipkaart),SAVE");
            ps.setInt(5, kaart.getReiziger().getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("OVChipkaart save: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(OV_Chipkaart kaart) {
        String sql = "UPDATE ov_chipkaart SET geldig_tot=?, klasse=?, saldo=?, reiziger_id=? " +
                "WHERE kaart_nummer=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(kaart.getGeldigTot()));
            ps.setInt(2, kaart.getKlasse());
            ps.setBigDecimal(3, kaart.getSaldo());
            if (kaart.getReiziger() == null) throw new SQLException("reiziger is null bij update(OV_Chipkaart),UPDATE");
            ps.setInt(4, kaart.getReiziger().getId());
            ps.setLong(5, kaart.getKaartNummer());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("OVChipkaart update: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(OV_Chipkaart k) {
        String sql = "DELETE FROM ov_chipkaart WHERE kaart_nummer=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, k.getKaartNummer());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("OVChipkaart delete: " + e.getMessage());
            return false;
        }
    }

    @Override
    public OV_Chipkaart findByKaartNummer(long nummer) {
        String q = "SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart WHERE kaart_nummer=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setLong(1, nummer);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                     new OV_Chipkaart(
                            rs.getInt("kaart_nummer"),
                            rs.getDate("geldig_tot").toLocalDate(),
                            rs.getInt("klasse"),
                            rs.getBigDecimal("saldo")
                    );
                }
            }
        } catch (SQLException e) { System.err.println("OV findByKaartNummer: " + e.getMessage()); }
        return null;
    }


    @Override
    public List<OV_Chipkaart> findByReiziger(Reiziger r) {
        List<OV_Chipkaart> list = new ArrayList<>();
        String q = "SELECT kaart_nummer, geldig_tot, klasse, saldo FROM ov_chipkaart WHERE reiziger_id=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, r.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OV_Chipkaart kaart = new OV_Chipkaart(
                            rs.getInt("kaart_nummer"),
                            rs.getDate("geldig_tot").toLocalDate(),
                            rs.getInt("klasse"),
                            rs.getBigDecimal("saldo")
                    );
                    kaart.setReiziger(r);
                    list.add(kaart);
                }
            }
        } catch (SQLException e) { System.err.println("OV findByReiziger: " + e.getMessage()); }
        return list;
    }
    @Override
    public List<OV_Chipkaart> findAll() {
        List<OV_Chipkaart> list = new ArrayList<>();
        String q = "SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart";
        try (PreparedStatement ps = conn.prepareStatement(q);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                OV_Chipkaart kaart = new OV_Chipkaart(
                        rs.getInt("kaart_nummer"),
                        rs.getDate("geldig_tot").toLocalDate(),
                        rs.getInt("klasse"),
                        rs.getBigDecimal("saldo")
                );
                list.add(kaart);
            }
        } catch (SQLException e) { System.err.println("OV findAll: " + e.getMessage()); }
        return list;
    }

}
