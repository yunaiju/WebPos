package com.pos.webPos.product;

import com.pos.webPos.DataNotFoundException;
import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryRepository;
import com.pos.webPos.session.PosSession;
import com.pos.webPos.session.PosSessionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.security.ProtectionDomain;
import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {
    @Autowired private ProductRepository productRepository;
    @Autowired private ProductService productService;
    @Autowired private PosSessionRepository posSessionRepository;
    @Autowired private CategoryRepository categoryRepository;

    private PosSession productTestSession;
    private Category defaultCategory;

    @BeforeEach
    public void beforeEach() {
        productTestSession = new PosSession("productTestSession", LocalDateTime.now());
        this.posSessionRepository.save(productTestSession);

        defaultCategory = new Category("기본", productTestSession);
        this.categoryRepository.save(defaultCategory);
    }

    @DisplayName("세션으로 제품 목록 조회 성공")
    @Test
    void getSessionProductList_success() {
        // given
        Product testProduct1 = new Product(defaultCategory,"testProduct1",10000,productTestSession);
        this.productRepository.save(testProduct1);

        Product testProduct2 = new Product(defaultCategory, "testProduct2", 20000, productTestSession);
        this.productRepository.save(testProduct2);

        // when
        List<Product> productList = this.productService.getSessionProductList(productTestSession);

        // then
        assertThat(productList).isNotNull();
        assertThat(productList).hasSize(2);
        assertThat(productList.get(0).getPosSession()).isEqualTo(productTestSession);
        assertThat(productList.get(1).getPosSession()).isEqualTo(productTestSession);
    }

    @DisplayName("세션으로 제품 목록 조회 실패")
    @Test
    void getSessionProductList_fail() {

        // given
        PosSession notSavedSession = new PosSession("notSavedSession",LocalDateTime.now());

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.productService.getSessionProductList(notSavedSession);
        });
    }

    @DisplayName("세션으로 해당 카테고리 제품 목록 조회 성공")
    @Test
    void getProductsByCategoryAndSession_success() {
        // given
        Category testCategory = new Category("test", productTestSession);
        this.categoryRepository.save(testCategory);

        Product testProduct1 = new Product(testCategory,"test1",10000, productTestSession);
        this.productRepository.save(testProduct1);

        Product testProduct2 = new Product(testCategory,"test2",20000,productTestSession);
        this.productRepository.save(testProduct2);

        // when
        List<Product> productList = this.productService.getProductsByCategoryAndSession(testCategory.getId(), productTestSession);

        // then
        assertThat(productList).isNotNull();
        assertThat(productList).hasSize(2);
        assertThat(productList.get(0).getPosSession()).isEqualTo(productTestSession);
        assertThat(productList.get(1).getPosSession()).isEqualTo(productTestSession);
        assertThat(productList.get(0).getCategory()).isEqualTo(testCategory);
        assertThat(productList.get(1).getCategory()).isEqualTo(testCategory);
    }

    @DisplayName("세션으로 해당 카테고리 제품 목록 조회 실패 - category not found")
    @Test
    void getProductsByCategoryAndSession_fail_when_category_not_found() {
        Category testCategory = new Category("test", productTestSession);

        Product testProduct1 = new Product(testCategory,"test1",10000, productTestSession);
        this.productRepository.save(testProduct1);

        Product testProduct2 = new Product(testCategory,"test2",20000,productTestSession);
        this.productRepository.save(testProduct2);

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.productService.getProductsByCategoryAndSession(testCategory.getId(), productTestSession);
        });
    }

    @DisplayName("제품 조회 성공")
    @Test
    void getProduct_success() {
        // given
        Product testProduct = new Product(defaultCategory, "testProduct", 10000, productTestSession);
        this.productRepository.save(testProduct);

        // when
        Product getProduct = this.productService.getProduct(testProduct.getId());

        // then
        assertThat(getProduct.getId()).isEqualTo(testProduct.getId());
        assertThat(getProduct.getCategory()).isEqualTo(testProduct.getCategory());
        assertThat(getProduct.getProductName()).isEqualTo(testProduct.getProductName());
        assertThat(getProduct.getPrice()).isEqualTo(testProduct.getPrice());
        assertThat(getProduct.getPosSession()).isEqualTo(testProduct.getPosSession());
    }

    @DisplayName("제품 조회 실패")
    @Test
    void getProduct_fail() {
        // given
        Product testProduct = new Product(defaultCategory, "testProduct", 10000, productTestSession);

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.productService.getProduct(testProduct.getId());
        });
    }

    @DisplayName("제품 추가 성공")
    @Test
    void addProduct_success() {
        // given
        ProductForm productForm = new ProductForm();
        productForm.setProductName("새로운 제품");
        productForm.setCategory(defaultCategory);
        productForm.setPrice(10000);

        // when
        this.productService.addProduct(productForm.getCategory(), productForm.getProductName(),
                productForm.getPrice(), productTestSession);
        Product addProduct = this.productRepository.findByProductName("새로운 제품");

        // then
        assertThat(addProduct.getProductName()).isEqualTo(productForm.getProductName());
        assertThat(addProduct.getCategory()).isEqualTo(productForm.getCategory());
        assertThat(addProduct.getPrice()).isEqualTo(productForm.getPrice());
    }

    @DisplayName("제품 추가 실패 - 제품명 중복")
    @Test
    void addProduct_fail_when_productName_duplicate() {
        // given
        Product testProduct = new Product(defaultCategory, "이미 있는 이름", 10000, productTestSession);
        this.productRepository.save(testProduct);

        ProductForm productForm = new ProductForm();
        productForm.setCategory(defaultCategory);
        productForm.setProductName("이미 있는 이름");
        productForm.setPrice(20000);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            this.productService.addProduct(productForm.getCategory(), productForm.getProductName(), productForm.getPrice(), productTestSession);
        });
    }

    @DisplayName("제품 수정 성공")
    @Test
    void edit() {
        // given
        Product productBeforeEdit = new Product(defaultCategory, "수정할 제품", 10000, productTestSession);
        this.productRepository.save(productBeforeEdit);

        Category newCategory = new Category("new", productTestSession);
        this.categoryRepository.save(newCategory);

        // when
        ProductForm productForm = new ProductForm();
        productForm.setCategory(newCategory);
        productForm.setProductName("new Name");
        productForm.setPrice(20000);

        this.productService.edit(productBeforeEdit, productForm.getCategory(), productForm.getProductName(),
                productForm.getPrice(), productTestSession);

        Product productAfterEdit = this.productRepository.findByProductName("new Name");

        // then
        assertThat(productAfterEdit.getId()).isEqualTo(productBeforeEdit.getId());
        assertThat(productAfterEdit.getCategory()).isEqualTo(productBeforeEdit.getCategory());
        assertThat(productAfterEdit.getProductName()).isEqualTo(productBeforeEdit.getProductName());
        assertThat(productAfterEdit.getPrice()).isEqualTo(productBeforeEdit.getPrice());
        assertThat(productAfterEdit.getPosSession()).isEqualTo(productBeforeEdit.getPosSession());
    }

    @DisplayName("제품 수정 실패 - 제품명 중복")
    @Test
    void edit_fail_when_productName_duplicate() {
        // given
        Product editProduct = new Product(defaultCategory, "수정할 제품", 10000, productTestSession);
        this.productRepository.save(editProduct);

        Category newCategory = new Category("new", productTestSession);
        this.categoryRepository.save(newCategory);

        // when & then
        ProductForm productForm = new ProductForm();
        productForm.setCategory(newCategory);
        productForm.setProductName("수정할 제품");
        productForm.setPrice(20000);

        assertThrows(IllegalArgumentException.class, () -> {
            this.productService.edit(editProduct, productForm.getCategory(), productForm.getProductName(),
                    productForm.getPrice(), productTestSession);
        });
    }

    @DisplayName("제품 삭제 성공")
    @Test
    void delete_success() {
        // given
        Product deleteProduct = new Product(defaultCategory, "삭제할 제품", 1000, productTestSession);
        this.productRepository.save(deleteProduct);

        // when
        this.productService.delete(deleteProduct);

        // then
        assertThrows(DataNotFoundException.class, () -> {
            this.productService.getProduct(deleteProduct.getId());
        });
    }

    @DisplayName("제품 삭제 실패")
    @Test
    void delete_fail() {
        // given
        Product invalidProduct = new Product(defaultCategory, "삭제할 제품", 10000, productTestSession);

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.productService.delete(invalidProduct);
        });
    }
}