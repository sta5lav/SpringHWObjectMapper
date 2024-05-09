package com.example.springobjectmapper.controller;

import com.example.springobjectmapper.model.Order;
import com.example.springobjectmapper.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getInfoOrder(@PathVariable Long id) {
        Optional<Order> orderOptional = orderService.getOrderById(id);
        return orderOptional
                .map(order -> ResponseEntity.ok(order))
                .orElse(ResponseEntity.notFound().build());
    }



    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestBody String orderJson) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(orderService.createOrder(orderJson));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }




}
