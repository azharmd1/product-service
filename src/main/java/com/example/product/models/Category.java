package com.example.product.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@JsonSerialize
public class Category extends BaseModel implements Serializable {
    @OneToMany(mappedBy = "category")
//    // the column on the other side
    private List<Product> products;
    private String name;
}

// Spring does not know, that Category -> List<Products>
// is the exact same relationship as Product -> Category


// in my category Table
// cid = 1, list -> [10, 20, 39, 81]

// 2 possibilities
// cid | pid
// 1     10
// 1     20
// 1     39
// 1     81


// If I can tell spring
// hey spring, the other side is already handling the cardinality,
// using a specific column,
// you dont have to handle cardinality over here separately.