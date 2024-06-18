package com.example.product.controllers;

import com.example.product.dtos.ProductRequestDto;
import com.example.product.dtos.ProductWrapper;
import com.example.product.exceptions.ProductDoesNotExistException;
import com.example.product.models.Product;
import com.example.product.services.IProductService;
import com.example.product.exceptions.InvalidProductIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    private IProductService productService;

    // Get all the products
    @GetMapping("/products")
    public List<Product> getAllProducts() {

        return productService.getAllProducts();
    }
    //Get product by name
    @GetMapping("/products/search")
    public Page<Product> getProductsByName(@RequestParam("name") String name, @RequestParam("pageSize") int pageSize,
                                           @RequestParam("startingElementIndex") int startingElementIndex){
        return productService.getProductsContainingName(name, pageSize, startingElementIndex);
    }

    // Get a product with Id
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductWrapper> getSingleProduct(@PathVariable("id") Long id) throws InvalidProductIdException {

        ResponseEntity<ProductWrapper> response;
        Product singleProduct = productService.getSingleProduct(id);
        ProductWrapper productWrapper = new ProductWrapper(singleProduct, "Successfully retried the data");
        response = new ResponseEntity<>(productWrapper, HttpStatus.OK);
        return response;
    }

    @PostMapping("/products")
    public Product addProduct(@RequestBody ProductRequestDto productRequestDto) {
        return new Product();
    }

    @PutMapping("/products/{id}")
    public Product updateProduct(@PathVariable("id") Long id,
                                 @RequestBody ProductRequestDto productRequestDto) throws ProductDoesNotExistException {
        return productService.updateProduct(id, productRequestDto);
    }


    @DeleteMapping("/products/{id}")
    public boolean deleteProduct(@PathVariable("id") Long id) {
        return true;
    }


}