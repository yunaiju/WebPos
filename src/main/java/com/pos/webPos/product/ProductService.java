package com.pos.webPos.product;

import com.pos.webPos.DataNotFoundException;
import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryRepository;
import com.pos.webPos.session.PosSession;
import com.pos.webPos.session.PosSessionRepository;
import com.pos.webPos.session.PosSessionService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Interceptor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final PosSessionRepository posSessionRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> getSessionProductList(PosSession posSession) {
        Optional<PosSession> validSession = this.posSessionRepository.findBySessionId(posSession.getSessionId());
        if(validSession.isPresent()) {
            return this.productRepository.findAllByPosSession(posSession);
        } else {
            throw new DataNotFoundException("session not found");
        }
    }

    public List<Product> getProductsByCategoryAndSession(Integer categoryId, PosSession posSession) {
        if (categoryId!=null) {
            Optional<Category> validCategory = this.categoryRepository.findById(categoryId);
            if(validCategory.isPresent()) {
                return productRepository.findByCategoryIdAndPosSession(categoryId, posSession);
            } else {
                throw new DataNotFoundException("category not found");
            }
        } else {
            throw new DataNotFoundException("category not found");
        }
    }

    public Product getProduct(Integer id) {
        if (id!=null) {
            return this.productRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException("data not found"));
        } else {
            throw new DataNotFoundException("data not found");
        }
    }

    public void addProduct(Category category, String productName, Integer price, PosSession posSession) {
        if(this.productRepository.existsByPosSessionAndProductName(posSession, productName)) {
            throw new IllegalArgumentException("이미 존재하는 제품입니다.");
        }
        Product product = new Product(category, productName, price, posSession);
        this.productRepository.save(product);
    }

    public void edit(Product product, Category category, String productName, Integer price, PosSession posSession) {
        if(product.getProductName().equals(productName)) {
            if(this.productRepository.existsByPosSessionAndProductName(posSession, productName)) {
                throw new IllegalArgumentException("이미 존재하는 제품입니다.");
            }
        }
        product.update(category,productName,price);
        this.productRepository.save(product);
    }

    public void delete(Product product) {
        if(product.getId()!=null) {
            Optional<Product> validProduct = this.productRepository.findById(product.getId());
            if(validProduct.isPresent()) {
                this.productRepository.delete(product);
            } else {
                throw new DataNotFoundException("product not found");
            }
        } else {
            throw new DataNotFoundException("product not found");
        }
    }
}
