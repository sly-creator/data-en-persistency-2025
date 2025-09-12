
import Domein.Reiziger;
import Implementatie.ReizigerDAOPsql;
import Interfaces.ReizigerDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class testReizigerDao {

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/ovchip", "postgres", "postgres"
            );
            ReizigerDAO rdao = new ReizigerDAOPsql(conn);

            testReizigerDAO(rdao);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
//     * @throws SQLException
     */

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // test findall
        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", LocalDate.parse(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.

        //save
        List<Reiziger> AlleReizigersVoor = rdao.findAll();
        int reizigerAantalVoor = AlleReizigersVoor.size();
        Reiziger reiziger1 = new Reiziger(12,"S",null,"sloote",LocalDate.of(2003,8,2));
        rdao.save(reiziger1);
        List<Reiziger> AlleReizigersNA = rdao.findAll();
        int reizigerAantalNa = AlleReizigersNA.size();
        if (reizigerAantalNa == reizigerAantalVoor + 1){
            System.out.println("nieuwe reiziger is toegevoegd");
        }

        //  find by id
        Reiziger gevonden = rdao.findById(12);
        System.out.println(gevonden);

        // geboortedatum
        LocalDate testGbdatum = LocalDate.of(2003,8,2);
        List<Reiziger> metDatum = rdao.findByGbdatum(testGbdatum);
        System.out.println(metDatum);

        //delete
        rdao.delete(reiziger1);
//        rdao.delete(sietske);
        if (rdao.findById(12) == null){
            System.out.println("reiziger is verwijdered");
        }
        /// update
        sietske.setTussenvoegsel("ss");
        rdao.update(sietske);
        Reiziger updated = rdao.findById(sietske.getId());
        if ("ss".equals(updated.getTussenvoegsel())) {
            System.out.println("nieuwe tussenvoegsel is = " + updated.getTussenvoegsel());
        }




    }


}
