package com.example.product.services;

import com.example.product.dtos.ProductRequestDto;
import com.example.product.exceptions.InvalidProductIdException;
import com.example.product.exceptions.ProductDoesNotExistException;
import com.example.product.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {

    public Product getSingleProduct(Long id) throws InvalidProductIdException;

    List<Product> getAllProducts();

    Page<Product> getProductsContainingName(String name, int pageSize, int startingElementIndex);

    Product addProduct(Product product);

    Product updateProduct(Long id, ProductRequestDto product) throws ProductDoesNotExistException;

    Product updateProduct(Long id, Product product) throws ProductDoesNotExistException;


}