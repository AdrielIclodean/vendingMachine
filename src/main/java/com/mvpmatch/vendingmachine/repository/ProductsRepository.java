package com.mvpmatch.vendingmachine.repository;

import com.mvpmatch.vendingmachine.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {

    void deleteBySellerId(long id);
}
