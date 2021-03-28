package com.example.webshop.controller;

import com.example.webshop.dto.LoginDto;
import com.example.webshop.dto.OrderDto;
import com.example.webshop.dto.ProductDto;
import com.example.webshop.entity.Customer;
import com.example.webshop.entity.Orders;
import com.example.webshop.entity.Product;
import com.example.webshop.enums.Status;
import com.example.webshop.repository.CustomerRepository;
import com.example.webshop.repository.OrdersRepository;
import com.example.webshop.repository.ProductRepository;
import com.example.webshop.service.OrderService;
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
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

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
        if(!checkLogin){
            redirectAttributes.addFlashAttribute("loginError","Username or password are incorrect.");
            return "redirect:/order/login";
        }

        return "redirect:/user/user-page";
    }

    @GetMapping("/user/user-page")
    public String navigateToUserPage(@ModelAttribute LoginDto loginDto){
        if(loginDto.getUsername() == null || loginDto.getPassword() == null){
            return "redirect:/index";
        }

        return "user/user_page";
    }

    @GetMapping("/users/order")
    public String navigateToUsersOrder(Model model,@ModelAttribute LoginDto loginDto){
        if(loginDto.getUsername() == null || loginDto.getPassword() == null){
            return "redirect:/index";
        }
        Customer customer = customerRepository.findCustomerByUsername(loginDto.getUsername());
        List<Orders> customerListOrders = ordersRepository.findCustomerAllOrderById(customer.getId());
        model.addAttribute("customerListOrders",customerListOrders);
        model.addAttribute("customerName", customer.getName());
        return "user/users_order";
    }

    @GetMapping("/user/order/delete/{orderId}")
    public String deleteUsersOrder(@PathVariable("orderId") Integer orderId){
        Orders selectedOrder = ordersRepository.findOrderById(orderId);

        Product product = productRepository.findProductById(selectedOrder.getProduct().getId());
        product.setStatus(Status.AVAILABLE.toString());

        productRepository.save(product);
        ordersRepository.delete(selectedOrder);

        return "redirect:/users/order";
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

        List<ProductDto> listOfSelectedProducts = orderService.setSelectedProductStatusToSold(orderDto);

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

        return "redirect:/order/login";
    }

    @GetMapping("/list/orders")
    public String navigateToListOrders(Model model){

        List<Orders> allOrders = ordersRepository.findAll();
        model.addAttribute("orders",allOrders);

        return "admin/list_orders";
    }
}
