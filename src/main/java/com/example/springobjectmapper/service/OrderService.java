package com.example.springobjectmapper.service;

import com.example.springobjectmapper.model.Order;

import java.util.Optional;

public interface OrderService {

    Optional<Order> getOrderById(Long id);

    Order createOrder(String orderJson);
}
