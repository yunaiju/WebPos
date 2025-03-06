package com.pos.webPos.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.webPos.category.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonIgnore
    private Category category;

    private String productName;

    private Integer price;

    public Product(Category category, String productName, Integer price) {
        this.category = category;
        this.productName = productName;
        this.price = price;
    }

    public void update(Category category, String productName, Integer price) {
        this.category=category;
        this.productName=productName;
        this.price=price;
    }
}


