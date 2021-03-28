package com.example.webshop.repository;

import com.example.webshop.entity.Orders;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends CrudRepository<Orders, Integer> {

    List<Orders> findAll();

    @Query("SELECT o FROM Orders o WHERE o.customer.id = :id")
    List<Orders> findCustomerAllOrderById(Integer id);

    @Query("SELECT o FROM Orders o WHERE o.id = :id")
    Orders findOrderById(Integer id);
}
