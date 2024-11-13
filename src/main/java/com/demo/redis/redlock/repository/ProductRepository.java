package com.demo.redis.redlock.repository;

import com.demo.redis.redlock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
