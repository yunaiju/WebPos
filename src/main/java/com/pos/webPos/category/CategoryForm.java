package com.pos.webPos.category;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryForm {
    @NotNull(message = "카테고리명을 입력하세요.")
    private String categoryName;
}
