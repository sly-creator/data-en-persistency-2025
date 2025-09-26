package Interfaces;

import Domein.Product;

import java.util.List;

public interface ProductDAO {
    boolean save(Product product);
    boolean update(Product product);
    boolean delete(Product product);
    List<Product> findByOVChipkaart(long kaartNummer);
    List<Product> findAll();
}


