package com.demo.redis.redlock.service.impl;

import com.demo.redis.redlock.entity.Order;
import com.demo.redis.redlock.entity.Product;
import com.demo.redis.redlock.exception.ServiceException;
import com.demo.redis.redlock.repository.OrderRepository;
import com.demo.redis.redlock.repository.ProductRepository;
import com.demo.redis.redlock.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final RedissonClient redissonClient;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public void orderProduct(long personId, Long productId, Integer orderCount) {
        final RLock lock = redissonClient.getLock(String.format("orderProduct:productId:%d", productId));
        try {
            // 타임아웃 설정
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                System.out.println("redisson lock timeout");
                throw new IllegalArgumentException();
            }

            String redisKey = String.format("product:%d", productId);
            Product product = (Product) redissonClient.getBucket(redisKey).get();

            if (Objects.isNull(product)) {
                product = productRepository.findById(productId)
                        .orElseThrow(() -> new ServiceException("Not found product with id: " + productId, "err.not-data-found"));
                redissonClient.getBucket(redisKey).set(product);
            }

            checkInventory(product, orderCount);

            orderRepository.save(new Order(personId, productId, orderCount));

            subtractInventoryCount(product, orderCount);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    private void checkInventory(Product product, Integer orderCount) {
        if (orderCount > product.getInventoryCount()) {
            throw new ServiceException("Order count exceeds available inventory", "err.inventory-exceeded");
        }
    }

    private void subtractInventoryCount(Product product, Integer orderCount) {
        int newInventoryCount = product.getInventoryCount() - orderCount;
        product.setInventoryCount(newInventoryCount);
        productRepository.save(product);

        String redisKey = String.format("product:%d", product.getId());
        redissonClient.getBucket(redisKey).set(product);
    }
}
