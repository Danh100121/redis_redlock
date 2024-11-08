package com.demo.redis.redlock.service;

import com.demo.redis.redlock.dto.request.PersonUpdateRequest;
import com.demo.redis.redlock.dto.response.PersonResponse;

public interface PersonService {

    String updatePerson(Long id, PersonUpdateRequest request);
}
