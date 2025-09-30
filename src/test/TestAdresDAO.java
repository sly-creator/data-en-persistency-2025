import Domein.Adres;
import Domein.Reiziger;
import Implementatie.AdresDAOPsql;
import Implementatie.ReizigerDAOPsql;
import Interfaces.AdresDAO;
import Interfaces.ReizigerDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class TestAdresDAO {

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/ovchip", "postgres", "postgres")) {

            AdresDAO adao = new AdresDAOPsql(conn);
            ReizigerDAO rdao = new ReizigerDAOPsql(conn, adao);

            testAdresDAO(adao, rdao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n Test AdresDAO");

        // Maak een reiziger voor de koppeling
        Reiziger r = new Reiziger(77, "G", "", "van Rijn",
                LocalDate.of(2002, 9, 17));

        rdao.save(r);

        // save()
        Adres a1 = new Adres(77, "3511", "LX", "37", "rrr",r);
        System.out.println("[save] \n" + a1);

        adao.save(a1);

        // findByReiziger()
        System.out.println("[findByReiziger] \n" + adao.findByReiziger(r));

        // update()
        a1.setWoonplaats("Rotterdam");
        adao.update(a1);
        System.out.println("update \n" + adao.findByReiziger(r));


        // delete()
        adao.delete(a1);
        System.out.println("delete \n" + adao.findByReiziger(r));

        // opruimen
        rdao.delete(r);
    }
}