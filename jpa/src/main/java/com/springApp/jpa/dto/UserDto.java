package com.springApp.jpa.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
@ToString
public class UserDto implements Serializable {
    private String name;
    private String lastName;
    private Integer age;
    private LocalDate localDate;
    private String email;
    private String username;
    private boolean active;
    private AddressDto address;
    private List<OrderDto> orders;
}
