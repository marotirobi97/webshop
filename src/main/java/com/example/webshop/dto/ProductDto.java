package com.example.webshop.dto;

import lombok.Data;

@Data
public class ProductDto {

    private Integer id;
    private String name;
    private Integer price;
    private String description;
    private byte[] image;
}
