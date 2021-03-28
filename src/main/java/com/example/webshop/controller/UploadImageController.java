package com.example.webshop.controller;

import com.example.webshop.dto.ImageDto;
import com.example.webshop.entity.Product;
import com.example.webshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Base64;

@Controller
@SessionAttributes("imageDto")
public class UploadImageController {

    @Autowired
    private ProductRepository productRepository;

    @ModelAttribute
    private ImageDto imageDto(){
        return new ImageDto();
    }

    @GetMapping("/upload/image/{productId}")
    public String navigateToUploadImage(@PathVariable("productId") Integer productId,@ModelAttribute ImageDto imageDto){
        imageDto.setProductId(productId);

        return "admin/upload_image";
    }

    @PostMapping("/save/image")
    public String saveImage(@RequestParam("imageFile") MultipartFile imageFile,@ModelAttribute ImageDto imageDto,
                            RedirectAttributes redirectAttributes) throws Exception {
        Integer productId = imageDto.getProductId();
        Product product = productRepository.findProductById(productId);
        byte[] byteImage = imageFile.getBytes();
        product.setImage(byteImage);
        redirectAttributes.addFlashAttribute("uploadedImageMessage","You have successfully uploaded an image.");
        productRepository.save(product);

        return "redirect:/upload/image/" + productId ;
    }
}
