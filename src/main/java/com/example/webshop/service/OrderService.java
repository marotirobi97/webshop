package com.example.webshop.service;

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
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.*;

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

        List<ProductDto> allProduct = this.getProductDtoList();
        List<ProductDto> allAvailableProductDto = orderDto.getAllAvailableProduct();

        if(allAvailableProductDto.isEmpty()){
            allAvailableProductDto.addAll(allProduct);
        }

        model.addAttribute("availableProducts", allAvailableProductDto);
        model.addAttribute("selectedProducts", orderDto.getSelectedProducts());
    }

    public void putTheSelectedProductToUserList(Integer productId, OrderDto orderDto) {

        List<ProductDto> listOfSelectedProducts = orderDto.getSelectedProducts();
        List<ProductDto> allAvailableProduct = orderDto.getAllAvailableProduct();

        List<ProductDto> selectedProductDto = new ArrayList<>();
        for(ProductDto pr : allAvailableProduct){
            if(pr.getId().equals(productId)){
                selectedProductDto.add(pr);
            }
        }
        listOfSelectedProducts.add(selectedProductDto.get(0));
        allAvailableProduct.remove(selectedProductDto.get(0));
    }

    public void deleteProductFromUserList(Integer productId, OrderDto orderDto) {


        List<ProductDto> listOfSelectedProducts = orderDto.getSelectedProducts();
        List<ProductDto> allAvailableProduct = orderDto.getAllAvailableProduct();

        List<ProductDto> selectedProductDto = new ArrayList<>();
        for(ProductDto productDto : listOfSelectedProducts){
            if(productId == productDto.getId()){
                selectedProductDto.add(productDto);
            }
        }

        allAvailableProduct.add(selectedProductDto.get(0));
        listOfSelectedProducts.remove(selectedProductDto.get(0));
        Collections.sort(allAvailableProduct, Comparator.comparingLong(ProductDto::getId));
    }

    public List<ProductDto> setSelectedProductStatusToSold(OrderDto orderDto) {

        List<ProductDto> allProductInDatabase = this.getProductDtoList();
        List<ProductDto> listOfSelectedProducts = orderDto.getSelectedProducts();

        for (ProductDto productDto : allProductInDatabase) {
            for (ProductDto listOfSelectedProduct : listOfSelectedProducts) {
                if (productDto.equals(listOfSelectedProduct)) {
                    Product dbCurrentProduct = productRepository.findProductById(productDto.getId());
                    Product currentProduct = Product.builder()
                            .id(productDto.getId())
                            .name(productDto.getName())
                            .price(productDto.getPrice())
                            .description(productDto.getDescription())
                            .image(dbCurrentProduct.getImage())
                            .status(Status.SOLD.toString())
                            .build();

                    productRepository.save(currentProduct);
                }
            }
        }

        return listOfSelectedProducts;
    }

    public void saveUsersOrderToDatabase(LoginDto loginDto, List<ProductDto> listOfSelectedProducts) {

        for(ProductDto pr : listOfSelectedProducts){
            Orders order = new Orders();
            String username = loginDto.getUsername();
            Customer customer = customerRepository.findCustomerByUsername(username);

            LocalDateTime date = LocalDateTime.now();
            order.setCustomer(customer);
            order.setDateTime(date);

            Product dbCurrentProduct = productRepository.findProductById(pr.getId());
            Product dtoToProduct = Product.builder()
                    .id(pr.getId())
                    .price(pr.getPrice())
                    .description(pr.getDescription())
                    .image(dbCurrentProduct.getImage())
                    .status(Status.SOLD.toString())
                    .build();
            order.setProduct(dtoToProduct);
            ordersRepository.save(order);
        }
    }

    public boolean checkLogin(LoginDto loginDto) {

        String username = loginDto.getUsername();
        String passwd = loginDto.getPassword();
        List<Customer> allCustomer = customerRepository.findAll();

        for(Customer ct : allCustomer){
            if(username.equals(ct.getUsername()) && passwd.equals(ct.getPassword())){
                return true;
            }
        }

        return false;
    }

    public List<ProductDto> getProductDtoList() {
        List<Product> allAvailableProduct = productRepository.findAllAvailableProduct();
        List<ProductDto> all = new ArrayList<>();
        for(Product current : allAvailableProduct){
            ProductDto productDto = new ProductDto();
            productDto.setId(current.getId());
            productDto.setName(current.getName());
            productDto.setPrice(current.getPrice());
            productDto.setDescription(current.getDescription());
            if(current.getImage() != null){
                String decodedImage = Base64.getEncoder().encodeToString(current.getImage());
                productDto.setDecodedImage(decodedImage);
            }
            all.add(productDto);

        }
        return all;
    }
}
