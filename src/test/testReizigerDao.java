import Domein.Reiziger;
import Implementatie.ReizigerDAOPsql;
import Implementatie.AdresDAOPsql;          // <-- toevoegen
import Interfaces.AdresDAO;
import Interfaces.ReizigerDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class testReizigerDao {

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/ovchip", "postgres", "")) {

            // 1) Eerst AdresDAO maken
            AdresDAO adao = new AdresDAOPsql(conn);

            // 2) Dan injecteren in ReizigerDAO
            ReizigerDAO rdao = new ReizigerDAOPsql(conn, adao);

            // 3) Testen
            testReizigerDAO(rdao);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n Test ReizigerDAO ");

        // findAll
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // save
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", LocalDate.parse(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // extra save
        List<Reiziger> alleVoor = rdao.findAll();
        int aantalVoor = alleVoor.size();
        Reiziger reiziger1 = new Reiziger(12, "S", null, "sloote", LocalDate.of(2003, 8, 2));
        rdao.save(reiziger1);
        int aantalNa = rdao.findAll().size();
        if (aantalNa == aantalVoor + 1) {
            System.out.println("nieuwe reiziger is toegevoegd");
        }

        // findById
        Reiziger gevonden = rdao.findById(12);
        System.out.println(gevonden);

        // findByGbdatum (laat zo als jouw interface LocalDate verwacht)
        LocalDate testGbdatum = LocalDate.of(2003, 8, 2);
        List<Reiziger> metDatum = rdao.findByGbdatum(testGbdatum);
        System.out.println(metDatum);

        // delete
        rdao.delete(reiziger1);
        if (rdao.findById(12) == null) {
            System.out.println("reiziger is verwijdered");
        }

        // update
        sietske.setTussenvoegsel("ss");
        rdao.update(sietske);
        Reiziger updated = rdao.findById(77); // of sietske.getId()/getReizigerId, afhankelijk van jouw model
        if (updated != null && "ss".equals(updated.getTussenvoegsel())) {
            System.out.println("nieuwe tussenvoegsel is = " + updated.getTussenvoegsel());
        }
    }
}
