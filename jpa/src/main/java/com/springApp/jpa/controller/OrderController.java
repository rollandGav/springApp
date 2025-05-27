package com.springApp.jpa.controller;

import com.springApp.jpa.entity.Order;
import com.springApp.jpa.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService service;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestParam Long userId, @RequestParam String product, @RequestParam Double price){
        Order order = service.placeOrder(userId, product, price);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> ordersWithPagination(@RequestParam Long userId, @RequestParam int page, @RequestParam int size){
        List<Order> orders = service.getOrdersWithPagination(userId, page, size);
        return ResponseEntity.ok(orders);
    }

}
