package com.example.webshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Integer id;
    private String name;
    private Integer price;
    private String description;
    private String decodedImage;
}
