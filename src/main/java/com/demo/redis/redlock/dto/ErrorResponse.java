package com.demo.redis.redlock.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse{

    private final String errKey;
    private final String errMessage;

}
