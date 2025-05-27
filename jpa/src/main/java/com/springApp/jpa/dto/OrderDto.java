package com.springApp.jpa.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class OrderDto {
    private String product;
    private Double price;
    private LocalDate orderDate;
}
