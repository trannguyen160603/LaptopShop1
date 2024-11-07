package vn.hdoan.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hdoan.laptopshop.domain.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
