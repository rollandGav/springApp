package com.springApp.jpa.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String lastName;
    private Integer age;
    private LocalDate localDate;
    private String email;
    private String username;
    private boolean active;
}
