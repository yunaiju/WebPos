package com.pos.webPos.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pos.webPos.product.Product;
import com.pos.webPos.session.PosSession;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true,nullable = false)
    private String categoryName;

    @ManyToOne
    private PosSession posSession;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Product> productList = new ArrayList<>();

    public Category(String categoryName, PosSession posSession) {
        this.categoryName = categoryName;
        this.posSession = posSession;
    }

    public void update(String categoryName) {
        this.categoryName=categoryName;
    }
}
