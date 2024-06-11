package com.example.product.services;

import com.example.product.dtos.ProductRequestDto;
import com.example.product.exceptions.InvalidProductIdException;
import com.example.product.exceptions.ProductDoesNotExistException;
import com.example.product.models.Category;
import com.example.product.models.Product;
import com.example.product.repositories.CategoryRepository;
import com.example.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
//@Primary
//@Qualifier("selfProductService")
public class SelfProductService implements IProductService{

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public SelfProductService(ProductRepository productRepository, CategoryRepository categoryRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product getSingleProduct(Long id) throws InvalidProductIdException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            throw new InvalidProductIdException("product with id " + id + " not found");
        }
        Product product = productOptional.get();
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return null;
    }

    @Override
    public Product addProduct(Product product) {
        Optional<Category> categoryOptional = categoryRepository.findByName(product.getCategory().getName());
        if(categoryOptional.isEmpty()){

        } else {
            product.setCategory(categoryOptional.get());
        }

        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    @Override
    public Product updateProduct(Long id, ProductRequestDto product) throws ProductDoesNotExistException {
        return null;
    }

    @Override
    public Product updateProduct(Long id, Product product) throws ProductDoesNotExistException {
        // 1. get the existing product to update
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            throw new ProductDoesNotExistException("product with id " + id + " does not exist");
        }

        Product existingProduct = productOptional.get();
        Product updatedProduct = new Product();

        updatedProduct.setId(id);

        updatedProduct.setName(
                product.getName() != null ?
                        product.getName() :
                        existingProduct.getName()
        );

        updatedProduct.setDescription(
                product.getDescription() != null ?
                        product.getDescription() :
                        existingProduct.getDescription()
        );

        updatedProduct.setImage(
                product.getImage() != null ?
                        product.getImage() :
                        existingProduct.getImage()
        );

        updatedProduct.setPrice(
                product.getPrice() > 0 ?
                        product.getPrice() :
                        existingProduct.getPrice()
        );

        Optional<Category> categoryOptional = categoryRepository.findByName(product.getCategory().getName());
        if(categoryOptional.isEmpty()){
            Category categoryToSave = new Category();
            categoryToSave.setName(product.getCategory().getName());
            Category savedCategory = categoryRepository.save(categoryToSave);
            updatedProduct.setCategory(savedCategory);
        } else {
            updatedProduct.setCategory(categoryOptional.get());
        }

        Product savedUpdatedProduct = productRepository.save(updatedProduct);
        return savedUpdatedProduct;
    }
}