package vn.hdoan.laptopshop.controller.admin;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.hdoan.laptopshop.domain.Product;

import vn.hdoan.laptopshop.service.ProductService;
import vn.hdoan.laptopshop.service.UploadService;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final UploadService uploadService;
    private final ProductService productService;

    public ProductController(UploadService uploadService, ProductService productService){
        this.uploadService = uploadService;
        this.productService = productService;
    }

    @GetMapping("/admin/product")
    public String getProduct(Model model){
        List<Product> products = this.productService.fetchProducts();
        model.addAttribute("products", products);
        return "admin/product/show";
    }

    @GetMapping("admin/product/create")
    public String createProduct(Model model){
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping("/admin/product/create")
    public String handleCreateProduct(
            @ModelAttribute("newProduct") @Valid Product product,
            BindingResult newProductBindingResult,
            @RequestParam("usersFile") MultipartFile file) {
        // validate
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }

        // upload image
        String image = this.uploadService.handleSaveUploadFile(file, "product");
        product.setImage(image);

        this.productService.createProduct(product);

        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String getDeleteProductPage(Model model, @PathVariable long id){
        model.addAttribute("id", id);
        model.addAttribute("newProduct", new Product());
        return "admin/product/delete";
    }

    @PostMapping("/admin/product/delete")
    public String postDeleteProduct(Model model, @ModelAttribute("newProduct") Product product){
        this.productService.deleteProduct(product.getId());
        return "redirect:/admin/product";
    }

    @RequestMapping("/admin/product/update/{id}")
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        Optional<Product> currentProduct = this.productService.fetchProductById(id);
        model.addAttribute("newProduct", currentProduct.get());
        return "admin/product/update";
    }

    @PostMapping("/admin/product/update")
    public String handleUpdateProduct(Model model, @ModelAttribute("newProduct") @Valid Product product,
                                      BindingResult newProductBindingResult,
                                      @RequestParam("usersFile") MultipartFile multipartFile) {

        // validate
        if(newProductBindingResult.hasErrors()){
            return "admin/product/update";
        }
        Product currentProduct= this.productService.fetchProductById(product.getId()).get();
        if(currentProduct != null){
            // update new image
            if(!multipartFile.isEmpty()){
                String img = this.uploadService.handleSaveUploadFile(multipartFile, "product");
                currentProduct.setImage(img);
            }

            currentProduct.setName(product.getName());
            currentProduct.setPrice(product.getPrice());
            currentProduct.setQuantity(product.getQuantity());
            currentProduct.setDetailDesc(product.getDetailDesc());
            currentProduct.setShortDesc(product.getShortDesc());
            currentProduct.setFactory(product.getFactory());
            currentProduct.setTarget(product.getTarget());

            this.productService.createProduct(currentProduct);
        }
        return "redirect:/admin/product";
    }

    @GetMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product pr = this.productService.fetchProductById(id).get();
        model.addAttribute("product", pr);
        model.addAttribute("id", id);
        return "admin/product/detail";
    }

}
