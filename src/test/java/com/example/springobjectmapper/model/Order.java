package com.example.springobjectmapper.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    private Customer customer;

    @OneToMany
    private List<Product> products;

    private Date orderDate;
    private String shippingAddress;
    private BigDecimal totalPrice;
    private String orderStatus;

}
