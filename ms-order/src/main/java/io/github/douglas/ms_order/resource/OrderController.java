package io.github.douglas.ms_order.resource;

import io.github.douglas.ms_order.dto.OrderRequest;
import io.github.douglas.ms_order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")

public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> registerOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(orderService.createOrder(request)).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrderDetails(id));
    }
}
