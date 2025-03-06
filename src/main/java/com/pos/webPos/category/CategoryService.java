package com.pos.webPos.category;

import com.pos.webPos.DataNotFoundException;
import com.pos.webPos.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getList() {
        return this.categoryRepository.findAll();
    }

    public Category getCategory(Integer id) {
        Optional<Category> category = this.categoryRepository.findById(id);
        if(category.isPresent()) {
            return category.get();
        } else {
            throw new DataNotFoundException("category not found");
        }
    }

    public void addCategory(String categoryName) {
        Category category = new Category(categoryName);
        this.categoryRepository.save(category);
    }

    public void editCategory(Category category, String categoryName) {
        category.update(categoryName);
        this.categoryRepository.save(category);
    }

    public void delete(Category category) {
        this.categoryRepository.delete(category);
    }

    public void resetCategory() {
        this.categoryRepository.deleteAllCategories();
    }
}
