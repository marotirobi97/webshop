package com.example.webshop.dto;

import com.example.webshop.entity.Customer;
import com.example.webshop.entity.Product;
import com.example.webshop.repository.ProductRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {

    private List<ProductDto> selectedProducts = new ArrayList<>();
    private List<ProductDto> allAvailableProduct = new ArrayList<>();

    private Product product;
    private Customer customer;
    private LocalDateTime date;
}
