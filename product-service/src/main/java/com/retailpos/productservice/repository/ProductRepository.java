package com.retailpos.productservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retailpos.productservice.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findByProductCode(String productCode);

    boolean existsByProductCode(String productCode);

    List<Product> findByActiveTrue();

    Optional<Product> findByIdAndActiveTrue(Long id);

	boolean existsByProductNameIgnoreCaseAndBrandIgnoreCase(String productName, String brand);

	boolean existsByProductNameIgnoreCaseAndBrandIgnoreCaseAndIdNot(String productName, String brand, Long id);


}