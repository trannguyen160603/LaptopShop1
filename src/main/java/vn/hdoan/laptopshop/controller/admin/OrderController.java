package vn.hdoan.laptopshop.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.hdoan.laptopshop.domain.Order;
import vn.hdoan.laptopshop.domain.User;
import vn.hdoan.laptopshop.service.OrderService;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping("/admin/order")
    public String getDashboard(Model model,
                               @RequestParam("page") Optional<String> pageOptional) {
        // client: page = 1, limit = 10
        // database: offset + limit
        int page = 1;
        try {
            if(pageOptional.isPresent()){
                // convert from String to int
                page = Integer.parseInt(pageOptional.get());
            }else{
                // page = 1
            }
        } catch (Exception e){
            // page = 1
//             TODO: handle Exception
        }

        Pageable pageable = PageRequest.of(page - 1, 1);
        Page<Order> order = this.orderService.fetchAllOrders(pageable);
        List<Order> listOrders = order.getContent();
        model.addAttribute("orders", listOrders);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", order.getTotalPages());

        return "admin/order/show";
    }

    @GetMapping("/admin/order/{id}")
    public String getDashboard(Model model, @PathVariable long id) {
        Order order = this.orderService.fetchOrderById(id).get();
        model.addAttribute("order", order);
        model.addAttribute("id", id);
        model.addAttribute("orderDetails", order.getOrderDetails());
        return "admin/order/detail";
    }

    @GetMapping("/admin/order/delete/{id}")
    public String getDeleteOrderPage(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        model.addAttribute("newOrder", new Order());
        return "admin/order/delete";
    }

    @PostMapping("/admin/order/delete")
    public String postDeleteOrder(@ModelAttribute("newOrder") Order order) {
        this.orderService.deleteOrderById(order.getId());
        return "redirect:/admin/order";
    }

    @GetMapping("/admin/order/update/{id}")
    public String getUpdateOrderPage(Model model, @PathVariable long id) {
        Optional<Order> currentOrder = this.orderService.fetchOrderById(id);
        model.addAttribute("newOrder", currentOrder.get());
        return "admin/order/update";
    }

    @PostMapping("/admin/order/update")
    public String handleUpdateOrder(@ModelAttribute("newOrder") Order order) {
        this.orderService.updateOrder(order);
        return "redirect:/admin/order";
    }

}
