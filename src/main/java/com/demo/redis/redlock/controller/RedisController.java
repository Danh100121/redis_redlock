package com.demo.redis.redlock.controller;

import com.demo.redis.redlock.dto.OrderProductRequest;
import com.demo.redis.redlock.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/redis")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RedisController {
    private final OrderService orderService;
    private final RedissonClient redissonClient;

    @PostMapping(name = "상품 주문", value = "/product/order", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> orderProduct(@RequestBody @Valid OrderProductRequest request) {
        final long personId = request.getPersonId();
        orderService.orderProduct(personId, request.getProductId(), request.getOrderCount());

        return ResponseEntity.ok("Order Success");
    }
}
