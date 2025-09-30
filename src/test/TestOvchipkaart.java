import Domein.Adres;
import Domein.OV_Chipkaart;
import Domein.Reiziger;
import Implementatie.AdresDAOPsql;
import Implementatie.OVChipkaartDAOPsql;
import Implementatie.ProductDAOPsql;
import Implementatie.ReizigerDAOPsql;
import Interfaces.AdresDAO;
import Interfaces.OVChipkaartDAO;
import Interfaces.ProductDAO;
import Interfaces.ReizigerDAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

public class TestOvchipkaart {

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/ovchip", "postgres", "postgres")) {

            // dependency-injectie
            AdresDAO adao = new AdresDAOPsql(conn);
            ProductDAO productDAO = new ProductDAOPsql(conn);
            OVChipkaartDAO ovdao = new OVChipkaartDAOPsql(conn,adao,productDAO );
            ReizigerDAO rdao = new ReizigerDAOPsql(conn, adao, ovdao);

            // Test SAVE
            Reiziger r = new Reiziger(77, "G", "", "van Rijn", LocalDate.of(2002, 9, 17));
            r.setAdres(new Adres(77, "3511", "LX", "37", "Utrecht", r));
            System.out.println("[save reiziger] -> " + rdao.save(r));

            OV_Chipkaart k1 = new OV_Chipkaart(780000001, LocalDate.now().plusYears(1), 2, 25.00, r);
            System.out.println("[save kaart1] -> " + ovdao.save(k1));

            OV_Chipkaart k2 = new OV_Chipkaart(780000002, LocalDate.now().plusMonths(6), 1,
                    10.50, r);
            System.out.println("[save kaart2] -> " + ovdao.save(k2));

            // Test FIND
            System.out.println("[findByReiziger]");
            for (OV_Chipkaart k : ovdao.findByReiziger(r)) {
                System.out.println("  " + k);
            }

            // Test UPDATE
            k1.setKlasse(1);
            k1.setSaldo(42.35);
            System.out.println("[update kaart1] -> " + ovdao.update(k1));
            System.out.println("[kaart1 na update] -> " + ovdao.findByKaartNummer(780000001));

            // Test DELETE
            System.out.println("[delete kaart2] -> " + ovdao.delete(k2));

            System.out.println("[kaarten na delete kaart2]");
            for (OV_Chipkaart k : ovdao.findByReiziger(r)) {
                System.out.println("  " + k);
            }

            ovdao.delete(k1);
            System.out.println("[delete reiziger] -> " + rdao.delete(r));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
