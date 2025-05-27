package com.springApp.jpa.service;

import com.springApp.jpa.entity.Order;
import com.springApp.jpa.entity.User;
import com.springApp.jpa.repository.OrderRepository;
import com.springApp.jpa.repository.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    UserJpaRepository userRepository;
    @Autowired
    OrderRepository orderRepository;

    public Order placeOrder(Long userId, String product, Double price) {
        Optional<User> userOptional = userRepository.findById(userId);
        Order order = null;
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            order = new Order(product, price, LocalDate.now(), user);
        }
        else System.out.println("User not found for id: " + userId);

        return orderRepository.save(order);
    }

    public List<Order> getOrdersWithPagination(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Order> orderPage = orderRepository.findByUserId(userId, pageRequest);
        return orderPage.getContent();
    }
}
