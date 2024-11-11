package com.demo.redis.redlock.service.impl;

import com.demo.redis.redlock.dto.request.PersonUpdateRequest;
import com.demo.redis.redlock.entity.Person;
import com.demo.redis.redlock.repository.PersonRepository;
import com.demo.redis.redlock.service.PersonService;
import jakarta.transaction.Transactional;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private PersonRepository personRepository;

    private static final String LOCK_KEY_PREFIX = "personLock:";

    @Transactional
    @Override
    public String updatePerson(Long id, PersonUpdateRequest request) {
        String lockKey = LOCK_KEY_PREFIX + id;
        RLock lock = redissonClient.getLock(lockKey);

        try{
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)){
                Person person = personRepository.findById(id).orElseThrow(() -> new RuntimeException("Person not found"));

                person.setName(request.getName());
                person.setAge(request.getAge());
                personRepository.save(person);

                return "Person updated successfully";
            } else {
                return "Failed to acquired lock. Another precess is updating this record.";
            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            return "Error while updating person.";
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
