package com.example.webshop.controller;

import com.example.webshop.dto.OrderDto;
import com.example.webshop.dto.ProductDto;
import com.example.webshop.entity.Customer;
import com.example.webshop.entity.Product;
import com.example.webshop.repository.ProductRepository;
import com.example.webshop.service.OrderService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Controller
@SessionAttributes("productDto")
public class IndexController {

    @ModelAttribute
    private ProductDto productDto(){
        return new ProductDto();
    }

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderService orderService;
    
    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/list-products")
    public String navigateToListOfProducts(Model model){
        List<ProductDto> all = orderService.getProductDtoList();
        model.addAttribute("availableProducts",all);
        return "admin/list_products";
    }

    @GetMapping("/create-product")
    public String navigateToCreateNewProduct(Model model){
        model.addAttribute("product", new Product());
        return "admin/create_product";
    }

    @GetMapping("/register")
    public String navigateToRegister(Model model){
        model.addAttribute("customer",new Customer());
        return "public/register_users";
    }
}
