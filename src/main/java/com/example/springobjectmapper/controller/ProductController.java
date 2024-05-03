package com.example.springobjectmapper.controller;

import com.example.springobjectmapper.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(productService.getAllProducts());
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<String> getProductInfo(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductInfo(id));
    }

    @PostMapping
    public ResponseEntity<String> createNewProduct(@RequestBody String jsonValue) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(productService.createProduct(jsonValue));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping
    public ResponseEntity<Void> updateProductInfo(@RequestBody String jsonValue) {
        return ResponseEntity.status(
                productService.updateProduct(jsonValue)?
                        HttpStatus.OK :
                        HttpStatus.NOT_FOUND
        ).build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.status(
                productService.deleteProduct(id) ?
                        HttpStatus.OK :
                        HttpStatus.NOT_FOUND
        ).build();
    }





}
