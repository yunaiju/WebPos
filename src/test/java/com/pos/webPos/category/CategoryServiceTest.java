package com.pos.webPos.category;

import com.pos.webPos.DataNotFoundException;
import com.pos.webPos.session.PosSession;
import com.pos.webPos.session.PosSessionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private CategoryService categoryService;
    @Autowired private PosSessionRepository posSessionRepository;

    private PosSession categoryTestSession;

    @BeforeEach
    public void beforeEach() {
        categoryTestSession = new PosSession("categoryTestSession", LocalDateTime.now());
        this.posSessionRepository.save(categoryTestSession);

        Category defaultCategory = new Category("기본", categoryTestSession);
        categoryRepository.save(defaultCategory);
    }

    @DisplayName("세션의 카테고리 목록 가져오기 성공")
    @Test
    void getSessionCategoryList_success() {
        // given
        Category category = new Category("testCategory", categoryTestSession);
        this.categoryRepository.save(category);

        // when
        List<Category> categoryList = this.categoryService.getSessionCategoryList(categoryTestSession);

        // then
        assertThat(categoryList).isNotNull();
        assertThat(categoryList).hasSize(2);
        assertThat(categoryList.get(0).getCategoryName()).isEqualTo("기본");
        assertThat(categoryList.get(1).getCategoryName()).isEqualTo("testCategory");
    }

    @DisplayName("세션의 카테고리 목록 가져오기 실패")
    @Test
    void getSessionCategoryList_fail() {
        // given
        PosSession notSavedPosSession = new PosSession("notSavedPosSession", LocalDateTime.now());

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.categoryService.getSessionCategoryList(notSavedPosSession);
        });
    }

    @DisplayName("카테고리 조회 성공")
    @Test
    void getCategory_success() {
        // given
        Category testCategory = new Category("testCategory", categoryTestSession);
        this.categoryRepository.save(testCategory);
        Integer testCategoryId = testCategory.getId();

        // when
        Category foundCategory = this.categoryService.getCategory(testCategoryId);

        // then
        assertThat(foundCategory.getCategoryName()).isEqualTo(testCategory.getCategoryName());
        assertThat(foundCategory.getPosSession()).isEqualTo(testCategory.getPosSession());
    }

    @DisplayName("카테고리 조회 실패")
    @Test
    void getCategory_fail() {
        // given
        Integer fakeCategoryId = -1;

        // when & then
        assertThrows(DataNotFoundException.class,()-> {
           this.categoryService.getCategory(fakeCategoryId);
        });
    }

    @DisplayName("세션의 기본 카테고리 조회 성공")
    @Test
    void getDefaultCategoryBySession_success() {
        // given
        // beforeEach

        // when
        Category foundCategory = this.categoryService.getDefaultCategoryBySession(categoryTestSession);

        // then
        assertThat(foundCategory.getCategoryName()).isEqualTo("기본");
        assertThat(foundCategory.getPosSession()).isEqualTo(categoryTestSession);
    }

    @DisplayName("세션의 기본 카테고리 조회 실패")
    @Test
    void getDefaultCategoryBySession_fail() {
        // given
        PosSession notSavedPosSession = new PosSession("notSavedPosSession", LocalDateTime.now());

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.categoryService.getDefaultCategoryBySession(notSavedPosSession);
        });
    }

    @DisplayName("카테고리 추가 성공")
    @Test
    void addCategory_success() {
        // given
        // beforeEach

        // when
        this.categoryService.addCategory("testCategory", categoryTestSession);
        Category addCategory = this.categoryRepository.findByCategoryName("testCategory");

        // then
        assertThat(addCategory.getCategoryName()).isEqualTo("testCategory");
        assertThat(addCategory.getPosSession()).isEqualTo(categoryTestSession);
    }

    @DisplayName("카테고리 추가 실패")
    @Test
    void addCategory_fail() {
        // given
        Category duplicatedCategory = new Category("중복된 카테고리", categoryTestSession);
        this.categoryRepository.save(duplicatedCategory);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            this.categoryService.addCategory("중복된 카테고리", categoryTestSession);
        });
    }

    @DisplayName("카테고리 수정 성공")
    @Test
    void editCategory_success() {
        // given
        Category categoryBeforeEdit = new Category("수정해주세요", categoryTestSession);
        this.categoryRepository.save(categoryBeforeEdit);

        // when
        this.categoryService.editCategory(categoryBeforeEdit, "수정했어요", categoryTestSession);
        Category categoryAfterEdit = this.categoryRepository.findByCategoryName("수정했어요");

        // then
        assertThat(categoryAfterEdit).isEqualTo(categoryBeforeEdit);
        assertThat(categoryAfterEdit.getCategoryName()).isEqualTo("수정했어요");
        assertThat(categoryAfterEdit.getPosSession()).isEqualTo(categoryBeforeEdit.getPosSession());
    }

    @DisplayName("카테고리 수정 실패")
    @Test
    void editCategory_fail_when_duplicateName() {
        // given
        Category editCategory = new Category("수정카테고리", categoryTestSession);
        this.categoryRepository.save(editCategory);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            this.categoryService.editCategory(editCategory, "수정카테고리", categoryTestSession);
        });
    }

    @DisplayName("카테고리 삭제 성공")
    @Test
    void delete_success() {
        // given
        Category deleteCategory = new Category("삭제될 카테고리", categoryTestSession);
        this.categoryRepository.save(deleteCategory);

        // when
        this.categoryService.delete(deleteCategory);

        // then
        assertThrows(DataNotFoundException.class, () -> {
            this.categoryService.getCategory(deleteCategory.getId());
        });
    }

    @DisplayName("카테고리 삭제 실패")
    @Test
    void delete_fail() {
        // given
        Category notSavedCategory = new Category("저장안할거임", categoryTestSession);

        // when & then
        assertThrows(DataNotFoundException.class, () -> {
            this.categoryService.delete(notSavedCategory);
        });
    }
}