package com.pos.webPos.controller;

import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryForm;
import com.pos.webPos.category.CategoryService;
import com.pos.webPos.product.Product;
import com.pos.webPos.product.ProductForm;
import com.pos.webPos.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/item")
public class ItemController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("")
    public String main() {
        return "item/main";
    }

    @GetMapping("/products")
    public String products(Model model) {
        List<Product> products = this.productService.getList();
        model.addAttribute("products",products);

        return "item/products";
    }

    @GetMapping("/product/{id}")
    public String product(Model model, @PathVariable("id") Integer id) {
        Product product = this.productService.getProduct(id);
        model.addAttribute("product",product);

        return "item/product";
    }

    @GetMapping("/addProduct")
    public String addProduct(ProductForm productForm,Model model) {
        List<Category> categories = categoryService.getList();
        model.addAttribute("categories", categories);

        Category defaultCategory = this.categoryService.getCategory(1);
        model.addAttribute("defaultCategory",defaultCategory);

        return "item/addProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct(@Valid ProductForm productForm, BindingResult bindingResult, Model model) {
        List<Category> categories = categoryService.getList();
        model.addAttribute("categories", categories);

        Category defaultCategory = this.categoryService.getCategory(1);
        model.addAttribute("defaultCategory",defaultCategory);

        if(bindingResult.hasErrors()) {
            return "item/addProduct";
        }
        this.productService.addProduct(productForm.getCategory(), productForm.getProductName(), productForm.getPrice());

        return "redirect:/item/products";
    }

    @GetMapping("/product/{id}/edit")
    public String editProduct(ProductForm productForm, @PathVariable("id")Integer id,Model model) {
        List<Category> categories = categoryService.getList();
        model.addAttribute("categories", categories);

        Category defaultCategory = this.categoryService.getCategory(1);
        model.addAttribute("defaultCategory",defaultCategory);

        Product product = this.productService.getProduct(id);
        productForm.setCategory(product.getCategory());
        productForm.setProductName(product.getProductName());
        productForm.setPrice(product.getPrice());

        return "item/addProduct";
    }

    @PostMapping("/product/{id}/edit")
    public String editProduct(@PathVariable("id")Integer id,@Valid ProductForm productForm, BindingResult bindingResult,
                       Model model) {
        List<Category> categories = categoryService.getList();
        model.addAttribute("categories", categories);

        Category defaultCategory = this.categoryService.getCategory(1);
        model.addAttribute("defaultCategory",defaultCategory);

        if(bindingResult.hasErrors()) {
            return "item/addProduct";
        }
        Product product = this.productService.getProduct(id);
        this.productService.edit(product, productForm.getCategory(),productForm.getProductName(),productForm.getPrice());

        return "redirect:/item/product/{id}";
    }

    @GetMapping("/product/{id}/delete")
    public String deleteProduct(@PathVariable("id")Integer id) {
        Product product = this.productService.getProduct(id);
        this.productService.delete(product);

        return "redirect:/";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        List<Category> categories = this.categoryService.getList();
        model.addAttribute("categories",categories);

        return "item/categories";
    }

    @GetMapping("/category/{id}")
    public String category(Model model, @PathVariable("id") Integer id) {
        Category category = this.categoryService.getCategory(id);
        model.addAttribute("category", category);

        return "item/category";
    }

    @GetMapping("/addCategory")
    public String addCategory(CategoryForm categoryForm) {
        return "item/addCategory";
    }

    @PostMapping("/addCategory")
    public String addCategory(@Valid CategoryForm categoryForm,BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            return "item/addCategory";
        }
        this.categoryService.addCategory(categoryForm.getCategoryName());

        return "redirect:/item/categories";
    }

    @GetMapping("/category/{id}/edit")
    public String editCategory(CategoryForm categoryForm, @PathVariable("id")Integer id) {
        Category category = this.categoryService.getCategory(id);
        categoryForm.setCategoryName(category.getCategoryName());

        return "item/addCategory";
    }

    @PostMapping("/category/{id}/edit")
    public String editCategory(@Valid CategoryForm categoryForm,BindingResult bindingResult, @PathVariable("id")Integer id) {
        if(bindingResult.hasErrors()) {
            return "item/addCategory";
        }
        Category category = this.categoryService.getCategory(id);
        this.categoryService.editCategory(category,categoryForm.getCategoryName());

        return "redirect:/item/category/{id}";
    }

    @GetMapping("/category/{id}/delete")
    public String deleteCategory(@PathVariable("id") Integer id) {
        Category category = this.categoryService.getCategory(id);
        this.categoryService.delete(category);

        return "redirect:/";
    }

}
