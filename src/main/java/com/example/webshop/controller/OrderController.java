package com.example.webshop.controller;

import com.example.webshop.dto.LoginDto;
import com.example.webshop.dto.OrderDto;
import com.example.webshop.entity.Orders;
import com.example.webshop.entity.Product;
import com.example.webshop.repository.OrdersRepository;
import com.example.webshop.sevice.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes({"orderDto","loginDto"})
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrdersRepository ordersRepository;

    @ModelAttribute
    private OrderDto orderDto(){
        return new OrderDto();
    }

    @ModelAttribute
    private LoginDto loginDto(){
        return new LoginDto();
    }

    @GetMapping("/order/login")
    public String loginInOrderToOrder(Model model){

        model.addAttribute("loginDto", new LoginDto());
        return "user/login_page";
    }

    @PostMapping("/check/login")
    public String checkIfLoginValid(@ModelAttribute LoginDto loginDto,RedirectAttributes redirectAttributes){

        boolean checkLogin = orderService.checkLogin(loginDto);
        if(checkLogin){
            return "redirect:/order-product";
        }

        redirectAttributes.addFlashAttribute("loginError","Username or password are incorrect.");

        return "redirect:/order/login";
    }

    @GetMapping("/order-product")
    public String navigateToOrderProduct(Model model, @ModelAttribute OrderDto orderDto,@ModelAttribute LoginDto loginDto){

        if(loginDto.getUsername() == null || loginDto.getPassword() == null){
            return "redirect:/index";
        }

        orderService.setUpTwoListAndGiveItToFrontEnd(model, orderDto);

        return "user/order_product";
    }

    @GetMapping("/select/product/{productId}")
    public String selectProduct(@PathVariable("productId") Integer productId, @ModelAttribute OrderDto orderDto){

        orderService.putTheSelectedProductToUserList(productId, orderDto);

        return "redirect:/order-product";
    }

    @GetMapping("/delete/product/{productId}")
    public String deleteSelectedProduct(@PathVariable("productId") Integer productId,@ModelAttribute OrderDto orderDto){

        orderService.deleteProductFromUserList(productId, orderDto);

        return "redirect:/order-product";
    }

    @PostMapping("/submit/order")
    public String submitOrder(@ModelAttribute OrderDto orderDto,  @ModelAttribute LoginDto loginDto,RedirectAttributes redirectAttributes){

        List<Product> listOfSelectedProducts = orderService.setSelectedProductStatusToSold(orderDto);

        orderService.saveUsersOrderToDatabase(loginDto, listOfSelectedProducts);

        orderDto.setAllAvailableProduct(new ArrayList<>());
        orderDto.setSelectedProducts(new ArrayList<>());

        redirectAttributes.addFlashAttribute("successfulOrder","Successful order.");

        return "redirect:/order-product";
    }

    @GetMapping("/logout")
    public String logout(@ModelAttribute LoginDto loginDto){

        loginDto.setUsername(null);
        loginDto.setPassword(null);

        return "redirect:/index";
    }

    @GetMapping("/list/orders")
    public String navigateToListOrders(Model model){

        List<Orders> allOrders = ordersRepository.findAll();
        model.addAttribute("orders",allOrders);

        return "admin/list_orders";
    }
}
