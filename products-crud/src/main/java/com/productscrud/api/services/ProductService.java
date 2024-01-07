package com.productscrud.api.services;

import com.productscrud.api.controllers.ProductController;
import com.productscrud.api.dtos.ProductRecordDto;
import com.productscrud.api.models.ProductModel;
import com.productscrud.api.repositories.IProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ProductService {
    @Autowired
    private IProductRepository productRepository;

    public ProductModel save(ProductRecordDto productRecordDto) {
        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        return productRepository.save(productModel);
    }

    public List<ProductModel> getAll() {
        var productsList = productRepository.findAll();
        if (!productsList.isEmpty()) {
            for (var product : productsList) {
                product.add(linkTo(methodOn(ProductController.class).getOne(product.getIdProducts())).withRel("Product details"));
            }
        }
        return productsList;
    }

    public Optional<ProductModel> getOne(UUID id) {
        var product = productRepository.findById(id);
        if (product.isEmpty()) {
            return product;
        }

        product.get().add(linkTo(methodOn(ProductController.class).getAll()).withRel("Products list"));
        return product;
    }

    public Optional<ProductModel> update(UUID id, ProductRecordDto productRecordDto) {
        var product = productRepository.findById(id);
        if (product.isEmpty()) {
            return product;
        }

        var productModel = product.get();
        BeanUtils.copyProperties(productRecordDto, productModel);
        productRepository.save(productModel);
        return productRepository.findById(id);
    }

    public Optional<ProductModel>  delete(UUID id) {
        var product = productRepository.findById(id);
        if (product.isEmpty()) {
            return product;
        }
        productRepository.delete(product.get());
        return product;
    }
}

