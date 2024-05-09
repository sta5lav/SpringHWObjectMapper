package com.example.springobjectmapper.service;

import com.example.springobjectmapper.model.Order;
import com.example.springobjectmapper.model.Product;
import com.example.springobjectmapper.repository.OrderRepository;
import com.example.springobjectmapper.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(ObjectMapper objectMapper,
                            OrderRepository orderRepository,
                            ProductRepository productRepository) {
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }


    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional
    public Order createOrder(String orderJson) {
        Order order = converter(orderJson);
        List<Product> products = order.getProducts();
        for (Product orderProduct : products) {
            Optional<Product> productOptional = productRepository.findById(orderProduct.getProductId());
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                if (orderProduct.getQuantityInStock() > product.getQuantityInStock()) {
                    throw new RuntimeException("Недостаточное количество продукта " + product.getName() + " на складе");
                }
                product.setQuantityInStock(product.getQuantityInStock() - orderProduct.getQuantityInStock());
                productRepository.save(product);
            } else {
                throw new RuntimeException("Продукт с идентификатором " + orderProduct.getProductId() + " не найден");
            }
        }
        return orderRepository.save(order);
    }

    private Order converter(String jsonValue)  {
        try {
            return objectMapper.readValue(jsonValue, Order.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
