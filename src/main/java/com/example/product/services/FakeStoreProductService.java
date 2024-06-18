package com.example.product.services;

import com.example.product.dtos.ProductRequestDto;
import com.example.product.dtos.ProductResponseDto;
import com.example.product.exceptions.InvalidProductIdException;
import com.example.product.exceptions.ProductDoesNotExistException;
import com.example.product.models.Category;
import com.example.product.models.Product;
import com.example.product.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Primary
@Qualifier("fakeStoreProductService")
public class FakeStoreProductService implements IProductService{

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private RedisTemplate<String, Product> redisTemplate;
    @Autowired
    private ProductRepository productRepository;

    public Product getProductFromResponseDto(ProductResponseDto responseDto){
        Product product = new Product();
        product.setId(responseDto.getId());
        product.setName(responseDto.getTitle());
        product.setPrice(responseDto.getPrice());
        product.setDescription(responseDto.getDescription());
        product.setImage(responseDto.getImage());

        Category category = new Category();
        category.setName(responseDto.getCategory());

        product.setCategory(category);
        return product;
    }

    @Override
    public Product getSingleProduct(Long id) throws InvalidProductIdException {


        if(redisTemplate.opsForValue().get("product:"+id)!=null){
            System.out.println("Product found in cache");
            return (Product) redisTemplate.opsForValue().get("product:"+id);
        }
        if(id>50){
            throw new InvalidProductIdException("Invalid Product Id.");
        }

        // I should pass this 'id' to fakestore and get the details of this product.
        // "https://fakestoreapi.com/products/1"
        ProductResponseDto response = restTemplate.getForObject("https://fakestoreapi.com/products/" + id,
                ProductResponseDto.class);
        Product product = getProductFromResponseDto(response);
        redisTemplate.opsForValue().set("product:" + id, product, 5, TimeUnit.MINUTES);





        return product;
    }

    @Override
    public List<Product> getAllProducts() {

        ProductResponseDto[] responseDtoList =
                restTemplate.getForObject("https://fakestoreapi.com/products",
                        ProductResponseDto[].class);

        List<Product> output = new ArrayList<>();
        for(ProductResponseDto productResponseDto: responseDtoList){
            output.add(getProductFromResponseDto(productResponseDto));
        }
        return output;
    }
    @Override
    public Page<Product> getProductsContainingName(String name, int pageSize, int startingElementIndex){
     Page<Product> productPage = productRepository.findAllByNameContaining(name, PageRequest.of(startingElementIndex, pageSize, Sort.by("name")));
        return productPage;
    }

    @Override
    public Product addProduct(Product product) {
        return null;
    }

    @Override
    public Product updateProduct(Long id, ProductRequestDto product) {
        return null;
    }

    @Override
    public Product updateProduct(Long id, Product product) throws ProductDoesNotExistException {
        return null;
    }

//    @Override
//    public Product updateProduct(Long id, Product product) {
//
//        /*
//          Method to call -> PUT, it'll lead to two network calls.
//         */
//
//        RequestCallback requestCallback = restTemplate.httpEntityCallback(productRequestDto, ProductResponseDto.class);
//        HttpMessageConverterExtractor<ProductResponseDto> responseExtractor =
//                new HttpMessageConverterExtractor<>(ProductResponseDto.class,
//                        restTemplate.getMessageConverters());
//        ProductResponseDto responseDto = restTemplate.execute("https://fakestoreapi.com/products/" + id,
//                HttpMethod.PUT, requestCallback, responseExtractor);
//        return getProductFromResponseDto(responseDto);
//
////        // This is not returning the object that it has modified.
////        restTemplate.put("https://fakestoreapi.com/products/"+id, productRequestDto);
////
////
////        // But, I want to get the updated object.
////        return getSingleProduct(id);
//    }
}