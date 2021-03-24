package com.example.webshop.repository;

import com.example.webshop.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product,Integer> {

    @Query("FROM Product p WHERE p.status = 'AVAILABLE'")
    List<Product> findAllAvailableProduct();

    @Query("FROM Product p WHERE p.id = :id")
    Product findProductById(Integer id);
}
