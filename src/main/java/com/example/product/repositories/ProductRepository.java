package com.example.product.repositories;


import com.example.product.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByName(String name);
    Optional<Product> findById(Long id);
    Product findByNameAndDescription(String name, String description);
    Product findByPrice(int price);
    Page<Product> findAllByNameContaining(String name, Pageable pageable);

    List<Product> findByNameAndDescriptionAndPriceGreaterThan(String title, String description, int price);
//
//    List<Product> findTop5DistinctProductByName(String name);
//
//    void deleteById(Long id);

    Product save(Product product);

    // HQL Query: give the query in terms of Models
    @Query("select p from Product p where p.id = 10")
    Product something(@Param("id") Long id);

    @Query("select p.id as idd, p.name as title, p.price as value from Product p where p.id = :id ")
    ProductWithIdNamePrice somethingSpecific(@Param("id") Long id);

    // HQL queries, are not that intuitive.
    // what is the advantage over Declared queries.
    // you have more control, you give the exact query.

    // if the DB changes, then you DONT have to update your queries!
    // HQL and DB, Loosely couple

    // declared queries are also DB independent.

    @Query(value = "select * from product where id = :sugam limit 10", nativeQuery = true)
    Product somethingMySQLQuery(@Param("id") Long id);

    @Query(value = "select id as idd, name as title, price as value from product where id = :id", nativeQuery = true)
    ProductWithIdNamePrice somethingMySQLQuerySpecific(@Param("id") Long id);

    // if my DB change, will the query change?
    // it will change, exact syntax for MYSQL could be different from MSSQL
    // it is Tightly coupled to the DB.
}