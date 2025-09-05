package Interfaces;

import Domein.Reiziger;

import java.time.LocalDate;
import java.util.List;

public interface ReizigerDAO {
    public boolean save(Reiziger reiziger);
    public boolean update(Reiziger reiziger);
    public boolean delete(Reiziger reiziger);
    public Reiziger findById(int id);
    public List<Reiziger> findByGbdatum(LocalDate geboortedatum);
    public List<Reiziger> findAll();
}
