package Interfaces;

import Domein.Adres;
import Domein.Reiziger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AdresDAO {
    public boolean save(Adres adres);
    public boolean update(Adres adres);
    public boolean delete(Adres adres);
    public Adres findByReiziger(Reiziger reiziger);

    public List<Adres> findAll();
}
