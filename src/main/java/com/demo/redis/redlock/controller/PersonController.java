package com.demo.redis.redlock.controller;

import com.demo.redis.redlock.dto.request.PersonUpdateRequest;
import com.demo.redis.redlock.service.PersonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PersonController {
    PersonService personService;

    @PutMapping("/{id}")
    public String updatePerson(@PathVariable Long id, @RequestBody PersonUpdateRequest request) {
        return personService.updatePerson(id, request);
    }
}
