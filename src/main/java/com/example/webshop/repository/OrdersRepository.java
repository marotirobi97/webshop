package com.example.webshop.repository;

import com.example.webshop.entity.Orders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends CrudRepository<Orders, Integer> {

    List<Orders> findAll();
}
