package Interfaces;

import Domein.OV_Chipkaart;
import Domein.Reiziger;

import java.util.List;

public interface OVChipkaartDAO {
    boolean save(OV_Chipkaart kaart);
    boolean update(OV_Chipkaart kaart);
    boolean delete(OV_Chipkaart kaart);

    OV_Chipkaart findByKaartNummer(long kaartNummer);
    List<OV_Chipkaart> findByReiziger(Reiziger reiziger);
    List<OV_Chipkaart> findAll();

}
