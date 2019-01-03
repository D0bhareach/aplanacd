package com.github.zxxzru.aplanacd.disk;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class DiskNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(DiskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String diskNotFoundHandler(DiskNotFoundException ex) {
        return ex.getMessage();
    }
}

