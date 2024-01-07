package com.productscrud.api.controllers;

import com.productscrud.api.dtos.ProductRecordDto;
import com.productscrud.api.models.ProductModel;
import com.productscrud.api.repositories.IProductRepository;
import com.productscrud.api.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductModel> save(@RequestBody @Valid ProductRecordDto productRecordDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productRecordDto));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductModel>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAll());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Object> getOne(@PathVariable(value = "id") UUID id) {
        return notFoundOrOk(productService.getOne(id));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> update(
        @PathVariable(value = "id") UUID id,
        @RequestBody @Valid ProductRecordDto productRecordDto
    ) {
        return notFoundOrOk(productService.update(id, productRecordDto));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id) {
        return notFoundOrOk(productService.delete(id),"Product deleted successfully");
    }

    private ResponseEntity<Object> notFoundOrOk(Optional<ProductModel> product) {
        return notFoundOrOk(product, product.get());
    }

    private ResponseEntity<Object> notFoundOrOk(Optional<ProductModel> product, Object ok) {
        return notFoundOrOk(product, "Product not found", ok);
    }

    private ResponseEntity<Object> notFoundOrOk(Optional<ProductModel> product, Object notFound, Object ok) {
        return product.isEmpty()
            ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound)
            : ResponseEntity.status(HttpStatus.OK).body(ok);
    }
}
