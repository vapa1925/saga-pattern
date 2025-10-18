package com.example.order.controller;

import com.example.order.model.Order;
import com.example.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public String create(@RequestBody Order order) {
        return orderService.createOrder(order);
    }
}