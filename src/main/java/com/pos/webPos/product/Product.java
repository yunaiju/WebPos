package com.pos.webPos.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.webPos.category.Category;
import com.pos.webPos.session.PosSession;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.codehaus.groovy.reflection.stdclasses.CachedSAMClass;

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

    @Column(unique = true, nullable = false)
    private String productName;

    private Integer price;

    @ManyToOne
    @JsonIgnore
    private PosSession posSession;

    public Product(Category category, String productName, Integer price, PosSession posSession) {
        this.category = category;
        this.productName = productName;
        this.price = price;
        this.posSession = posSession;
    }

    public void update(Category category, String productName, Integer price) {
        this.category=category;
        this.productName=productName;
        this.price=price;
    }
}


