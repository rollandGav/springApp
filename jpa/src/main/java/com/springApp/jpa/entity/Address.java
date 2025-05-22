package com.springApp.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue
    private Long id;
    private String city;
    private String street;
    private String country;
    private Integer numberOfStreet;

    @OneToOne(mappedBy = "address")
    private User user;
}
