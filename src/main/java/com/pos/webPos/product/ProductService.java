package com.pos.webPos.product;

import com.pos.webPos.DataNotFoundException;
import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Interceptor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getList() {
        return this.productRepository.findAll();
    }

    public List<Product> getProductListByCategoryId(Integer categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public Product getProduct(Integer id) {
        return this.productRepository.findById(id)
                .orElseThrow(()->new RuntimeException("data not found"));
    }

    public void addProduct(Category category, String productName, Integer price) {
        Product product = new Product(category, productName, price);
        this.productRepository.save(product);
    }

    public void edit(Product product, Category category, String productName, Integer price) {
        product.update(category,productName,price);
        this.productRepository.save(product);
    }

    public void delete(Product product) {
        this.productRepository.delete(product);
    }

    public void resetProduct() {
        this.productRepository.deleteAllProducts();
    }
}
