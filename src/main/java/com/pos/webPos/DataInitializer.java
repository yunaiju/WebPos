package com.pos.webPos;

import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer {
    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        if(categoryRepository.findByCategoryName("기본").isEmpty()) {
            Category defaultCat = new Category("기본");
            categoryRepository.save(defaultCat);
        }
    }



}
