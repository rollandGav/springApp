package com.springApp.jpa;

import com.springApp.jpa.entity.Order;
import com.springApp.jpa.entity.User;
import com.springApp.jpa.repository.OrderRepository;
import com.springApp.jpa.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
public class OrderControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    private User testUser;

    @BeforeEach
    void setUp(){
        User user = new User();
        user.setName("Marcel");
        user.setUsername("marcel1");
        user.setEmail("margel1@gmail.com");
        testUser = userRepository.save(user);
    }

    @Test
    void placeOrder_ShouldReturnCreatedOrder() throws Exception {
        String product = "Test product";
        Double price = 100.0;
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .param("userId", String.valueOf(testUser.getId()))
                        .param("product", product)
                        .param("price", String.valueOf(price))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.product").value(product))
                .andExpect(jsonPath("$.price").value(price));

       Order order = orderRepository.findByUserId(testUser.getId());
       assertEquals(product, order.getProduct());
       assertEquals(price, order.getPrice());
       assertEquals(testUser.getId(), order.getUser().getId());
    }


    @Test
    void ordersWithPagination_ShouldReturnPaginatedOrders() throws Exception {
        createOrders(testUser, 10);

        mockMvc.perform(MockMvcRequestBuilders.get("/orders")
                        .param("userId", String.valueOf(testUser.getId()))
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));

        mockMvc.perform(MockMvcRequestBuilders.get("/orders")
                        .param("userId", String.valueOf(testUser.getId()))
                        .param("page", "2")
                        .param("size", "3")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

    }

    private void createOrders(User user, int count) {
        for (int i = 0; i < count; i++) {
            Order o = new Order("Product " + i, 100.0 + i, LocalDate.now(), user);
            orderRepository.save(o);
        }
    }

}

