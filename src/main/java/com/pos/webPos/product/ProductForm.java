package com.pos.webPos.product;

import com.pos.webPos.category.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {
    @NotNull(message = "카테고리를 선택하세요")
    private Category category;

    @NotEmpty(message = "상품명을 입력하세요.")
    private String productName;

    @NotNull(message = "가격을 입력하세요.")
    private Integer price;
}
