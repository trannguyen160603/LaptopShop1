package vn.hdoan.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hdoan.laptopshop.domain.Cart;
import vn.hdoan.laptopshop.domain.CartDetail;
import vn.hdoan.laptopshop.domain.Product;

public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {

    boolean existsByCartAndProduct(Cart cart, Product product);
    CartDetail findByCartAndProduct(Cart cart, Product product);

}
