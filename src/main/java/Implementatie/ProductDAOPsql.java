package Implementatie;

import Domein.OV_Chipkaart;
import Domein.Product;
import Domein.Reiziger;
import Interfaces.ProductDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private Connection conn;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Product product) {
        String sql = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";
        String Join = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer, status, last_update) " +
                "VALUES (?, ?, 'actief', CURRENT_DATE)";
        try {

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, product.getProductNummer());
            pst.setString(2, product.getNaam());
            pst.setString(3, product.getBeschrijving());
            pst.setDouble(4, product.getPrijs());
            pst.executeUpdate();
        }

        try (PreparedStatement pstJ = conn.prepareStatement(Join)) {
            for (OV_Chipkaart kaart : product.getOvChipkaarten()) {
                pstJ.setLong(1, kaart.getKaartNummer());
                pstJ.setLong(2, product.getProductNummer());
                try {
                    pstJ.executeUpdate();
                }  catch (SQLException e) {
                    System.err.println("Product save join: " + e.getMessage());
                    return false;
                }
            }
        }
        return true;

        }  catch (SQLException e) {
            System.err.println("Product save: " + e.getMessage());
            return false;
        }

    }

    @Override
    public boolean update(Product product) {
        String sql = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";
        String sqld = "DELETE FROM ov_chipkaart_product WHERE product_nummer=?";
        String sqli = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)";
        try {
            try (PreparedStatement u = conn.prepareStatement(sql)) {
                u.setString(1, product.getNaam());
                u.setString(2, product.getBeschrijving());
                u.setDouble(3, product.getPrijs());
                u.setLong(4, product.getProductNummer());
                if (u.executeUpdate() != 1) return false;
            }
            try (PreparedStatement pst = conn.prepareStatement(sqld)) {
                pst.setLong(1, product.getProductNummer());
                pst.executeUpdate();
            }
            try (PreparedStatement pst1 = conn.prepareStatement(sqli)) {
                for (OV_Chipkaart kaart : product.getOvChipkaarten()) {
                    pst1.setLong(1, kaart.getKaartNummer());
                    pst1.setLong(2, product.getProductNummer());
                    pst1.executeUpdate();
                }
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Product update: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Product product) {
        String sql = "DELETE FROM product WHERE product_nummer = ?";
        String join = "DELETE FROM ov_chipkaart_product WHERE product_nummer=?";
       try {

        try (PreparedStatement pst = conn.prepareStatement(join)) {
            pst.setLong(1, product.getProductNummer());
            pst.executeUpdate();
        }
           try (PreparedStatement pst1 = conn.prepareStatement(sql)) {
               pst1.setLong(1, product.getProductNummer());
               return pst1.executeUpdate() == 1;
           }

        } catch (SQLException e) {
            System.err.println("Product delete: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(long kaartNummer) {
        String sql = """
        SELECT p.product_nummer, p.naam, p.beschrijving, p.prijs
        FROM product p
        JOIN ov_chipkaart_product op
          ON op.product_nummer = p.product_nummer
        WHERE op.kaart_nummer = ?
        ORDER BY p.product_nummer
         """;

        List<Product> result = new ArrayList<>();
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setLong(1, kaartNummer);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    result.add(new Product(
                            rs.getLong("product_nummer"),
                            rs.getString("naam"),
                            rs.getString("beschrijving"),
                            rs.getDouble("prijs")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Product findByOVChipkaart: " + e.getMessage());
        }
        return result;
    }

    @Override
    public List<Product> findAll() {
        String sql = """
        SELECT prod.product_nummer, prod.naam, prod.beschrijving, prod.prijs, ov.kaart_nummer, ov.geldig_tot, ov.klasse, ov.saldo,
               reiz.reiziger_id, reiz.voorletters, reiz.tussenvoegsel, reiz.achternaam, reiz.geboortedatum
        FROM product prod
        LEFT JOIN ov_chipkaart_product op ON prod.product_nummer = op.product_nummer
        LEFT JOIN ov_chipkaart ov ON ov.kaart_nummer = op.kaart_nummer
        LEFT JOIN reiziger reiz ON reiz.reiziger_id = ov.reiziger_id
        ORDER BY prod.product_nummer
        """;

        List<Product> producten = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                long productNummer = rs.getLong("product_nummer");

                Product product = null;
                for (Product bestaatAl : producten) {
                    if (bestaatAl.getProductNummer() == productNummer) {
                        product = bestaatAl;
                        break;}}

                if (product == null) {
                    product = new Product(
                            productNummer,
                            rs.getString("naam"),
                            rs.getString("beschrijving"),
                            rs.getDouble("prijs"));
                    producten.add(product);}

                int kaartNummerr = rs.getInt("kaart_nummer");
                if (!rs.wasNull()) {
                    Reiziger reiziger = null;
                    int reizigerId = rs.getInt("reiziger_id");
                    // wasnull of integer moeten gebruiken
                    if (!rs.wasNull()) {
                        reiziger = new Reiziger();
                        reiziger.setId(reizigerId);
                        reiziger.setVoorletter(rs.getString("voorletters"));
                        reiziger.setTussenvoegsel(rs.getString("tussenvoegsel"));
                        reiziger.setAchternaam(rs.getString("achternaam"));
                        Date geboortedatum = rs.getDate("geboortedatum");
                        if (geboortedatum != null) reiziger.setGeboortedatum(geboortedatum.toLocalDate());}

                    Date geldig_tot = rs.getDate("geldig_tot");
                    OV_Chipkaart kaart = new OV_Chipkaart(
                            kaartNummerr,
                            geldig_tot != null ? geldig_tot.toLocalDate() : null,
                            rs.getInt("klasse"),
                            rs.getDouble("saldo"),
                            reiziger);
                    product.addOvChipkaart(kaart);
                }}
        } catch (SQLException e) {
            System.err.println("Product findAll:  " + e.getMessage());}
        return producten;
    }
}
