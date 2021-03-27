package com.example.webshop.controller;

import com.example.webshop.entity.Product;
import com.example.webshop.enums.Status;
import com.example.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/product/edit/{productId}")
    public String editProduct(@PathVariable("productId") Integer productId, Model model){

        Product selectedProduct = productRepository.findProductById(productId);
        model.addAttribute("product", selectedProduct);

        return "admin/edit_product";
    }

    @GetMapping("/product/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Integer productId, Model model){

        Product selectedProduct = productRepository.findProductById(productId);
        productRepository.delete(selectedProduct);

        return "redirect:/list-products";
    }

    @PostMapping("/save-edited-product")
    public String saveEditedProduct(@ModelAttribute("product") Product product){

        productRepository.save(product);

        return "redirect:/list-products";
    }

    @PostMapping("/save-new-product")
    public String saveNewProduct(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes){

        product.setStatus(Status.AVAILABLE.toString());
        productRepository.save(product);

        redirectAttributes.addFlashAttribute("message","You have created a new product!");

        return "redirect:/create-product";
    }


}
