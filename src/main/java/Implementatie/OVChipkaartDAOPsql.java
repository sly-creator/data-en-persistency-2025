package Implementatie;

import Domein.OV_Chipkaart;
import Domein.Product;
import Domein.Reiziger;
import Interfaces.AdresDAO;
import Interfaces.OVChipkaartDAO;
import Interfaces.ProductDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private  Connection conn;
    private  AdresDAO adresDAO;
    private  ProductDAO productDAO;

    public OVChipkaartDAOPsql(Connection conn, AdresDAO adresDAO,ProductDAO productDAO) {
        this.conn = conn;
        this.adresDAO = adresDAO;
        this.productDAO = productDAO;
    }

    @Override
    public boolean save(OV_Chipkaart kaart) {
        String sql = "INSERT INTO ov_chipkaart(kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, kaart.getKaartNummer());
                if (kaart.getGeldigTot() != null) ps.setDate(2, Date.valueOf(kaart.getGeldigTot()));
                else ps.setNull(2, Types.DATE);

                ps.setInt(3, kaart.getKlasse());
                ps.setDouble(4, kaart.getSaldo());

                if (kaart.getReiziger() == null) throw new SQLException("reiziger is null bij save(OV_Chipkaart)");
                ps.setInt(5, kaart.getReiziger().getId());

                if (ps.executeUpdate() != 1) {
                    conn.rollback();
                    return false;
                }
            }

            if (!insertJoinRowsVoorKaart(kaart)) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            System.err.println("OVChipkaart save: " + e.getMessage());
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }


    @Override
    public boolean update(OV_Chipkaart kaart) {
        String sql = "UPDATE ov_chipkaart SET geldig_tot=?, klasse=?, saldo=?, reiziger_id=? WHERE kaart_nummer=?";
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                if (kaart.getGeldigTot() != null) ps.setDate(1, Date.valueOf(kaart.getGeldigTot()));
                else ps.setNull(1, Types.DATE);

                ps.setInt(2, kaart.getKlasse());
                ps.setDouble(3, kaart.getSaldo());

                if (kaart.getReiziger() == null) throw new SQLException("reiziger is null bij update(OV_Chipkaart)");
                ps.setInt(4, kaart.getReiziger().getId());

                ps.setLong(5, kaart.getKaartNummer());

                if (ps.executeUpdate() != 1) {
                    conn.rollback();
                    return false;
                }
            }

            if (!deleteJoinRowsVoorKaart(kaart.getKaartNummer())) {
                conn.rollback();
                return false;
            }
            if (!insertJoinRowsVoorKaart(kaart)) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            System.err.println("OVChipkaart update: " + e.getMessage());
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }


    @Override
    public boolean delete(OV_Chipkaart kaart) {
        String sql = "DELETE FROM ov_chipkaart WHERE kaart_nummer=?";
        try {
            conn.setAutoCommit(false);

            if (!deleteJoinRowsVoorKaart(kaart.getKaartNummer())) {
                conn.rollback();
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setLong(1, kaart.getKaartNummer());
                if (ps.executeUpdate() != 1) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ignored) {}
            System.err.println("OVChipkaart delete: " + e.getMessage());
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
    }


    @Override
    public OV_Chipkaart findByKaartNummer(long nummer) {
        String q = "SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart WHERE kaart_nummer=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setLong(1, nummer);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OV_Chipkaart kaart = new OV_Chipkaart(
                            rs.getInt("kaart_nummer"),
                            rs.getDate("geldig_tot") != null ? rs.getDate("geldig_tot").toLocalDate() : null,
                            rs.getInt("klasse"),
                            rs.getDouble("saldo")
                    );

                    int reizigerId = rs.getInt("reiziger_id");
                    Reiziger r = new Reiziger();
                    r.setId(reizigerId);
                    kaart.setReiziger(r);

                    laadEnKoppelProducten(kaart);
                    return kaart;
                }
            }
        } catch (SQLException e) {
            System.err.println("OV findByKaartNummer: " + e.getMessage());
        }
        return null;
    }



    @Override
    public List<OV_Chipkaart> findByReiziger(Reiziger reiziger) {
        List<OV_Chipkaart> list = new ArrayList<>();
        String q = "SELECT kaart_nummer, geldig_tot, klasse, saldo FROM ov_chipkaart WHERE reiziger_id=?";
        try (PreparedStatement ps = conn.prepareStatement(q)) {
            ps.setInt(1, reiziger.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date d = rs.getDate("geldig_tot");
                    OV_Chipkaart kaart = new OV_Chipkaart(
                            rs.getInt("kaart_nummer"),
                            d == null ? null : d.toLocalDate(),
                            rs.getInt("klasse"),
                            rs.getDouble("saldo")
                    );
                    kaart.setReiziger(reiziger);

                    laadEnKoppelProducten(kaart);
                    list.add(kaart);
                }
            }
        } catch (SQLException e) {
            System.err.println("OV findByReiziger: " + e.getMessage());
        }
        return list;
    }


    @Override
    public List<OV_Chipkaart> findAll() {
        List<OV_Chipkaart> list = new ArrayList<>();
        String q = "SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart";
        try (PreparedStatement ps = conn.prepareStatement(q);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Date d = rs.getDate("geldig_tot");
                OV_Chipkaart kaart = new OV_Chipkaart(
                        rs.getInt("kaart_nummer"),
                        d == null ? null : d.toLocalDate(),
                        rs.getInt("klasse"),
                        rs.getDouble("saldo")
                );

                int reizigerId = rs.getInt("reiziger_id");
                Reiziger r = new Reiziger();
                r.setId(reizigerId);
                kaart.setReiziger(r);

                laadEnKoppelProducten(kaart);
                list.add(kaart);
            }
        } catch (SQLException e) {
            System.err.println("OV findAll: " + e.getMessage());
        }
        return list;
    }


    private boolean deleteJoinRowsVoorKaart(long kaartNummer) throws SQLException {
        String sql = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, kaartNummer);
            ps.executeUpdate();
            return true;
        }
    }

    private boolean insertJoinRowsVoorKaart(OV_Chipkaart kaart) throws SQLException {
        if (kaart.getProducten() == null) return true;

        String sql = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer, status, last_update) " +
                "VALUES (?, ?, 'actief', CURRENT_DATE)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            List<Long> seen = new ArrayList<>();
            for (Product prod : kaart.getProducten()) {
                if (prod == null) continue;

                long productNummers = prod.getProductNummer();
                if (seen.contains(productNummers)) continue;
                seen.add(productNummers);

                ps.setLong(1, kaart.getKaartNummer());
                ps.setLong(2, productNummers);
                ps.executeUpdate();
            }
            return true;
        }

    }

    private void laadEnKoppelProducten(OV_Chipkaart kaart) {
        if (productDAO == null) return;

        List<Product> producten = productDAO.findByOVChipkaart(kaart.getKaartNummer());

        kaart.getProducten().clear();

        for (Product p : producten) {
            p.addOvChipkaart(kaart);
        }
    }



}
