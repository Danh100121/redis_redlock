package com.demo.redis.redlock.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long productId;

    Long personId;

    Integer quantity;

    public Order(Long productId, Long personId, Integer quantity) {
        this.productId = productId;
        this.personId = personId;
        this.quantity = quantity;
    }

    public Order() {

    }
}
