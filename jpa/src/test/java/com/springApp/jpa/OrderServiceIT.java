package com.springApp.jpa;


import com.springApp.jpa.entity.Order;
import com.springApp.jpa.entity.User;
import com.springApp.jpa.repository.OrderRepository;
import com.springApp.jpa.repository.UserRepository;
import com.springApp.jpa.service.OrderService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderServiceIT {

    @Autowired
    private OrderService service;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setName("Marcel");
        user.setUsername("marcel1");
        user.setEmail("margel1@gmail.com");
        testUser = userRepository.save(user);

        Order order = new Order();
        order.setProduct("Test product");
        order.setPrice(100.0);
        order.setOrderDate(LocalDate.now());
        order.setUser(testUser);
        testOrder = orderRepository.save(order);
    }

    @Test
    void placeOrder_ShouldCreateNewOrder() {
        String product = "New product";
        Double price = 140.0;

        Order placedOrder = service.placeOrder(testUser.getId(), product, price);

        assertNotNull(placedOrder);
        assertNotNull(placedOrder.getId());
        assertEquals(product, placedOrder.getProduct());
        assertEquals(price, placedOrder.getPrice());
        assertEquals(testUser.getId(), placedOrder.getUser().getId());

        assertTrue(orderRepository.findById(placedOrder.getId()).isPresent());
    }

    @Test
    void placedOrder_ShouldReturnNull_WhenUserDoesNotExist() {
        Order placedOrder = service.placeOrder(99L, "Some product", 30.0);
        assertNull(placedOrder);
    }

}
