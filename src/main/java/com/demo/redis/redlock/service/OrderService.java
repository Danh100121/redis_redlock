package com.demo.redis.redlock.service;

public interface OrderService {
    void orderProduct(long personId, Long productId, Integer orderCount);
}
