package com.example.webshop.controller;

import com.example.webshop.entity.Customer;
import com.example.webshop.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RegisterUserController {

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/save/user")
    public String saveUserToDatabase(@ModelAttribute("customer")Customer customer,
                                     RedirectAttributes redirectAttributes, Model model){

        List<Customer> allRegisteredCustomer = customerRepository.findAll();

        for(Customer ct : allRegisteredCustomer){
            if(ct.getEmail().equals(customer.getEmail())){
                redirectAttributes.addFlashAttribute("errorMessage","You have already registered with this email address!");
                return "redirect:/register";
            }else if(ct.getUsername().equals(customer.getUsername())){
                redirectAttributes.addFlashAttribute("errorMessage","This username already taken!");
                return "redirect:/register";
            }
        }

        customerRepository.save(customer);

        return "redirect:/register";
    }

}
