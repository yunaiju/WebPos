package com.pos.webPos;

import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataInitializer {
    private final CategoryRepository categoryRepository;

}
