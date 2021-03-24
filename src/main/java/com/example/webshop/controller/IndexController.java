package com.example.webshop.controller;

import com.example.webshop.entity.Product;
import com.example.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private ProductRepository productRepository;
    
    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/list-products")
    public String navigateToListOfProducts(Model model){
        List<Product> allAvailableProduct = productRepository.findAllAvailableProduct();
        model.addAttribute("availableProducts",allAvailableProduct);
        return "list_products";
    }

    @GetMapping("/create-product")
    public String navigateToCreateNewProduct(Model model){
        model.addAttribute("product", new Product());
        return "create_product";
    }
}
