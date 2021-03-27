package com.example.webshop.repository;

import com.example.webshop.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Integer> {

    List<Customer> findAll();

    @Query("FROM Customer c WHERE c.username = :username")
    Customer findCustomerByUsername(String username);
}
