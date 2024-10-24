package vn.hdoan.laptopshop.service;

import org.springframework.stereotype.Service;
import vn.hdoan.laptopshop.domain.Product;
import vn.hdoan.laptopshop.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product){
        return this.productRepository.save(product);
    }

    public List<Product> fetchProducts(){
        return this.productRepository.findAll();
    }

}
