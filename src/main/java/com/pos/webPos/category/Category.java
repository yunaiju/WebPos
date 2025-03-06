package com.pos.webPos.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.webPos.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Product> productList;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public void update(String categoryName) {
        this.categoryName=categoryName;
    }
}
