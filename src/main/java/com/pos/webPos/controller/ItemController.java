package com.pos.webPos.controller;

import com.pos.webPos.category.Category;
import com.pos.webPos.category.CategoryForm;
import com.pos.webPos.category.CategoryService;
import com.pos.webPos.product.Product;
import com.pos.webPos.product.ProductForm;
import com.pos.webPos.product.ProductService;
import com.pos.webPos.session.PosSession;
import com.pos.webPos.session.PosSessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.SessionOwnerBehavior;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/item")
public class ItemController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final PosSessionService posSessionService;

    @GetMapping("")
    public String main(HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }
        return "item/main";
    }

    @GetMapping("/products")
    public String products(HttpSession session, Model model, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }
        PosSession posSession = this.posSessionService.getPosSessionOrElse(sessionId);

        List<Product> products = this.productService.getSessionProductList(posSession);
        model.addAttribute("products",products);

        return "item/products";
    }

    @GetMapping("/product/{id}")
    public String product(Model model, @PathVariable("id") Integer id, HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }

        Product product = this.productService.getProduct(id);
        model.addAttribute("product",product);

        return "item/product";
    }

    @GetMapping("/addProduct")
    public String addProduct(ProductForm productForm,Model model,HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }
        PosSession posSession = this.posSessionService.getPosSessionOrElse(sessionId);

        List<Category> categories = categoryService.getSessionCategoryList(posSession);
        model.addAttribute("categories", categories);

        Category defaultCategory = this.categoryService.getDefaultCategoryBySession(posSession);
        model.addAttribute("defaultCategory",defaultCategory);

        log.info("addProduct Get 성공");
        return "item/addProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct(@Valid ProductForm productForm, BindingResult bindingResult, Model model, HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }
        PosSession posSession = this.posSessionService.getPosSessionOrElse(sessionId);

        List<Category> categories = categoryService.getSessionCategoryList(posSession);
        model.addAttribute("categories", categories);

        Category defaultCategory = this.categoryService.getDefaultCategoryBySession(posSession);
        model.addAttribute("defaultCategory",defaultCategory);

        if(bindingResult.hasErrors()) {
            return "item/addProduct";
        }

        try {
            this.productService.addProduct(productForm.getCategory(), productForm.getProductName(), productForm.getPrice(), posSession);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("duplicate",e.getMessage());
            return "item/addProduct";
        }

        log.debug("addProduct Post 성공");
        return "redirect:/item/products";
    }

    @GetMapping("/product/{id}/edit")
    public String editProduct(ProductForm productForm, @PathVariable("id")Integer id,Model model, HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }
        PosSession posSession = this.posSessionService.getPosSessionOrElse(sessionId);

        List<Category> categories = categoryService.getSessionCategoryList(posSession);
        model.addAttribute("categories", categories);

        Category defaultCategory = this.categoryService.getDefaultCategoryBySession(posSession);
        model.addAttribute("defaultCategory",defaultCategory);

        Product product = this.productService.getProduct(id);
        productForm.setCategory(product.getCategory());
        productForm.setProductName(product.getProductName());
        productForm.setPrice(product.getPrice());

        return "item/addProduct";
    }

    @PostMapping("/product/{id}/edit")
    public String editProduct(@PathVariable("id")Integer id,@Valid ProductForm productForm, BindingResult bindingResult,
                       Model model, HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }
        PosSession posSession = this.posSessionService.getPosSessionOrElse(sessionId);

        List<Category> categories = categoryService.getSessionCategoryList(posSession);
        model.addAttribute("categories", categories);

        Category defaultCategory = this.categoryService.getDefaultCategoryBySession(posSession);
        model.addAttribute("defaultCategory",defaultCategory);

        if(bindingResult.hasErrors()) {
            return "item/addProduct";
        }
        Product product = this.productService.getProduct(id);

        try {
            this.productService.edit(product, productForm.getCategory(),productForm.getProductName(),productForm.getPrice(), posSession);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("duplicate",e.getMessage());
            return "item/addProduct";
        }

        return "redirect:/item/product/{id}";
    }

    @GetMapping("/product/{id}/delete")
    public String deleteProduct(@PathVariable("id")Integer id, HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }

        Product product = this.productService.getProduct(id);
        this.productService.delete(product);

        return "redirect:/item/products";
    }

    @GetMapping("/categories")
    public String categories(Model model, HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }
        PosSession posSession = this.posSessionService.getPosSessionOrElse(sessionId);

        List<Category> categories = this.categoryService.getSessionCategoryList(posSession);
        model.addAttribute("categories",categories);

        return "item/categories";
    }

    @GetMapping("/category/{id}")
    public String category(Model model, @PathVariable("id") Integer id, HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }

        Category category = this.categoryService.getCategory(id);
        model.addAttribute("category", category);

        return "item/category";
    }

    @GetMapping("/addCategory")
    public String addCategory(CategoryForm categoryForm, HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }

        return "item/addCategory";
    }

    @PostMapping("/addCategory")
    public String addCategory(@Valid CategoryForm categoryForm,BindingResult bindingResult, HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }
        PosSession posSession = this.posSessionService.getPosSessionOrElse(sessionId);

        if(bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            return "item/addCategory";
        }

        try {
            this.categoryService.addCategory(categoryForm.getCategoryName(), posSession);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("duplicate",e.getMessage());
            return "item/addCategory";
        }

        return "redirect:/item/categories";
    }

    @GetMapping("/category/{id}/edit")
    public String editCategory(CategoryForm categoryForm, @PathVariable("id")Integer id, HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }

        Category category = this.categoryService.getCategory(id);
        categoryForm.setCategoryName(category.getCategoryName());

        return "item/addCategory";
    }

    @PostMapping("/category/{id}/edit")
    public String editCategory(@Valid CategoryForm categoryForm,BindingResult bindingResult, @PathVariable("id")Integer id,
                               HttpSession session, HttpServletResponse response) {
        if(bindingResult.hasErrors()) {
            return "item/addCategory";
        }
        Category category = this.categoryService.getCategory(id);
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }
        PosSession posSession = this.posSessionService.getPosSessionOrElse(sessionId);

        try {
            this.categoryService.editCategory(category,categoryForm.getCategoryName(), posSession);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("duplicate",e.getMessage());
            return "item/addCategory";
        }

        return "redirect:/item/category/{id}";
    }

    @GetMapping("/category/{id}/delete")
    public String deleteCategory(@PathVariable("id") Integer id, HttpSession session, HttpServletResponse response) {
        String sessionId = session.getId();
        if(this.posSessionService.existOrCreatePosSession(sessionId)) {
            Cookie cookie = new Cookie("sessionId", sessionId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 12); // 12시간
            response.addCookie(cookie);
        }

        Category category = this.categoryService.getCategory(id);
        this.categoryService.delete(category);

        return "redirect:/item/categories";
    }

}
