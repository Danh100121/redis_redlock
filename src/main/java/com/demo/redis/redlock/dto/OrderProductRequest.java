package com.demo.redis.redlock.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderProductRequest {
    Long personId;

    Long productId;

    Integer orderCount;
}
