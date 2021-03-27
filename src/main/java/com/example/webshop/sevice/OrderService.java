package com.example.webshop.sevice;

import com.example.webshop.dto.LoginDto;
import com.example.webshop.dto.OrderDto;
import com.example.webshop.entity.Customer;
import com.example.webshop.entity.Orders;
import com.example.webshop.entity.Product;
import com.example.webshop.enums.Status;
import com.example.webshop.repository.CustomerRepository;
import com.example.webshop.repository.OrdersRepository;
import com.example.webshop.repository.ProductRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Data
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    public void setUpTwoListAndGiveItToFrontEnd(Model model, OrderDto orderDto) {

        List<Product> allProduct = productRepository.findAllAvailableProduct();
        List<Product> allAvailableProductDto = orderDto.getAllAvailableProduct();

        if(allAvailableProductDto.isEmpty()){
            allAvailableProductDto.addAll(allProduct);
        }

        model.addAttribute("availableProducts", allAvailableProductDto);
        model.addAttribute("selectedProducts", orderDto.getSelectedProducts());
    }

    public void putTheSelectedProductToUserList(Integer productId, OrderDto orderDto) {

        Product product = productRepository.findProductById(productId);

        List<Product> listOfSelectedProducts = orderDto.getSelectedProducts();
        List<Product> allAvailableProduct = orderDto.getAllAvailableProduct();

        listOfSelectedProducts.add(product);
        allAvailableProduct.remove(product);
    }

    public void deleteProductFromUserList(Integer productId, OrderDto orderDto) {

        Product product = productRepository.findProductById(productId);

        List<Product> listOfSelectedProducts = orderDto.getSelectedProducts();
        List<Product> allAvailableProduct = orderDto.getAllAvailableProduct();

        allAvailableProduct.add(product);
        listOfSelectedProducts.remove(product);
        Collections.sort(allAvailableProduct, Comparator.comparingLong(Product::getId));
    }

    public List<Product> setSelectedProductStatusToSold(OrderDto orderDto) {

        List<Product> allProductInDatabase = productRepository.findAllAvailableProduct();
        List<Product> listOfSelectedProducts = orderDto.getSelectedProducts();

        for (Product product : allProductInDatabase) {
            for (Product listOfSelectedProduct : listOfSelectedProducts) {
                if (product.equals(listOfSelectedProduct)) {
                    product.setStatus(Status.SOLD.toString());
                    productRepository.save(product);
                }
            }
        }

        return listOfSelectedProducts;
    }

    public void saveUsersOrderToDatabase(LoginDto loginDto, List<Product> listOfSelectedProducts) {

        Orders order = new Orders();
        String username = loginDto.getUsername();
        Customer customer = customerRepository.findCustomerByUsername(username);

        LocalDateTime date = LocalDateTime.now();
        order.setCustomer(customer);
        order.setDateTime(date);

        for(Product pr : listOfSelectedProducts){
            order.setProduct(pr);
            ordersRepository.save(order);
        }
    }

    public boolean checkLogin(LoginDto loginDto) {

        String username = loginDto.getUsername();
        String passwd = loginDto.getPassword();
        List<Customer> allCustomer = customerRepository.findAll();

        for(Customer ct : allCustomer){
            if(username.equals(ct.getUsername()) || passwd.equals(ct.getPassword())){
                return true;
            }
        }

        return false;
    }
}
