package com.example.springobjectmapper.service;

import com.example.springobjectmapper.model.Product;
import com.example.springobjectmapper.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;


    @Override
    public List<String> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::converter)
                .collect(Collectors.toList());
    }

    @Override
    public String getProductInfo(Long id) {
        return converter(productRepository.findById(id).orElse(null));
    }

    @Override
    public String createProduct(String jsonValue) {
        Product product = converter(jsonValue);
        if (!productRepository.existsById(product.getProductId())) {
            productRepository.save(product);
            return jsonValue;
        } else return null;
    }

    @Override
    public boolean updateProduct(String jsonValue) {
        Product productData = converter(jsonValue);
        Product productInRepository = productRepository.findById(productData.getProductId())
                .orElse(null);
        if (productRepository == null) {
            return false;
        }
        productInRepository.setName(productData.getName());
        productInRepository.setPrice(productData.getPrice());
        productInRepository.setDescription(productData.getDescription());
        productInRepository.setQuantityInStock(productData.getQuantityInStock());
        productRepository.save(productInRepository);
        return true;
    }

    @Override
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.delete(productRepository.findById(id).get());
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void checkProductInStock(Product orderProduct) {
        Optional<Product> productOptional
                = productRepository.findById(orderProduct.getProductId());
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            if (product.getQuantityInStock() > product.getQuantityInStock()) {
                throw new RuntimeException("Недостаточное количество продукта " + product.getName() + " на складе");
            }
            product.setQuantityInStock(product.getQuantityInStock() - orderProduct.getQuantityInStock());
            productRepository.save(product);
        } else {
            throw new RuntimeException("Продукт с идентификатором " + orderProduct.getProductId() + " не найден");
        }
    }


    private String converter(Product product) {
        try {
            return objectMapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Product converter(String jsonValue) {
        try {
            return objectMapper.readValue(jsonValue, Product.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
