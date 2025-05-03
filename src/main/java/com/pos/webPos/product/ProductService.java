package com.pos.webPos.product;

import com.pos.webPos.DataNotFoundException;
import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryRepository;
import com.pos.webPos.session.PosSession;
import com.pos.webPos.session.PosSessionService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Interceptor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> getSessionProductList(PosSession posSession) {
        return this.productRepository.findAllByPosSession(posSession);
    }

    public List<Product> getProductsByCategoryAndSession(Integer categoryId, PosSession posSession) {
        return productRepository.findByCategoryIdAndPosSession(categoryId, posSession);
    }

    public Product getProduct(Integer id) {
        return this.productRepository.findById(id)
                .orElseThrow(()->new RuntimeException("data not found"));
    }

    public void addProduct(Category category, String productName, Integer price, PosSession posSession) {
        if(this.productRepository.existsByPosSessionAndProductName(posSession, productName)) {
            throw new IllegalArgumentException("이미 존재하는 제품입니다.");
        }
        Product product = new Product(category, productName, price, posSession);
        this.productRepository.save(product);
    }

    public void edit(Product product, Category category, String productName, Integer price, PosSession posSession) {
        if(!product.getProductName().equals(productName)) {
            if(this.productRepository.existsByPosSessionAndProductName(posSession, productName)) {
                throw new IllegalArgumentException("이미 존재하는 제품입니다.");
            }
        }
        product.update(category,productName,price);
        this.productRepository.save(product);
    }

    public void delete(Product product) {
        this.productRepository.delete(product);
    }
}
