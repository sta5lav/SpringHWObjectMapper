package com.example.springobjectmapper.service;

import com.example.springobjectmapper.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface ProductService {

    List<String> getAllProducts();

    String getProductInfo(Long id);

    String createProduct(String jsonValue) throws JsonProcessingException;

    boolean updateProduct(String jsonValue);

    boolean deleteProduct(Long id);

    void checkProductInStock(Product orderProduct);
}
