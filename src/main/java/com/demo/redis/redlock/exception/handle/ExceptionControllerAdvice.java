package com.demo.redis.redlock.exception.handle;

import com.demo.redis.redlock.dto.ErrorResponse;
import com.demo.redis.redlock.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionControllerAdvice {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> unknownErrorHandler(Exception e) {
        log.error("Unexpected Exception", e);
        String errKey = "err.sys.unexpected-exception";
        String errMsg = "Unknown error";
        return new ResponseEntity<>(ErrorResponse.builder().errKey(errKey).errMessage(errMsg).build(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<?> serviceErrorHandler(ServiceException e) {
        String errKey = e.getMessage();
        String errMsg = e.getErrMsg();
        log.error(errKey, errMsg);
        return new ResponseEntity<>(ErrorResponse.builder().errKey(errKey).errMessage(errMsg).build(),HttpStatus.BAD_REQUEST);
    }
}