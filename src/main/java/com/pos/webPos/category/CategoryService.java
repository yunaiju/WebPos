package com.pos.webPos.category;

import com.pos.webPos.DataNotFoundException;
import com.pos.webPos.product.Product;
import com.pos.webPos.session.PosSession;
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

    public List<Category> getSessionCategoryList(PosSession posSession) {
        return this.categoryRepository.findAllByPosSession(posSession);
    }

    public Category getCategory(Integer id) {
        Optional<Category> category = this.categoryRepository.findById(id);
        if(category.isPresent()) {
            return category.get();
        } else {
            throw new DataNotFoundException("category not found");
        }
    }

    public Category getDefaultCategoryBySession(PosSession posSession) {
        return this.categoryRepository.findByCategoryNameAndPosSession("기본", posSession);
    }

    public void addCategory(String categoryName, PosSession posSession) {
        if(this.categoryRepository.existsByPosSessionAndCategoryName(posSession, categoryName)) {
            throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
        }
        Category category = new Category(categoryName, posSession);
        this.categoryRepository.save(category);
    }

    public void editCategory(Category category, String categoryName, PosSession posSession) {
        if(!category.getCategoryName().equals(categoryName)) {
            if(this.categoryRepository.existsByPosSessionAndCategoryName(posSession, categoryName)) {
                throw new IllegalArgumentException("이미 존재하는 카테고리입니다.");
            }
        }
        category.update(categoryName);
        this.categoryRepository.save(category);
    }

    public void delete(Category category) {
        this.categoryRepository.delete(category);
    }
}
