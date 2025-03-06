package com.pos.webPos;

import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryRepository;

import com.pos.webPos.product.Product;
import com.pos.webPos.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebPosApplicationTests {

	@Autowired ProductRepository productRepository;
	@Autowired CategoryRepository categoryRepository;

	@Test
	void testJPA() {

	}

}
