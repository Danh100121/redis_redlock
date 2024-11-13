package com.demo.redis.redlock.repository;

import com.demo.redis.redlock.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
