package com.retailpos.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retailpos.productservice.entity.ProductCodeSequence;

public interface ProductCodeSequenceRepository
        extends JpaRepository<ProductCodeSequence, Long> {

}